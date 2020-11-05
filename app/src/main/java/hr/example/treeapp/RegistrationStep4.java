package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrationStep4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step4);
    }
    public void OpenLogIn(View view) {
        Intent open = new Intent(RegistrationStep4.this, MainActivity.class);
        startActivity(open);
    }
}