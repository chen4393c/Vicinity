package com.chen4393c.vicinity.main;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.chen4393c.vicinity.Config;
import com.chen4393c.vicinity.Constant;
import com.chen4393c.vicinity.ControlPanelActivity;
import com.chen4393c.vicinity.R;
import com.chen4393c.vicinity.main.report.ReportRecyclerViewAdapter;
import com.chen4393c.vicinity.model.Item;
import com.chen4393c.vicinity.model.TrafficEvent;
import com.chen4393c.vicinity.utils.LocationTracker;
import com.chen4393c.vicinity.utils.QueryPreferences;
import com.chen4393c.vicinity.utils.UIUtils;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";

    private LocationTracker mLocationTracker;

    private View mParentView;
    private MapView mMapView;
    private GoogleMap mMap;
    FloatingActionButton mReportFAB;
    private Dialog mDialog;
    private RecyclerView mRecyclerView;
    private ReportRecyclerViewAdapter mAdapter;

    private ViewSwitcher mViewSwitcher;

    // event detail
    private String mCurrentEventType;
    private ImageView mImageCamera;
    private Button mBackButton;
    private Button mSendButton;
    private EditText mCommentEditText;
    private ImageView mEventTypeImage;
    private TextView mTypeTextView;

    private DatabaseReference mDatabaseReference;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mParentView = inflater.inflate(R.layout.fragment_map, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
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

        if (mLocationTracker == null) {
            mLocationTracker = new LocationTracker(getActivity());
        }
        mLocationTracker.getLocation();

        double lat = mLocationTracker.getLatitude();
        double lon = mLocationTracker.getLongitude();
        Log.i(TAG, "lat, lon: " + lat + ", " + lon);
        LatLng point = new LatLng(lat, lon);

        // clear all previous markers first, then we can add marker
        googleMap.clear();
        MarkerOptions marker = new MarkerOptions()
                .position(point)
                .title(getResources().getString(R.string.map_marker_title))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        googleMap.addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(point)      // Sets the center of the map to Mountain View
                .zoom(16)// Sets the zoom
                .bearing(90)           // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

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
        final View dialogView = View.inflate(context, R.layout.dialog, null);
        mViewSwitcher = (ViewSwitcher) dialogView.findViewById(R.id.view_switcher);
        mDialog = new Dialog(context, R.style.AppTheme);
        mDialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);

        Toolbar toolbar = dialogView.findViewById(R.id.toolbar_dialog);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = MapFragment.this.getActivity();
                if (activity != null) {
                    UIUtils.hideSoftKeyboard(mCommentEditText, activity); // working
                }
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

        Animation slideIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        Animation slideOut = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        mViewSwitcher.setInAnimation(slideIn);
        mViewSwitcher.setOutAnimation(slideOut);

        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialog.show();
            setupRecyclerView(dialogView, getContext());
            setUpEventDetails(dialogView);
        }
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

    private void setupRecyclerView(View dialogView, final Context context) {
        mRecyclerView = dialogView.findViewById(R.id.report_event_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        List<Item> items = new ArrayList<>();
        String[] labels = getResources().getStringArray(R.array.report_event_master_item_names);
        for (int i = 0; i < labels.length; i++) {
            items.add(new Item(labels[i], Constant.reportEventDrawableIds[i]));
        }
        mAdapter = new ReportRecyclerViewAdapter(getActivity(), items);
        mAdapter.setOnClickListener(new ReportRecyclerViewAdapter.EventOnClickListener() {
            @Override
            public void setItem(Item item) {
                mCurrentEventType = item.getDrawableLabel();
                if (mViewSwitcher != null) {
                    mViewSwitcher.showNext();
                    mTypeTextView.setText(item.getDrawableLabel());
                    mEventTypeImage.setImageDrawable(context.getDrawable(item.getDrawableId()));
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setUpEventDetails(final View dialogView) {
        mImageCamera = (ImageView) dialogView.findViewById(R.id.event_camera_image);
        mBackButton = (Button) dialogView.findViewById(R.id.event_back_button);
        mSendButton = (Button) dialogView.findViewById(R.id.event_send_button);
        mCommentEditText = (EditText) dialogView.findViewById(R.id.event_comment);
        mEventTypeImage = (ImageView) dialogView.findViewById(R.id.event_image);
        mTypeTextView = (TextView) dialogView.findViewById(R.id.event_type);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = MapFragment.this.getActivity();
                if (activity != null) {
                    UIUtils.hideSoftKeyboard(mCommentEditText, activity); // working
                }
                mViewSwitcher.showPrevious();
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadEvent(Config.username)) {
                    Activity activity = MapFragment.this.getActivity();
                    if (activity != null) {
                        UIUtils.hideSoftKeyboard(mCommentEditText, activity); // working
                    }
                    mViewSwitcher.showPrevious();
                }
            }
        });
    }

    // Upload event
    private boolean uploadEvent(String userId) {
        if (userId == null) {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.login_hint_toast),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String description = mCommentEditText.getText().toString();
        if (description.isEmpty()) {
            Toast.makeText(getContext(),
                    getResources().getString(R.string.upload_event_empty_content_toast),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String key = mDatabaseReference.child("events").push().getKey();
        if (key == null) {
            Toast.makeText(getContext(),
                    getResources().getText(R.string.upload_event_failed_toast),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        TrafficEvent event = new TrafficEvent();
        // better use builder pattern
        event.setEventType(mCurrentEventType);
        event.setEventDescription(description);
        event.setEventReporterId(userId);
        event.setEventTimestamp(System.currentTimeMillis());
        event.setEventLatitude(mLocationTracker.getLatitude());
        event.setEventLongitude(mLocationTracker.getLongitude());
        event.setEventLikeNumber(0);
        event.setEventCommentNumber(0);
        event.setId(key);

        mDatabaseReference.child("events").child(key).setValue(event, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getContext(),
                            getResources().getText(R.string.upload_event_failed_toast),
                            Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                } else {
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.upload_success_toast),
                            Toast.LENGTH_SHORT).show();
                    // TODO: update map fragment
                }
            }
        });

        return true;
    }
}
