package hr.example.treeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import managers.DataPresentersManager;

import com.example.timeline.PostListFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginTest extends AppCompatActivity {
    DataPresentersManager dataPresentersManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
        context=this;
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        dataPresentersManager=new DataPresentersManager(context);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dataPresentersManager.currentPresenter.getFragment()).commit();
        FillTopMenu();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setItemIconTintList(null);
        //displayMainFragment();
    }

    private void FillTopMenu() {
        HorizontalScrollView horizontalScrollView = findViewById(R.id.topmenu);
        LinearLayout mainLinearLayout =findViewById(R.id.topmenumainlayout);
        for (int i=0;i<dataPresentersManager.presenters.size() ;i++){
            LinearLayout newLayout = new LinearLayout(this);
            mainLinearLayout.addView(newLayout);
            Button button=new Button(this);
            button.setText(dataPresentersManager.presenters.get(i).getModuleName(this));
            int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dataPresentersManager.presenters.get(finalI).getFragment()).commit();
                }
            });
            newLayout.addView(button);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment =null;

                    switch(item.getItemId()){
                        case R.id.nav_home:
                                selectedFragment=dataPresentersManager.currentPresenter.getFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            break;
                        case R.id.nav_leaderboard:
                            break;
                        case R.id.nav_addtree:
                            break;
                        case R.id.nav_search:
                            break;
                        case R.id.nav_profile:
                            break;
                    }
                    return true;
                }
            };

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