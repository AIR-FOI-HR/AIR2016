package auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.example.treeapp.R;

public class AuthRepository {

    public static final int RC_SIGN_IN = 123;
    public static FirebaseUser user;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private GoogleSignInAccount account;
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private Context context;

    private boolean googleSignin = false;
    public String returnValue;

    public AuthRepository(Context context) {
        this.context = context;
    }

    public void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(String.valueOf("207643504221-mtkrdhkctrvcpq18cr7n8m8a0vldi4a1.apps.googleusercontent.com"))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void requestCheck(int requestCode, Intent data) throws ApiException {

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                throw e;
            }
        }
    }

    public Intent getSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }

    public void firebaseAuthWithGoogle(String idToken) {
        //kod this, executor je bacalo grešku
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (account != null) {
                                DocumentReference documentReference = firebaseFirestore.collection("Korisnici").document(user.getUid());
                                Map<String, Object> korisnik = new HashMap<>();
                                korisnik.put("Ime", account.getGivenName());
                                korisnik.put("Prezime", account.getFamilyName());
                                korisnik.put("E-mail", account.getEmail());
                                korisnik.put("Bodovi", 0);
                                korisnik.put("Uloga_ID", 2);
                                korisnik.put("Datum_rodenja", null);
                                String googleEmail = account.getEmail();
                                //za ovaj dio možda ne bi bilo loše napraviti popup prozor u koji korisnik može upisati kor ime, da mu ne namećemo kor ime
                                korisnik.put("Korisnicko_ime", googleEmail.split("@")[0]);
                                Uri photoUri = account.getPhotoUrl();
                                korisnik.put("Profilna_slika_ID", photoUri.toString());
                                documentReference.set(korisnik).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    //ne razumijem za što služi ovaj void, ako nema svrhu treba ga ukloniti
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            //Toast.makeText(MainActivity.this, getText(R.string.authentication_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void login(String emailVal, String passwordVal, final LogInStatusCallback loginCallback) {
        String value;
        firebaseAuth.signInWithEmailAndPassword(emailVal, passwordVal).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    user = firebaseAuth.getCurrentUser();
                    if (!user.isEmailVerified()) {
                        setValueMethod("notVerified");
                    } else {
                        setValueMethod("ok");
                    }
                    loginCallback.onCallback(returnValue);
                } else {
                    // Sign in fail
                    setValueMethod("error");
                    loginCallback.onCallback(returnValue);
                }

            }
        });
    }

    public void setValueMethod(String value) {
        returnValue = value;
    }

    public String dostupno;
    List<String> listaKorisnickihImena = new ArrayList<String>();
    int i = 0;

    public void checkUsernameAvailability(String korime, final UsernameAvailabilityCallback usernameAvailabilityCallback) {
        firebaseFirestore.collection("Korisnici")
                .whereEqualTo("Korisnicko_ime", korime)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listaKorisnickihImena.add(document.get("Korisnicko_ime").toString());
                                i++;
                                if(i==3){
                                    break;
                                }
                            }
                            if(listaKorisnickihImena.contains(korime)){
                                dostupno = "Zauzeto";
                                usernameAvailabilityCallback.onCallback(dostupno);
                            }
                            else{
                                dostupno = "Dostupno";
                                usernameAvailabilityCallback.onCallback(dostupno);
                            }
                        } else {
                            usernameAvailabilityCallback.onCallback(dostupno);
                        }
                    }
                });
    }

    public void guest() {

        firebaseAuth.signInAnonymously()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            user = firebaseAuth.getCurrentUser();

                        } else {
                            // Sign in fail

                        }
                    }
                });
    }


}
