package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RegistrationStep1 extends AppCompatActivity {
    EditText name, surname;
    DatePicker datumrodenja;
    ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST=71;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step1);
        imageView=(ImageView) findViewById(R.id.imgProfile);

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                chooseImage();
            }
        });

    //}

        }


    public void OpenRegistrationStep2(View view) {
        name=findViewById(R.id.txtBoxStep1Name);
        surname=findViewById(R.id.txtBoxStep1Surname);
        datumrodenja= findViewById(R.id.datePickerStep1);
        int day=datumrodenja.getDayOfMonth();
        int month=datumrodenja.getMonth()+1;
        int year=datumrodenja.getYear();


     //  date=findViewById(R.id.txtStep1Date);
     //   String pattern = "MM/dd/yyyy";
     //   DateFormat df = new SimpleDateFormat(pattern);
    //    String date1 = df.format(date);
        String Name=name.getText().toString();
        String Surname=surname.getText().toString();

        Intent open = new Intent(getApplicationContext(), RegistrationStep2.class);
        if (filePath != null && !filePath.equals(Uri.EMPTY)) {
            open.putExtra("name_key", Name);
            open.putExtra("surname_key", Surname);
            open.putExtra("image_key", filePath.toString());
            open.putExtra("day_key", day);
            open.putExtra("month_key", month);
            open.putExtra("year_key", year);

            Log.d("Dobro", "Ima slike");
        }
        else{
            open.putExtra("name_key", Name);
            open.putExtra("surname_key", Surname);
            Log.d("Lose", "Nema slike");
            open.putExtra("day_key", day);
            open.putExtra("month_key", month);
            open.putExtra("year_key", year);

        }

        if(TextUtils.isEmpty(Name)){
            name.setError(getString(R.string.no_name));
        }
        else if(TextUtils.isEmpty(Surname)){
            surname.setError(getString(R.string.no_surname));
        }
        else {
            startActivity(open);
        }
   //     open.putExtra("date_key", date1);



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
    //TODO: nisam siguran baš, ali dio ovog bi mogao u poslovnu logiku pa se tu samo provjerava je li sve uspješno napravljeno
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