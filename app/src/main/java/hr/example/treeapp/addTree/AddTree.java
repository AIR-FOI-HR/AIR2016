package hr.example.treeapp.addTree;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import hr.example.treeapp.R;

public class AddTree extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    TextView treeDescription;

    ImageView imageView;
    Uri filePath;
    ImageButton imageLoad;
    ImageManipulation imageManipulation;
    Bitmap imageFinal;

    GoogleMap map;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tree2);
        imageLoad = (ImageButton) findViewById(R.id.imageLoadButton);
        treeDescription = (TextView)findViewById(R.id.treeDescriptionText);

        checkPermissions();
        mapInitialization();

        imageLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void mapInitialization() {
        imageView = (ImageView) findViewById(R.id.treeImageView);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //provjera je li korisnik dao dopuštenja, ako nije od korisnika se traži davanje dopuštenja
    private void checkPermissions() {
        //dopuštenje za uporabu kamere
        if (ContextCompat.checkSelfPermission(AddTree.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddTree.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        //dopuštenje za čitanje vanjske memorije
        if (ContextCompat.checkSelfPermission(AddTree.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddTree.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 100);
        }
        //dopuštenje za pisanje unutar vanjske memorije
        if (ContextCompat.checkSelfPermission(AddTree.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddTree.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }
        //dopuštenje za učitavanja korisnikove lokacije
        if (ContextCompat.checkSelfPermission(AddTree.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddTree.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
    }

    //prikaz izbornika za odabir načina uploada,
    private void selectImage() {
        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_photo_from_gallery), getString(R.string.cancel)};
        //otvaranje izbornika
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTree.this);
        builder.setTitle(R.string.add_photo);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //odabir opcija izbornika
                //pokreće se kamera
                if (options[item].equals(getString(R.string.take_photo))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                }
                //pokreće se odabir iz galerije
                else if (options[item].equals(getString(R.string.choose_photo_from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 200);
                }
                //zatvara se izbornik
                else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //resize i postavljanje slike fotoaparata
        if (requestCode == 100) {
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            setImageInView(capturedImage);
        }
        //resize i postavljanje slike iz galerije
        if (requestCode == 200 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                setImageInView(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //popunjavanje image view-a
    private void setImageInView(Bitmap bitmap) {
        imageLoad.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        Bitmap image = imageManipulation.getResizedBitmap(bitmap,800);
        imageFinal = image;
        imageView.setImageBitmap(image);
    }
    //image resize

    //klik na dicard
    public void discard(View view) {
        finish();
    }

    public void uploadImage() {
        LatLng treeLocation = getPinedLocation();
        Bitmap treeImage = imageFinal;
        String treeDesc = treeDescription.toString().trim();
        //uzmi Bitmap imageFinal i pozovi metodu za upload
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);

        final LatLng startLocation = new LatLng(44.579842, 16.722162);

        final com.google.android.gms.maps.model.LatLng mapsLatLng =
                new com.google.android.gms.maps.model.LatLng(startLocation.latitude,
                        startLocation.longitude);
        marker = map.addMarker(new MarkerOptions()
                .position(mapsLatLng)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));
        Float zoomLvl = (float)5.5;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapsLatLng,zoomLvl));
        map.getUiSettings().setMyLocationButtonEnabled(true);

    }

    private LatLng getPinedLocation (){
        LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        return latLng;
    }

}