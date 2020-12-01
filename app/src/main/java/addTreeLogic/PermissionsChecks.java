package addTreeLogic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class PermissionsChecks {

    private Activity activity;

    public PermissionsChecks(Activity activity){
        this.activity=activity;
    }
    public boolean checkLocationPermissions(){
        if((ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED))
            return true;
        else
            return false;
    }

    public boolean checkCameraAndStoragePermissions(){
        if(ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    public boolean checkAllPermissions(){
        if(checkCameraAndStoragePermissions() && checkLocationPermissions())
            return true;
        else
            return false;
        /**
         * ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)
         *                 != PackageManager.PERMISSION_GRANTED &&
         *                 ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
         *                         != PackageManager.PERMISSION_GRANTED &&
         *                 ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
         *                         != PackageManager.PERMISSION_GRANTED &&
         *                 ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
         *                         != PackageManager.PERMISSION_GRANTED &&
         *                 ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
         *                         != PackageManager.PERMISSION_GRANTED
         */
    }


    public boolean checkAnyCameraAndStoragePermission (){
        if(ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
            return false;
        else return true;
    }

    public boolean checkBuildVersion(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return true;
        else
            return false;
    }

    public boolean checkAnyImportantPermission (){
        if (ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
            return false;
        else
            return true;
    }
}
