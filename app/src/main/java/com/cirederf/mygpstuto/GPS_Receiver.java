package com.cirederf.mygpstuto;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

//step 1 implement LocationListerner
public class GPS_Receiver implements LocationListener {

    //step 2 add Context and constructor
    Context context;
    private LocationManager locationManager;

    public GPS_Receiver(Context context) {
        this.context = context;
    }

    //step 4 create a method to get location
    public Location getLocation() {

        //step 5: adding requestoptions
        //with ContextCompat
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(context, "Permissions not granted ! Apply manually in app setting of your device", Toast.LENGTH_SHORT).show();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        //with ActivityCompat
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(context, "Permissions not granted !", Toast.LENGTH_SHORT).show();
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return null;
//        }


        //if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION))
        // step 4.1 get systemService via context
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //step 4.2 check if gps is enable
        boolean isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGpsEnable) {
            //step 4.5 create request to lacation
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 10, this);
                }
            if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 500, 10, this);
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 10, this);
            }
            //step 6:
            Location locationToReturn = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return locationToReturn;
            //step 5 add the request options ahead

        } else {
            //step 4.3
            Toast.makeText(context, "Please, activate GPS !", Toast.LENGTH_SHORT).show();
        }
        //Location getLocation() return...in other case
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
//step 3 implement listerner methods
}
