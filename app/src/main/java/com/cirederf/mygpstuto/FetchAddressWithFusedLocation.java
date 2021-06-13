package com.cirederf.mygpstuto;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

//step 2 extends
public class FetchAddressWithFusedLocation extends IntentService {

    //step 6 attribu for receiver
    private ResultReceiver resultReceiver;
    //step 7 create a class for constant

    //step 3 constructor
    public FetchAddressWithFusedLocation() {
        super("FetchAddressWithFusedLocation");
    }
//step 4 implement onHandle
    //step 5 in manifest
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //step 8 implement method
        if (intent != null) {
            String error = "";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRAS);
            if (location == null) {
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (Exception e) {
                error = e.getMessage();
            }
            if (addresses == null || addresses.isEmpty()) {
                deliverResultToReceiver(Constants.FAILURE_RESULT, error);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                deliverResultToReceiver(
                        Constants.SUCCES_RESULT,
                        TextUtils.join(
                                Objects.requireNonNull(System.getProperty("line.separator")),
                                addressFragments
                        )
                        //step 9 in main
                );
            }
        }
    }

    //step 8: create a method to get result and send it to receiver
    private void deliverResultToReceiver(int resultCode, String address) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, address);
        resultReceiver.send(resultCode, bundle);

    }
}
