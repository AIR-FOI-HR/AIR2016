package hr.example.treeapp;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.core.entities.Post;
import com.example.core.entities.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import managers.DataManager;

public class LeaderboardLocationMapview extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    Marker noviMarker;
    LatLng novaLat;
    Circle circle;
    SupportMapFragment mapFragment;
    View view;
    Button button;
    EditText editText;
    int radius=100000;
    double minLatitude;
    double maxLatitude;
    double minLongitude;
    double maxLongitude;
    private GetPostData getPostData = new GetPostData();
    private List<Post> posts = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private RecyclerView myRecyclerView;
    UserRepository userRepository = new UserRepository();
    List<User> leaderboardKorisnici= new ArrayList<>();
    List<User> leaderboardKorisnici2= new ArrayList<>();
    private int numberOfNewUsers = 0;
    private boolean userpostoji=false;
    private Context context=this;
    private static final int MY_REQUEST_CODE = 0xe110;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_location_mapview);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initClickEvent();

        button=findViewById(R.id.button_apply_leaderboard);
        editText=findViewById(R.id.radius_text);
        String temp=editText.getText().toString();
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(novaLat!=null){
                    minLatitude=novaLat.latitude-radius/111111;
                    maxLatitude=novaLat.latitude+radius/111111;
                    minLongitude=novaLat.longitude-radius/111111;
                    maxLongitude=novaLat.longitude+radius/111111;
                    getPostData.getPostsForLeaderboard(minLatitude, maxLatitude, minLongitude, maxLongitude, new GetPostsInLatLng() {
                        @Override
                        public void onCallbackPostsInLatLng(List<Post> postsInLatLng) {
                            if(postsInLatLng!=null){
                                posts=postsInLatLng;
                                getUsersForLocationLeaderboard(posts);
                            }
                            if(postsInLatLng.isEmpty()){
                                finish();
                            }
                        }
                    });

                }
            }
        });

    }

    public void initClickEvent() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                noviMarker=mMap.addMarker(new MarkerOptions().position(latLng).title("Centar kruga"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                novaLat=latLng;
                if(noviMarker!=null){
                    noviMarker.remove();
                }
                editText=findViewById(R.id.radius_text);
                String temp=editText.getText().toString();
                if(temp.matches("")){
                }
                else{
                    radius = Integer.valueOf(editText.getText().toString());
                }

                if(circle!=null){
                    circle.remove();

                }

                circle = mMap.addCircle(new CircleOptions()
                        .center(novaLat)
                        .radius(radius)
                        .strokeWidth(10)
                        .strokeColor(Color.GREEN)
                        .clickable(true));

                mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                    @Override
                    public void onCircleClick(Circle circle) {
                        // Flip the r, g and b components of the circle's stroke color.
                        int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                        circle.setStrokeColor(strokeColor);

                    }
                });
            }

        });
    }

    public  void getUsersForLocationLeaderboard(List<Post> posts) {
        numberOfNewUsers=0;
        leaderboardKorisnici.clear();
            for (Post p : posts) {
                userRepository.getUser(p.getKorisnik_ID(), new UserCallback() {
                    @Override
                    public void onCallback(User user) {
                        if (user != null) {
                            userpostoji = false;
                            numberOfNewUsers++;
                            for (User u : leaderboardKorisnici) {
                                if (u.getKorisnickoIme() == user.getKorisnickoIme()) {
                                    userpostoji = true;
                                }
                            }
                            if (!userpostoji) {
                                leaderboardKorisnici.add(user);
                            }
                            if (posts.size() == numberOfNewUsers) {
                                sortAndSendUsersForLocationLeaderboard(leaderboardKorisnici);
                            }
                        } else {
                            numberOfNewUsers++;
                            Log.d("dokument", "Nema dokumenta objave.");
                        }
                    }
                });
            };
        }

    public void sortAndSendUsersForLocationLeaderboard(List<User> users){
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return (int) (o2.getBodovi()-o1.getBodovi());
                }
            });
            Intent resultIntent = new Intent();
            resultIntent.putExtra("LIST", (Serializable) users);
            setResult(MY_REQUEST_CODE, resultIntent);
            finish();
        }

    }



