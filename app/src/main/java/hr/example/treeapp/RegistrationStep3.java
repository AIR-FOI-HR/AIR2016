package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import auth.RegistrationRepository;


public class RegistrationStep3 extends AppCompatActivity {
    private static final String TAG = "";
    String email;
    RegistrationRepository registrationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step3);
        registrationRepository=new RegistrationRepository(this);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        IspisEmaila();
    }

    public void IspisEmaila() {
        TextView lblEmail = findViewById(R.id.lblStep3Email);
        lblEmail.setText(email);
    }

    public void OpenRegistrationStep1(View view) {
        Intent open = new Intent(RegistrationStep3.this, RegistrationStep1.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft, R.anim.stayinplace);
    }

    public void OpenRegistrationStep2(View view) {
        Intent open = new Intent(RegistrationStep3.this, RegistrationStep2.class);
        startActivity(open);
    }

    public void OpenRegistrationStep4(View view) {

    }

    public void OpenLogIn(View view) {
        Intent open = new Intent(RegistrationStep3.this, LoginActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft, R.anim.stayinplace);
    }
}



