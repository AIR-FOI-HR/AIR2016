package hr.example.treeapp.addTree;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;

public class UserLocation implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public double latitude, longitude;
    private int counter=0;

    @SuppressLint("MissingPermission")
    public UserLocation(Context context){
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location.getAccuracy()<1000 && counter==0){
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            counter++;
        }
    }

    private LatLng getMyLatLng(){
        return new LatLng(latitude,longitude);
    }
    public com.google.android.gms.maps.model.LatLng getLatLng(){
        final LatLng latLng = getMyLatLng();

        final com.google.android.gms.maps.model.LatLng convertedUserLocation =
                new com.google.android.gms.maps.model.LatLng(latLng.latitude,
                        latLng.longitude);
        return convertedUserLocation;
    }
}
