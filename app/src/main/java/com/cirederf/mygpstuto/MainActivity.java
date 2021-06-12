package com.cirederf.mygpstuto;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
        onClickButton();
    }

    private void onClickButton() {
        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPS_Receiver gps = new GPS_Receiver(getApplicationContext());
                Location location = gps.getLocation();
                if (location != null) {
                    double lat = location.getLatitude();
                    double longi = location.getLongitude();
                    textLatitude.setText(String.valueOf(lat));
                    textLongitude.setText(String.valueOf(longi));
                    Toast.makeText(getApplicationContext(), "Latitude ="+lat+" // Longitude ="+longi, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}