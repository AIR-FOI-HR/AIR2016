package hr.example.treeapp;

import androidx.fragment.app.FragmentActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class LeaderboardLocationMapview extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker noviMarker;
    LatLng novaLat;
    Circle circle;
    SupportMapFragment mapFragment;
    View view;
    Button button;
    EditText editText;
    int radius=100000;

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
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(novaLat!=null){

                }
                finish();
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



}