package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegistrationStep1 extends AppCompatActivity {
    EditText name, surname, date;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step1);

        firebaseAuth = FirebaseAuth.getInstance();


    //}

      //  name=findViewById(R.id.txtBoxStep1Name);
       // surname=findViewById(R.id.txtBoxStep1Surname);
        //btStep2=findViewById(R.id.btnStep1Next);

       // btStep2.setOnClickListener(new View.OnClickListener() {
          //  @Override
         //   public void onClick(View v) {
           //     String name1=name.getText().toString();
           //     String surname1=surname.getText().toString();
          //      Intent intent = new Intent(getApplicationContext(), RegistrationStep2.class);
           //     intent.putExtra("message_key", name1);
           //     startActivity(intent);
    //       });

        }




    public void OpenRegistrationStep2(View view) {
        name=findViewById(R.id.txtBoxStep1Name);
        surname=findViewById(R.id.txtBoxStep1Surname);
     //  date=findViewById(R.id.txtStep1Date);
     //   String pattern = "MM/dd/yyyy";
     //   DateFormat df = new SimpleDateFormat(pattern);
    //    String date1 = df.format(date);
        String Name=name.getText().toString();
        String Surname=surname.getText().toString();
        Intent open = new Intent(getApplicationContext(), RegistrationStep2.class);
        open.putExtra("name_key", Name);
        open.putExtra("surname_key", Surname);
   //     open.putExtra("date_key", date1);

        startActivity(open);

    }
    public void OpenLogIn(View view){
        Intent open = new Intent(RegistrationStep1.this, MainActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft,R.anim.stayinplace);
    }




}