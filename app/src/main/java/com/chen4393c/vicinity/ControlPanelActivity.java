package com.chen4393c.vicinity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.chen4393c.vicinity.main.LoginFragment;
import com.chen4393c.vicinity.main.MapFragment;
import com.chen4393c.vicinity.settings.SettingsActivity;
import com.chen4393c.vicinity.utils.AddressFetcher;
import com.chen4393c.vicinity.utils.LocationTracker;
import com.chen4393c.vicinity.utils.QueryPreferences;
import com.chen4393c.vicinity.utils.UIUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ControlPanelActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ControlPanelActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private LocationTracker mLocationTracker;
    private FetchAddressTask mFetchAddressTask;

    private TabLayout mTabLayout;
    private TextView mAddressTextView;
    private PagerAdapter mPagerAdapter;
    private static final int[] tabIcons = {
            R.drawable.ic_tab_account,
            R.drawable.ic_tab_map,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Add listener to check sign in status
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // sign in anonymously
        mAuth.signInAnonymously().addOnCompleteListener(this,  new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInAnonymously", task.getException());
                }
            }
        });


        mLocationTracker = new LocationTracker(this);

        setContentView(R.layout.activity_control_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mPagerAdapter);
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                UIUtils.detectAndHideKeyboard(ControlPanelActivity.this);
            }

            @Override
            public void onPageSelected(int i) {
                UIUtils.detectAndHideKeyboard(ControlPanelActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // Give the TabLayout the ViewPager
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(pager);
        setupTabIcons();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                final TextView userNameTextView =
                        (TextView) drawerView.findViewById(R.id.user_name_nav_header);
                mAddressTextView =
                        (TextView) drawerView.findViewById(R.id.user_location_nav_header);

                UIUtils.detectAndHideKeyboard(ControlPanelActivity.this);

                // Respond when the drawer is opened
                if (Config.username == null) {
                    userNameTextView.setText(getResources().getText(R.string.nav_header_title));
                    mAddressTextView.setText(getResources().getText(R.string.nav_header_subtitle));
                } else {
                    if (Config.address == null) {
                        mLocationTracker.getLocation();
                        final double latitude = mLocationTracker.getLatitude();
                        final double longitude = mLocationTracker.getLongitude();
                        if (mFetchAddressTask == null) {
                            mFetchAddressTask = new FetchAddressTask(
                                    getApplicationContext(), latitude, longitude);
                        }
                        mFetchAddressTask.execute();
                    } else {
                        setupAddress();
                    }

                    String displayName = QueryPreferences
                            .getDisplayName(ControlPanelActivity.this);
                    if (displayName != null) {
                        userNameTextView.setText(displayName);
                    } else {
                        userNameTextView.setText(Config.username);
                    }
                }
            }
        });
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //Add authentication listener when activity starts
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //Remove authentication listener when activity starts
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.control_panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();

        if (id == R.id.nav_settings) {
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private final int NUM_ITEMS = 2;
        private String tabTitles[] = new String[] { "Account", "Map" };

        PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LoginFragment.newInstance();
                case 1:
                    return MapFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

    private void setupTabIcons() {
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            mTabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }

    private class FetchAddressTask extends AsyncTask<Void, Void, String> {

        private double mLatitude;
        private double mLongitude;
        private Context mContext;

        FetchAddressTask(Context context, double latitude, double longitude) {
            mContext = context;
            mLatitude = latitude;
            mLongitude = longitude;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return new AddressFetcher().fetchAddress(mContext, mLatitude, mLongitude);
        }

        @Override
        protected void onPostExecute(String s) {
            Config.address = s;
            setupAddress();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mFetchAddressTask = null;
        }
    }

    private void setupAddress() {
        if (Config.address != null) {
            mAddressTextView.setText(Config.address);
        } else {
            mAddressTextView.setText(getResources().getText(R.string.location_error_subtitle));
        }
    }
}
