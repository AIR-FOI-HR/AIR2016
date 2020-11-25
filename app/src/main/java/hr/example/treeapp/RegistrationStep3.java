package hr.example.treeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class RegistrationStep3 extends AppCompatActivity {
    private static final String TAG = "";
    String email;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_step3);
        Intent intent = getIntent();
        IspisEmaila();
        sendEmailVerificationWithContinueUrl();
    }

    public void IspisEmaila() {
        TextView lblEmail = findViewById(R.id.lblStep3Email);
        lblEmail.setText(user.getEmail());
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
        user.reload();
        if(user.isEmailVerified()==true) {
            Intent open = new Intent(RegistrationStep3.this, RegistrationStep4.class);
            startActivity(open);
        }
    }

    public void OpenLogIn(View view) {
        Intent open = new Intent(RegistrationStep3.this, MainActivity.class);
        startActivity(open);
        overridePendingTransition(R.anim.slideleft, R.anim.stayinplace);
    }
    //TODO: ovaj dio bi se trebao prebaciti u sloj poslovne logike i pozvati se u ovom koraku
    public void sendEmailVerificationWithContinueUrl() {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
        auth.useAppLanguage();
    }
}



