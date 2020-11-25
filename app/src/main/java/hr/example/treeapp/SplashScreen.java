package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //TODO: ovaj dio ide u poslovnu logiku bool userIsLogedIn i poziva getCurrentUser iz database (ako je getCurrentUser null onda se vraća false, ako ne onda je true), tu se
        firebaseAuth=FirebaseAuth.getInstance(); //dohvaca se instanca firebase-a

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Provjera postoji li ulogirani korisnik
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if(currentUser!=null){
                    //Ako postoji pokreće se naslovnica
                    startActivity(new Intent(getApplicationContext(), LoginTest.class));
                }
                else
                {
                    //ne postoji pokreće se login screen
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                finish();
            }
        }, 1500);
    }

}