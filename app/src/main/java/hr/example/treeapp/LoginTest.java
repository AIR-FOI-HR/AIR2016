package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import fragments.PostListFragment;

import android.content.Context;
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

        displayMainFragment();
    }

    private void displayMainFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new PostListFragment());
        ft.commit();
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void addTree (View view){
        /*Intent open = new Intent(LoginTest.this, AddTree.class);
            startActivity(open);*/
    }


    public void mapView (View view){
        Intent openMapview = new Intent(
                LoginTest.this,
                PostMapView.class
        );
        startActivity(openMapview);
    }
}