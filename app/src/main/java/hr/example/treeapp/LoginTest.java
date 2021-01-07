package hr.example.treeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.lifecycle.Observer;
import hr.example.mapview.PostMapView;
import managers.DataManager;
import managers.DataPresentersManager;

import com.example.core.LiveData.LiveData;
import com.example.core.VisibleMapRange;
import com.example.timeline.PostListFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginTest extends AppCompatActivity {
    DataPresentersManager dataPresentersManager;
    Context context;
    private LiveData model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
        context=this;
        DataManager dataManager = DataManager.getInstance();

        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(String lastPostID) {
                    dataManager.GetPostsFromLastID();
            }
        };

        model.lastPostID().observe(this, nameObserver);

        final Observer<String> postIdObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Intent singlePostView = new Intent(LoginTest.this, SinglePostViewActivity.class);
                singlePostView.putExtra("postId",s);
                startActivity(singlePostView);
            }
        };
        model.selectedPostId().observe(this, postIdObserver);

        final Observer<VisibleMapRange> visibleMapRangeObserver = new Observer<VisibleMapRange>() {
            @Override
            public void onChanged(VisibleMapRange mapMinMaxLatLng) {
                dataManager.getPostsInLatLng(mapMinMaxLatLng.minLatitude, mapMinMaxLatLng.maxLatitude, mapMinMaxLatLng.minLongitude, mapMinMaxLatLng.maxLongitude);
            }
        };
        model.visibleMapRange().observe(this, visibleMapRangeObserver);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        dataPresentersManager=new DataPresentersManager(context);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dataPresentersManager.firstPresenter.getFragment()).commit();
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
            button.setBackgroundColor(getResources().getColor(R.color.baby_green));
            Typeface typeface = ResourcesCompat.getFont(this,R.font.roboto);
            button.setTextColor(getResources().getColor(R.color.tree_green));
            button.setTypeface(typeface);
            button.setPadding(32,0,0,0);



            int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dataPresentersManager.presenters.get(finalI).getFragment()).commit();
                    dataPresentersManager.loadFragment(finalI);
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
                                selectedFragment=dataPresentersManager.firstPresenter.getFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            break;
                        case R.id.nav_leaderboard:
                            break;
                        case R.id.nav_addtree:
                            Intent open = new Intent(LoginTest.this, AddTree.class);
                            startActivity(open);
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