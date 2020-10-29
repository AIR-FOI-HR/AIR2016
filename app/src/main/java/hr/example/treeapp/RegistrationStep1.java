package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrationStep1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step1);
    }

    public void OpenRegistrationStep2(View view) {
        Intent open = new Intent(RegistrationStep1.this, RegistrationStep2.class);
        startActivity(open);
    }
    public void OpenLogIn(View view){
        Intent open = new Intent(RegistrationStep1.this, MainActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft,R.anim.stayinplace);
    }
}