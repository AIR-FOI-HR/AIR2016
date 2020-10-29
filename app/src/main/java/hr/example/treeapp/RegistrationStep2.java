package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.protobuf.DescriptorProtos;

import org.w3c.dom.Text;

public class RegistrationStep2 extends AppCompatActivity {

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

    }
    public void OpenRegistrationStep1(View view) {
        Intent open = new Intent(RegistrationStep2.this, RegistrationStep1.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft,R.anim.stayinplace);
    }
    public void OpenRegistrationStep3(View view) {
        Intent open = new Intent(RegistrationStep2.this, RegistrationStep3.class);
        startActivity(open);
    }
    public void OpenLogIn(View view){
        Intent open = new Intent(RegistrationStep2.this, MainActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft,R.anim.stayinplace);
    }
}