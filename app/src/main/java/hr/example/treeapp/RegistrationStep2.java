package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrationStep2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step2);
    }
    public void OpenRegistrationStep3(View view) {
        Intent open = new Intent(RegistrationStep2.this, RegistrationStep3.class);
        startActivity(open);
    }
}