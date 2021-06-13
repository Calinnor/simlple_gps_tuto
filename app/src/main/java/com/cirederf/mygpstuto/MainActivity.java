package com.cirederf.mygpstuto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

//step 1 create xml, declare and initiate xml attributs
//step 2 in manifest
public class MainActivity extends AppCompatActivity {

    //step 3 other attributs
    private static final int REQUEST_CODE_LOCATION_PERMISSIONS = 12340;

    private Button buttonGetLocation;
    private TextView textCurrentLocation, textAddress;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textCurrentLocation = findViewById(R.id.current_location);
        textAddress = findViewById(R.id.textAdress);
        buttonGetLocation = findViewById(R.id.button_get_current_location);
        progressBar = findViewById(R.id.progressBar);
        onButtonLocationClick();
    }

    //step 5 listener onClick with method calling location
    private void onButtonLocationClick() {
        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getCurrentLocation();
            }
        });
    }

    //step 7 onRequestResume
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSIONS && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Need permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

    //step 6 initiate the method to geLocation
    private void getCurrentLocation() {
        //step 8 implement method
        //8.1 progressbar
        progressBar.setVisibility(View.VISIBLE);

        //8.2 set speed to refresh data
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext()
                    , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        MainActivity.this
                        , new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }
                        , REQUEST_CODE_LOCATION_PERMISSIONS
                );
            }
            return;
        }

        //8.3 implement fusedLocation
        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        //unsuscribe
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);

                        //if there's data in locationResult
                        if(locationResult != null && locationResult.getLocations().size() > 0) {

                            int latestLocationIndex = locationResult.getLocations().size() -1;
                            
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            textCurrentLocation.setText(
                                    String.format(
                                            "Latitude: %s\nLongitude: %s ",
                                            latitude, longitude
                                    )
                            );
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }, Looper.getMainLooper());


    }

}