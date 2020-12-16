package hr.example.treeapp;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class PostMapView extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GetPostData getPostData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    List<PostLocation> postList= new ArrayList<>();
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        getPostData = new GetPostData();
        getPostData.getPostsForMap(new PostLocationcallback() {
            @Override
            public void onCallbackList(List<PostLocation> postLocationList) {
                if(postLocationList!=null){
                    for (PostLocation postLocation : postLocationList){
                        final com.google.android.gms.maps.model.LatLng mapsLatLng =
                                new com.google.android.gms.maps.model.LatLng(postLocation.latLng.latitude,
                                        postLocation.latLng.longitude);
                        MarkerOptions marker = new MarkerOptions()
                                .position(mapsLatLng)
                                .title(postLocation.postId);
                        mMap.addMarker(marker);
                    }
                }
            }
        });

    }
}