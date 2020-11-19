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
import java.util.concurrent.Executor;

import auth.AuthRepository;

public class MainActivity extends AppCompatActivity {

    //ostaje
    private static final int RC_SIGN_IN = 123;
    EditText email, password;
    AuthRepository authRepository= new AuthRepository(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=(EditText)findViewById(R.id.txtEmail3);
        password=(EditText)findViewById(R.id.txtPassword3);
        authRepository.createRequest();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void signIn(View view) {
        Intent signInIntent = authRepository.getSignInIntent();
        startActivityForResult(signInIntent, authRepository.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            authRepository.requestCheck(requestCode,data);
        }
        catch (ApiException e){
            Toast.makeText(this, "Error:  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    /**
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
     **/

    public void login(View view) {
        String emailVal=email.getText().toString().trim();
        String passwordVal=password.getText().toString();

        String checkValue="";


        if(inputValidation(emailVal,passwordVal)){
            checkValue=authRepository.login(emailVal,passwordVal);
        }


        if(checkValue=="ok"){
            startActivity(new Intent(getApplicationContext(), LoginTest.class));
            finish();
        }
        else if(checkValue=="notVerified"){
            email.setError(getString(R.string.email_unconfirmed));
            password.getText().clear();
        }
        else{
            password.getText().clear();
            password.setError(getText(R.string.invalid_password_login));
        }
        /**
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

                    } else {
                        // Sign in fail
                        password.getText().clear();
                        password.setError(getText(R.string.invalid_password_login));
                    }
                }
            });*/
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
        /**
         * //TODO: terba napraviti da se ne radi novi korisnik svaki put nakon što se korisnik odjavi i prijavi kao guset, isti id treba ostati sve dok korisnik ima aplikaciju na uređaju
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
                });*/
        authRepository.guest((Executor) this);
    }
}