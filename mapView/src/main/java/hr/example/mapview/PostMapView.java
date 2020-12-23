package hr.example.mapview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.core.DataPresenter;
import com.example.core.LiveData.LiveData;
import com.example.core.entities.Post;
import com.example.core.entities.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class PostMapView extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, DataPresenter {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private List<Post> posts;
    private boolean dataReady=false;
    private List<User> users;
    private boolean mapReady=false;
    private LiveData liveData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_map_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        liveData = new LiveData();
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        /**setContentView(R.layout.activity_post_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);**/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapReady=true;
        if(mapReady&& dataReady)
            setMarkersOnMap();

    }

    private void setMarkersOnMap() {
        for (Post currentPost: posts){
            Marker markerOnMap = mMap.addMarker(setMarkerOptions(currentPost));
            markerOnMap.setTag(currentPost.getID_objava());
        }
        mMap.setOnMarkerClickListener(this);
    }

    private MarkerOptions setMarkerOptions(Post post) {
        final com.google.android.gms.maps.model.LatLng mapsLatLng =
                new com.google.android.gms.maps.model.LatLng(post.getLatitude(),
                        post.getLongitude());
        return new MarkerOptions()
                .position(mapsLatLng)
                .title(post.getOpis())
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        liveData.UpdateSelectedPostId(marker.getTag().toString());
        return false;
    }

    @Override
    public void setData(List<Post> posts, List<User> users) {
        this.posts=posts;
        this.users=users;
        dataReady=true;
        if(dataReady && mapReady)
            setMarkersOnMap();

    }

    @Override
    public String getModuleName(Context context) {
        return context.getString(R.string.map_view_module_name);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}