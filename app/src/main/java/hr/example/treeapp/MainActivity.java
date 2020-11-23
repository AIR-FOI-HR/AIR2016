package hr.example.treeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

import auth.AuthRepository;
import auth.LogInStatusCallback;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    AuthRepository authRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authRepository= new AuthRepository(this);
        authRepository.createRequest();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void signIn(View view) {
        Intent signInIntent = authRepository.getSignInIntent();
        startActivityForResult(signInIntent, authRepository.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            authRepository.requestCheck(requestCode,data);
        }
        catch (ApiException e){
            Toast.makeText(this, "Error:  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    public void login(View view) throws InterruptedException {
        email=(EditText)findViewById(R.id.txtEmail3);
        password=(EditText)findViewById(R.id.txtPassword3);

        String emailVal=email.getText().toString().trim();
        String passwordVal=password.getText().toString();

        if(inputValidation(emailVal,passwordVal)) {
            authRepository.login(emailVal, passwordVal, new LogInStatusCallback() {
                @Override
                public void onCallback(String value) {
                    if (value == "ok") {
                        startActivity(new Intent(getApplicationContext(), LoginTest.class));
                        finish();
                    } else if (value == "notVerified") {
                        email.setError(getString(R.string.email_unconfirmed));
                        password.getText().clear();
                    } else {
                        password.getText().clear();
                        password.setError(getText(R.string.invalid_password_login));

                    }
                }
            });

        }
    }


    public void OpenRegistration(View view) {
        Intent open = new Intent(MainActivity.this, RegistrationStep1.class);
        startActivity(open);
    }

    public void OpenReset(View view){
        Intent open= new Intent(MainActivity.this,PassReset.class);
        startActivity(open);
    }


    private boolean inputValidation(String emailVal, String passwordVal){
        if(TextUtils.isEmpty(emailVal)){
            email.setError(getString(R.string.no_email));
        }
        if(TextUtils.isEmpty(passwordVal)){
            password.setError(getString(R.string.no_password));
        }
        return !TextUtils.isEmpty(emailVal) && !TextUtils.isEmpty(passwordVal);
    }


    public void guest(View view) {
        authRepository.guest();
    }
}