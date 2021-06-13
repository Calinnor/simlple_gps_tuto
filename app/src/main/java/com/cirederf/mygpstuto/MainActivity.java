package com.cirederf.mygpstuto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSIONS = 12340;

    private Button buttonGetLocation;
    private TextView textCurrentLocation, textAddress;
    private ProgressBar progressBar;

    //step 10
    private ResultReceiver resultReceiver;
    //step 11 initiate resultReceiver (here in main)

    //step 1 create a class for fetch address
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //step 11
        resultReceiver = new AdressesResultReceiver(new Handler());
        //step 12 create a method to fetchAdress

        textCurrentLocation = findViewById(R.id.current_location);
        textAddress = findViewById(R.id.textAdress);
        buttonGetLocation = findViewById(R.id.button_get_current_location);
        progressBar = findViewById(R.id.progressBar);
        onButtonLocationClick();
    }

    //step 12
    private void fetchAddressFromCoordinates(Location location) {
        Intent intent = new Intent(this, FetchAddressWithFusedLocation.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRAS, location);
        startService(intent);
    }//step 13 in getCurrentLocation()

    private void onButtonLocationClick() {
        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getCurrentLocation();
            }
        });
    }

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

    private void getCurrentLocation() {
        progressBar.setVisibility(View.VISIBLE);

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

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                       LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);

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
                            //step 14
                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAddressFromCoordinates(location);
                          //step 13 placing here progressBar
                        }else {
                            progressBar.setVisibility(View.GONE);
                        }
                        //in step 13 progressBar move just ahead
//                        progressBar.setVisibility(View.GONE);

                    }
                }
                , Looper.getMainLooper());
    }

    //step 9 create class which receive results for addresses
    private class AdressesResultReceiver extends ResultReceiver {

        //step 9.1 constructor without public
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        AdressesResultReceiver(Handler handler) {
            super(handler);
        }

        //step 9.2 onReceiverResult
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCES_RESULT) {
                textAddress.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            }else {
                Toast.makeText(MainActivity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
            //step 10 declaring ResultReceiver atribut
        }
    }
}