package hr.example.treeapp;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class PostMapView extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        setMarkersOnMap();

    }

    private void setMarkersOnMap() {
        getPostData = new GetPostData();
        getPostData.getPostsForMap(new PostLocationcallback() {
            @Override
            public void onCallbackList(List<PostLocation> postLocationList) {
                if(postLocationList!=null){
                    for (PostLocation postLocation : postLocationList){
                        Marker markerOnMap = mMap.addMarker(setMarkerOptions(postLocation));
                        markerOnMap.setTag(postLocation.postId);
                    }
                }
            }
        });
        mMap.setOnMarkerClickListener(this);
    }

    private MarkerOptions setMarkerOptions(PostLocation postLocation) {
        final com.google.android.gms.maps.model.LatLng mapsLatLng =
                new com.google.android.gms.maps.model.LatLng(postLocation.latLng.latitude,
                        postLocation.latLng.longitude);
        return new MarkerOptions()
                .position(mapsLatLng)
                .title(postLocation.postId)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String postMarkerID = marker.getTag().toString();
        Intent singlePostView = new Intent(PostMapView.this, SinglePostViewActivity.class);
        singlePostView.putExtra("postId",postMarkerID);
        startActivity(singlePostView);

        return false;
    }
}