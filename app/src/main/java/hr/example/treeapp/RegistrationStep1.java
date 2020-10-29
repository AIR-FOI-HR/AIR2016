package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
<<<<<<< HEAD
=======

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
>>>>>>> 512dae518b1901f8ae4a72739a40f614df43328c

public class RegistrationStep1 extends AppCompatActivity {
    EditText name, surname, date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step1);

<<<<<<< HEAD
    }
=======
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

>>>>>>> 512dae518b1901f8ae4a72739a40f614df43328c


    public void OpenRegistrationStep2(View view) {
        name=findViewById(R.id.txtBoxStep1Name);
        surname=findViewById(R.id.txtBoxStep1Surname);
     //  date=findViewById(R.id.txtStep1Date);
     //   String pattern = "MM/dd/yyyy";
     //   DateFormat df = new SimpleDateFormat(pattern);
    //    String date1 = df.format(date);
        String name1=name.getText().toString();
        String surname1=surname.getText().toString();
        Intent open = new Intent(getApplicationContext(), RegistrationStep2.class);
        open.putExtra("name_key", name1);
        open.putExtra("surname_key", surname1);
   //     open.putExtra("date_key", date1);
        startActivity(open);
    }
    public void OpenLogIn(View view){
        Intent open = new Intent(RegistrationStep1.this, MainActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft,R.anim.stayinplace);
    }

<<<<<<< HEAD
=======

>>>>>>> 512dae518b1901f8ae4a72739a40f614df43328c
}