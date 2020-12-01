package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginTest extends AppCompatActivity {
    TextView userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
        userID=(TextView)findViewById(R.id.userid);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        userID.setText(user.getUid());
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void addTree (View view){
        Intent open = new Intent(LoginTest.this, AddTree.class);
        startActivity(open);
    }
}