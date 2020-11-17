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
        firebaseAuth = FirebaseAuth.getInstance(); // TODO: ide u database
        firebaseFirestore = FirebaseFirestore.getInstance(); //TODO: ide u database
        firebaseStorage = FirebaseStorage.getInstance();//TODO: ide u database
        storageReference = firebaseStorage.getReference();//TODO: ide u database
        createRequest();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        //treba li ovaj dio ako imamo splash screen gdje se to provjerava?
        //TODO: napraviti metodu tipa FirebaseUser getCurrentUser koja će vraćati logiranog korisnika, naopraviti ju database
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            startActivity(new Intent(getApplicationContext(), LoginTest.class));
        }
    }
    private void createRequest() {
        // Configure Google Sign In
        // TODO: ovo ispod može ići u database i tamo se pozivati (napraviti void getGoogleToken u kojem će ovo biti)
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
                // TODO: tu treba handlati error, najbolje možda ovaj dio u database -> proslijediti error u logiku i prikazati ga u UI
                // Google Sign In failed, update UI appropriately
                // ...
                //Toast.makeText(this, "Error:  " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
//TODO: ova cijela metoda može u database (tu se kreira novi korisnik preko Google SignIn), idToken dohvaća se od getGoogleToken iz database, a ulazni parametri mogu biti tipa user ili stringovi s podacima o korisniku.
    //tu će se samo pozvati metoda iz database i proslijediti podaci o korisniku ili
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
                                //za ovaj dio možda ne bi bilo loše napraviti popup prozor u koji korisnik može upisati kor ime, da mu ne namećemo kor ime
                                korisnik.put("Korisnicko_ime", googleEmail.split("@")[0]);
                                Uri photoUri= account.getPhotoUrl();
                                korisnik.put("Profilna_slika_ID", photoUri.toString());
                                documentReference.set(korisnik).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    //ne razumijem za što služi ovaj void, ako nema svrhu treba ga ukloniti
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                            }
                            startActivity(new Intent(getApplicationContext(), LoginTest.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            //TODO: toast ostaje u UI, ali se pokreće temeljem bool GoogleLogInSuccess koji je true ako je sve oke, a false ako nije
                            Toast.makeText(MainActivity.this, getText(R.string.authentication_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

//TODO: void login je u poslovnoj logici, tu ostaje dohvaćanje emaila i passworda koji se prosljeđuju u sloj poslovne logike, a ono što je direktno povezano s bazom ide u database. IF (inputValidation)-> login iz poslovne logike
    //možda napraviti metodu u poslovnoj logici koja poziva firebaseAuth
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
        //TODO: terba napraviti da se ne radi novi korisnik svaki put nakon što se korisnik odjavi i prijavi kao guset, isti id treba ostati sve dok korisnik ima aplikaciju na uređaju
        //pokreće se metoda u poslovnoj logici koja poziva metodu iz database gdje se kreira anonym korisnik, a dio s provjerom se hendla u poslovnoj logici
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