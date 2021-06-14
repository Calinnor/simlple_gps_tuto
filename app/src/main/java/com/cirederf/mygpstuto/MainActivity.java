package com.cirederf.mygpstuto;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //map implementatiion: step 1 in gradle
    //step 2 modify xml
    //step 3 modify attributs and on clicklisterner
    private static final int REQUEST_CODE_PERMISSIONS = 12340;
    private LocationManager locationManager;
    private MapFragment mapFragment;
    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //step 4 initiate fragment and cast to MapFragment
        FragmentManager fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.id_for_google_map_fragment);
        //step 5 create a method to getGoogleMap
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForLocationPermissions();
    }

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
        getMyGoogleMap();
        //step 7 modify onLocationChanged
    }

    //step 5 :method to call google map in async
    //step 5.3 add suppress warning
    @SuppressWarnings("MissingPermission")
    private void getMyGoogleMap() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //5.1 implement support_view (mainactivity) for map
                MainActivity.this.googleMap = googleMap;
                googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));
                //step 5.2 not needing new permission check because this method will be call just after the permissions call already done
                googleMap.setMyLocationEnabled(true);
                googleMap.addMarker(new MarkerOptions().position(new LatLng(48.5277772, 2.6580556))
                        .title("apm"));
                //step 6 after checkpermissions call method

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            checkForLocationPermissions();
        }
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

        Toast.makeText(this, "Location "+latitude +"/"+ longitude, Toast.LENGTH_LONG).show();
        //step 7.1 if map is loaded (not null)
        if (googleMap != null) {
            LatLng googleLocation = new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
            //step 8 in manifest add apiKey
        }

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