package com.chen4393c.vicinity.main;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen4393c.vicinity.Constant;
import com.chen4393c.vicinity.R;
import com.chen4393c.vicinity.settings.SettingsActivity;
import com.chen4393c.vicinity.utils.LocationTracker;
import com.chen4393c.vicinity.utils.QueryPreferences;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";

    private LocationTracker mLocationTracker;

    private View mParentView;
    private MapView mMapView;
    private GoogleMap mMap;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mParentView = inflater.inflate(R.layout.fragment_map, container, false);
        return mParentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mParentView.findViewById(R.id.event_map_view);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();// needed to get the map to display immediately
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");
        mMapView.onResume();
        setTheme();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady(GoogleMap)");
        Context context = getContext();
        if (context == null) {
            Log.e(TAG, "getContext() == null");
            return;
        }
        MapsInitializer.initialize(context);

        mMap = googleMap;
        setTheme();

        mLocationTracker = new LocationTracker(getActivity());
        mLocationTracker.getLocation();

        double lat = mLocationTracker.getLatitude();
        double lon = mLocationTracker.getLongitude();
        Log.i(TAG, "lat, lon: " + lat + ", " + lon);
        LatLng point = new LatLng(lat, lon);

        MarkerOptions marker = new MarkerOptions()
                .position(point)
                .title(getResources().getString(R.string.map_marker_title))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        googleMap.addMarker(marker);

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(point).zoom(12).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setTheme() {
        if (mMap == null) {
            return;
        }
        // Load theme index from shared preferences
        int themeIndex = QueryPreferences.getThemeIndex(getActivity());
        Context context = getContext();
        if (context == null) {
            Log.e(TAG, "getContext() == null");
            return;
        }
        mMap.setMapStyle(MapStyleOptions
                .loadRawResourceStyle(context, Constant.mapThemes[themeIndex]));
    }
}
