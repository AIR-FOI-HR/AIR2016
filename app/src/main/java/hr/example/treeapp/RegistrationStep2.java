package hr.example.treeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.protobuf.DescriptorProtos;

import org.w3c.dom.Text;

public class RegistrationStep2 extends AppCompatActivity {
    EditText email, korIme, password, repeatedPassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step2);

        Intent intent=getIntent();
        String str_name = intent.getStringExtra("name_key");
        String str_surname = intent.getStringExtra("surname_key");
      //  String str_date = (String) intent.getSerializableExtra("date_key");
        Log.d("Poruka", str_name);
        Log.d("Poruka", str_surname);
    //    Log.d("Poruka", str_date);

        //dohvacanje upisanih podataka kao objekte
        email = (EditText)findViewById(R.id.txtBoxStep2Email);
        korIme = (EditText)findViewById(R.id.txtBoxStep2Username);
        password = (EditText)findViewById(R.id.txtBoxStep2Password);
        repeatedPassword = (EditText)findViewById(R.id.txtBoxStep2PasswordRepeat);

        //kreiranje instance Firebase autentikacije
        firebaseAuth = FirebaseAuth.getInstance();

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

        //provjerava je li upisan e-mail
        if(TextUtils.isEmpty(Email)){
            email.setError("Upišite važeći e-mail.");
            return;
        }

        //provjerava je li upisana lozinka
        if(TextUtils.isEmpty(Password)){
            password.setError("Unesite lozinku!");
            return;
        }

        //provjerava sadrži li lozinka najmanje 6 znakova
        if(Password.length() < 6){
            password.setError("Lozinka mora sadržavati najmanje 6 znakova.");
            return;
        }

        //provjerava je li točno upisana ponovljena lozinka
        if(TextUtils.isEmpty(RepeatedPassword) || !RepeatedPassword.equals(Password)){
            repeatedPassword.setError("Ponovite lozinku.");
            return;
        }

        //upisuje korisnika u Authentication i Database kolekciju korisnici ukoliko nema pogreške
        firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent open = new Intent(RegistrationStep2.this, RegistrationStep3.class);
                    startActivity(open);
                }else{

                }
            }
        });


    }
    public void OpenLogIn(View view){
        Intent open = new Intent(RegistrationStep2.this, MainActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft,R.anim.stayinplace);
    }
}