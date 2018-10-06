package com.chen4393c.vicinity.main;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ViewSwitcher;

import com.chen4393c.vicinity.Constant;
import com.chen4393c.vicinity.R;
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

    private View mParentView;
    private MapView mMapView;
    private GoogleMap mMap;
    FloatingActionButton mReportFAB;
    private Dialog mDialog;
    private ViewSwitcher mViewSwitcher;

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
        FloatingActionButton resetFAB = mParentView.findViewById(R.id.fab_reset_focus);
        mReportFAB = mParentView.findViewById(R.id.fab_report);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();// needed to get the map to display immediately
            mMapView.getMapAsync(this);
        }

        resetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.getMapAsync(MapFragment.this);
            }
        });

        mReportFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getContext());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume()");
        mMapView.onResume();
        setMapTheme(mMap, getContext());
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
        setMapTheme(googleMap, context);

        LocationTracker locationTracker = new LocationTracker(getActivity());
        locationTracker.getLocation();

        double lat = locationTracker.getLatitude();
        double lon = locationTracker.getLongitude();
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

    private void setMapTheme(GoogleMap googleMap, Context context) {
        if (googleMap == null) {
            return;
        }
        // Load theme index from shared preferences
        int themeIndex = QueryPreferences.getThemeIndex(getActivity());
        googleMap.setMapStyle(MapStyleOptions
                .loadRawResourceStyle(context, Constant.mapThemes[themeIndex]));
    }

    private void showDialog(Context context) {
        final View dialogView = View.inflate(getActivity(), R.layout.dialog, null);
        mViewSwitcher = (ViewSwitcher) dialogView.findViewById(R.id.view_switcher);
        mDialog = new Dialog(getActivity(), R.style.AppTheme);
        mDialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);

        Toolbar toolbar = dialogView.findViewById(R.id.toolbar_dialog);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateDialog(dialogView, false, mDialog);
            }
        });

        mDialog.setContentView(dialogView);

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                animateDialog(dialogView, true, null);
            }
        });

        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    animateDialog(dialogView, false, mDialog);
                    return true;
                }
                return false;
            }
        });

        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    // Add animation to Floating Action Button
    private void animateDialog(View dialogView, boolean open, final Dialog dialog) {
        final View view = dialogView.findViewById(R.id.dialog);
        int w = view.getWidth();
        int h = view.getHeight();

        float endRadius = (float) Math.hypot(w, h);

        int cx = w - mReportFAB.getWidth() / 2 - Constant.FAB_MARGIN;
        int cy = h - mReportFAB.getHeight() / 2 - Constant.FAB_MARGIN;

        if (open) {
            Animator anim = ViewAnimationUtils
                    .createCircularReveal(view, cx, cy, 0, endRadius);
            anim.setDuration(500);
            anim.start();
        } else {
            Animator anim = ViewAnimationUtils
                    .createCircularReveal(view, cx, cy, endRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(500);
            anim.start();
        }
    }
}
