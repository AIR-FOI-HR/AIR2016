package hr.example.treeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 123;
    EditText email, password;
    FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore firebaseFirestore;
    GoogleSignInAccount account;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=(EditText)findViewById(R.id.txtEmail3);
        password=(EditText)findViewById(R.id.txtPassword3);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        createRequest();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            startActivity(new Intent(getApplicationContext(), LoginTest.class));
        }
    }
    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void signIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                //Toast.makeText(this, "Error:  " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(account!=null){
                                DocumentReference documentReference = firebaseFirestore.collection("Korisnici").document(user.getUid());
                                Map<String, Object> korisnik = new HashMap<>();
                                korisnik.put("Ime", account.getGivenName());
                                korisnik.put("Prezime", account.getFamilyName());
                                korisnik.put("E-mail", account.getEmail());
                                korisnik.put("Bodovi", 0);
                                korisnik.put("Uloga_ID", 2);
                                korisnik.put("Datum_rodenja", null);
                                String googleEmail=account.getEmail();
                                korisnik.put("Korisnicko_ime", googleEmail.split("@")[0]);
                                Uri photoUri= account.getPhotoUrl();
                                korisnik.put("Profilna_slika_ID", photoUri.toString());
                                documentReference.set(korisnik).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                            }
                            startActivity(new Intent(getApplicationContext(), LoginTest.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, getText(R.string.authentication_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void login(View view) {
        String emailVal=email.getText().toString().trim();
        String passwordVal=password.getText().toString();

        if(inputValidation(emailVal, passwordVal)) {
            firebaseAuth.signInWithEmailAndPassword(emailVal, passwordVal).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if(!user.isEmailVerified()) {
                            email.setError(getString(R.string.email_unconfirmed));
                            password.getText().clear();
                            return;
                        }
                        startActivity(new Intent(getApplicationContext(), LoginTest.class));
                        finish();
                    } else {
                        // Sign in fail
                        password.getText().clear();
                        password.setError(getText(R.string.invalid_password_login));
                    }
                }
            });
        }
    }



    public void OpenRegistration(View view) {
        Intent open = new Intent(MainActivity.this, RegistrationStep1.class);
        startActivity(open);
    }

    public void OpenReset(View view){
        Intent open= new Intent(MainActivity.this,PassReset.class);
        startActivity(open);
    }


    private boolean inputValidation(String emailVal, String passwordVal){
        if(TextUtils.isEmpty(emailVal)){
            email.setError(getString(R.string.no_email));
        }
        if(TextUtils.isEmpty(passwordVal)){
            password.setError(getString(R.string.no_password));
        }
        return !TextUtils.isEmpty(emailVal) && !TextUtils.isEmpty(passwordVal);
    }


    public void guest(View view) {
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), LoginTest.class));
                            finish();
                        } else {
                            // Sign in fail
                            Toast.makeText(MainActivity.this, getText(R.string.authentication_failed),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}