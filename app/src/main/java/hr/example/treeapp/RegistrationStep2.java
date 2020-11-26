package hr.example.treeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import auth.AuthRepository;
import auth.RegistrationRepository;
import auth.UsernameAvailabilityCallback;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

public class RegistrationStep2 extends AppCompatActivity {
    EditText email, korIme, password, repeatedPassword;
    String Ime, Prezime, userID, Slika, slikaID;
    int day, month, year;
    String datumRodenja;
    public Boolean KorImeZauzeto;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    AuthRepository authRepository;
    RegistrationRepository registrationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step2);

        authRepository= new AuthRepository(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        registrationRepository = new RegistrationRepository(this);

        Intent intent = getIntent();
        Ime = intent.getStringExtra("name_key");
        Prezime = intent.getStringExtra("surname_key");

        Bundle extras = getIntent().getExtras();
        day = extras.getInt("day_key");
        month = extras.getInt("month_key");
        year = extras.getInt("year_key");

        datumRodenja = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

        String extraStr = extras.getString("image_key");
        if (extraStr == null) {
            Log.d("Poruka", "Nema slike");
        } else {
            Log.d("Poruka", "Ima slike");
            Slika = extraStr;
            Log.d("Poruka", Slika);
        }


        Log.d("Poruka", Ime);
        Log.d("Poruka", Prezime);
        Log.d("Poruka", String.valueOf(day));
        Log.d("Poruka", String.valueOf(month));
        Log.d("Poruka", String.valueOf(year));


        //dohvacanje upisanih podataka kao objekte
        email = (EditText) findViewById(R.id.txtBoxStep2Email);
        korIme = (EditText) findViewById(R.id.txtBoxStep2Username);
        password = (EditText) findViewById(R.id.txtBoxStep2Password);
        repeatedPassword = (EditText) findViewById(R.id.txtBoxStep2PasswordRepeat);

        //kreiranje instance Firebase autentikacije
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

    }

    public void OpenRegistrationStep1(View view) {
        Intent open = new Intent(RegistrationStep2.this, RegistrationStep1.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft, R.anim.stayinplace);
    }

    public boolean korimeDostupno;

    public void OpenRegistrationStep3(View view) {
     //kreiranje stringova upisanih podataka
        String KorIme = korIme.getText().toString().trim();

        registrationRepository.checkUsernameAvailability(KorIme, new UsernameAvailabilityCallback() {
            @Override
            public void onCallback(String value) {
                if (value == "Dostupno") {
                    korimeDostupno = true;
                    OtvoriRegistrationStep3(KorIme);
                }
                else{
                    korimeDostupno = false;
                    korIme.setError(getString(R.string.username_taken));
                }
            }
        });

    }

    public void OtvoriRegistrationStep3(String KorIme){
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String RepeatedPassword = repeatedPassword.getText().toString().trim();
        Integer Bodovi = 0;
        Integer UlogaID = 2;


        //provjerava je li upisan e-mail

        if (TextUtils.isEmpty(Email)) {
            email.setError(getString(R.string.no_email));
            return;
        }

        //provjerava je li struktura e-maila točna
        //TODO: ovaj dio provjere se treba obavljati u poslovnoj logici bool emailIsOk (string email)
        if ((Pattern.compile("^[a-zA-Z0-9.-]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$").matcher(Email).matches()) == false) {
            email.setError(getString(R.string.invalid_email));
            return;
        }

        //provjerava je li upisana lozinka
        if (TextUtils.isEmpty(Password)) {
            password.setError(getString(R.string.no_password));
            return;
        }

        //provjerava sadrži li lozinka između 6 i 20 znakova, barem 1 veliko slovo i jedan broj
        //TODO: ovaj dio provjere se treba obavljati u poslovnoj logici bool passwordIsOk (string password)
        if ((Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{6,20}$").matcher(Password).matches()) == false) {
            password.setError(getString(R.string.invalid_password));
            return;
        }


        //provjerava je li točno upisana ponovljena lozinka
        if (TextUtils.isEmpty(RepeatedPassword) || !RepeatedPassword.equals(Password)) {
            repeatedPassword.setError(getString(R.string.invalid_password_repeat));
            return;
        }

        //upisuje korisnika u Authentication i Database kolekciju korisnici ukoliko nema pogreške
        registrationRepository.firebaseCreateUser(Email, Password, Ime, Prezime, Bodovi, KorIme, Slika, datumRodenja, UlogaID);
        Intent open = new Intent(RegistrationStep2.this, RegistrationStep3.class);
        open.putExtra("email", Email);
        startActivity(open);
    }

    public void OpenLogIn(View view) {
        Intent open = new Intent(RegistrationStep2.this, MainActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft, R.anim.stayinplace);
    }
}
