package com.chen4393c.vicinity.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class AddressFetcher {

    private static final String TAG = "AddressFetcher";

    public String fetchAddress(Context context, double latitude, double longitude) {
        String result = "";
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList.size() == 1) {
                Address address = addressList.get(0);
                String addressLine = address.getAddressLine(0);
                if (!addressLine.isEmpty()) {
                    int end = addressLine.lastIndexOf(',');
                    if (end != -1) {
                        result = addressLine.substring(0, end);
                    }
                }
            }
        } catch (IOException e) {
            result = null;
        }
        return result;
    }
}
