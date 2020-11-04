package hr.example.treeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.DescriptorProtos;
import com.google.type.DateTime;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class RegistrationStep2 extends AppCompatActivity {
    EditText email, korIme, password, repeatedPassword;
    String Ime, Prezime, userID, Slika, slikaID;
    int day,month,year;
    String datumRodenja;
    public Boolean KorImeZauzeto;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step2);

        Intent intent=getIntent();
        Ime = intent.getStringExtra("name_key");
        Prezime = intent.getStringExtra("surname_key");

        Bundle extras = getIntent().getExtras();
        day = extras.getInt("day_key");
        month = extras.getInt("month_key");
        year = extras.getInt("year_key");

        datumRodenja = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

        String extraStr = extras.getString("image_key");
        if(extraStr==null){
            Log.d("Poruka", "Nema slike");
        }
        else{
            Log.d("Poruka", "Ima slike");
            Slika=extraStr;
            Log.d("Poruka", Slika);
        }


        Log.d("Poruka", Ime);
        Log.d("Poruka", Prezime);
        Log.d("Poruka", String.valueOf(day));
        Log.d("Poruka", String.valueOf(month));
        Log.d("Poruka", String.valueOf(year));



        //dohvacanje upisanih podataka kao objekte
        email = (EditText)findViewById(R.id.txtBoxStep2Email);
        korIme = (EditText)findViewById(R.id.txtBoxStep2Username);
        password = (EditText)findViewById(R.id.txtBoxStep2Password);
        repeatedPassword = (EditText)findViewById(R.id.txtBoxStep2PasswordRepeat);

        //kreiranje instance Firebase autentikacije
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

    }
    public void OpenRegistrationStep1(View view) {
        Intent open = new Intent(RegistrationStep2.this, RegistrationStep1.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft,R.anim.stayinplace);
    }
    public void OpenRegistrationStep3(View view) {
        //kreiranje stringova upisanih podataka
        String Email = email.getText().toString().trim();
        String KorIme = korIme.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String RepeatedPassword = repeatedPassword.getText().toString().trim();
        Integer Bodovi = 0;
        Integer UlogaID = 2;


     //   ProvjeraKorisnickogImena2("Korisnicko_ime", KorIme, new OnSuccessListener<Boolean>() {
     //       @Override
     //       public void onSuccess(Boolean aBoolean) {
     //           if(!aBoolean){
     //               KorImeZauzeto = true;
     //           }
     //       }
     //   });

     //   if(KorImeZauzeto == true){
      //      korIme.setError(getString(R.string.username_taken));
      //      return;
     //   }

        //provjerava je li upisan e-mail
        if(TextUtils.isEmpty(Email)){
            email.setError(getString(R.string.no_email));
            return;
        }

        //provjerava je li struktura e-maila točna
        if((Pattern.compile("^[a-zA-Z0-9.-]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$").matcher(Email).matches()) == false){
            email.setError(getString(R.string.invalid_email));
            return;
        }

        //provjerava je li upisana lozinka
        if(TextUtils.isEmpty(Password)){
            password.setError(getString(R.string.no_password));
            return;
        }

        //provjerava sadrži li lozinka između 6 i 20 znakova, barem 1 veliko slovo i jedan broj
        if((Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{6,20}$").matcher(Password).matches()) == false){
            password.setError(getString(R.string.invalid_password));
            return;
        }


        //provjerava je li točno upisana ponovljena lozinka
        if(TextUtils.isEmpty(RepeatedPassword) || !RepeatedPassword.equals(Password)){
            repeatedPassword.setError(getString(R.string.invalid_password_repeat));
            return;
        }

        //upisuje korisnika u Authentication i Database kolekciju korisnici ukoliko nema pogreške
        firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userID = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firebaseFirestore.collection("Korisnici").document(userID);
                    Map<String, Object> korisnik = new HashMap<>();
                    korisnik.put("Ime", Ime);
                    korisnik.put("Prezime", Prezime);
                    korisnik.put("E-mail", Email);
                    korisnik.put("Bodovi", Bodovi);
                    korisnik.put("Korisnicko_ime", KorIme);
                    if(!TextUtils.isEmpty(Slika)) {
                        UploadPicture();
                    }
                    korisnik.put("Profilna_slika_ID", slikaID);
                    korisnik.put("Datum_rodenja", datumRodenja);
                    korisnik.put("Uloga_ID", UlogaID);
                    documentReference.set(korisnik).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent open = new Intent(RegistrationStep2.this, RegistrationStep3.class);
                            startActivity(open);
                        }
                    });
                }else{

                }
            }
        });


    }

    public void ProvjeraKorisnickogImena(String KorIme){
       Query query = firebaseFirestore.collection("Korisnici")
                .whereEqualTo("Korisnicko_ime", KorIme);

       query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.getResult().getDocuments() != null){
                   korIme.setError(getString(R.string.username_taken));

               }
           }
       });

    }

    public void ProvjeraKorisnickogImena2(String key, String value, OnSuccessListener<Boolean> onSuccessListener) {
        firebaseFirestore.collection("Korisnici").whereEqualTo(key, value).addSnapshotListener(new EventListener<QuerySnapshot>() {
            private boolean isRunOneTime = false;

            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (!isRunOneTime) {
                    isRunOneTime = true;
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    if (e != null) {
                        e.printStackTrace();
                        String message = e.getMessage();
                        onSuccessListener.onSuccess(false);
                        return;
                    }

                    if (snapshotList.size() > 0) {
                        //Field is Exist
                        onSuccessListener.onSuccess(false);
                    } else {
                        onSuccessListener.onSuccess(true);
                    }

                }
            }
        });
    }

    public void OpenLogIn(View view){
        Intent open = new Intent(RegistrationStep2.this, MainActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft,R.anim.stayinplace);
    }

    private void UploadPicture(){

        slikaID = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("Profilne_slike/" + slikaID);

        Uri myUri = Uri.parse(Slika);

        riversRef.putFile(myUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(findViewById(android.R.id.content), "Slika uploadana.", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Slika nije uploadana.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}