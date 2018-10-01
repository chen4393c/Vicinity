package com.chen4393c.vicinity.utils;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressFetcher {

    private static final String TAG = "AddressFetcher";

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String GEO_CODING_API_KEY = "AIzaSyBj5cz4B09ni_DrADdlB3orqbz7YZ1hUBM";

    public String fetchAddress(double lat, double lon) {
        NetworkClient client = new NetworkClient();
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter("latlng", lat + "," + lon)
                .appendQueryParameter("key", GEO_CODING_API_KEY)
                .build().toString();
        String jsonString = client.getJSONResponse(url, null, "GET");
        Log.i(TAG, "Received JSON: " + jsonString);
        String result = null;
        try {
            JSONObject jsonResponse = new JSONObject(jsonString);
            result = parseJSON(jsonResponse);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON", e);
        }

        return result;
    }

    private String parseJSON(JSONObject jsonObject) throws JSONException {
        String address = null;
        String status = jsonObject.getString("status");
        Log.d(TAG, "status: " + status);
        if (status.equalsIgnoreCase("OK")) {
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                if (result.has("formatted_address")) {
                    String formattedAddress = result.getString("formatted_address");
                    if (!formattedAddress.isEmpty()) {
                        int end = formattedAddress.lastIndexOf(',');
                        if (end != -1) {
                            address = formattedAddress.substring(0, end);
                        }
                    }
                    break;
                }
            }
        }
        return address;
    }
}
