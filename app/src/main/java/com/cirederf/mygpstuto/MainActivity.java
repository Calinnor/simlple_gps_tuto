package com.cirederf.mygpstuto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_CODE_PERMISSIONS = 12340;
    private LocationManager locationManager;
    private Button buttonGetLocation;
    private TextView textLongitude;
    private TextView textLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGetLocation = findViewById(R.id.button_getting_location);
        textLatitude = findViewById(R.id.latitude);
        textLongitude = findViewById(R.id.longitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForLocationPermissions();

        //step 1 create a pop up permission allowed (line 42 to 53)
        // which will be placed in a dedied method below with all others informations (42 to 69)

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            //step 1.1 add the created pop up "permission allowed"
//            ActivityCompat.requestPermissions(
//                    this,
//                    new String[] {
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                    },
//                    REQUEST_CODE_PERMISSIONS
//            );
//            //step 2: get all informations in a dedied method :line 42 to 68 !
//            //go in private void checkForPermissions()
//            return;
//        }
//
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
//        }
//        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000, 10, this);
//        }
//        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, this);
//        }
    }

    //step 2 the dedied method to make the call in async
    private void checkForLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    REQUEST_CODE_PERMISSIONS
            );
            return;
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        }
        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000, 10, this);
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, this);
        }
        //step 3 go in onRequestPermissionsResult
    }

    //step 3 : override onRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //step 3.1 create a loop for seeking permissions allowed: if permission allowed: check, if not : popup
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            checkForLocationPermissions();
        }
        //End of the tuto for simple gps implementation.
        //note: if you use a emulator you have false gps location given with the system
        //note 2 : after update this app with those code lines
        // , you have to modify the allowed permissions in the app system in the device to test correctly the update...;)
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textLatitude.setText(String.valueOf(latitude));
                textLongitude.setText(String.valueOf(longitude));
            }
        });
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
}