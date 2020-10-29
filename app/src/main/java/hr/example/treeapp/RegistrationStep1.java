package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegistrationStep1 extends AppCompatActivity {
    EditText name, surname, date;
    ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST=71;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step1);
        imageView=(ImageView) findViewById(R.id.imgProfile);
        firebaseAuth = FirebaseAuth.getInstance();
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                chooseImage();
            }
        });

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

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }




}