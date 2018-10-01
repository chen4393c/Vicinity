package com.chen4393c.vicinity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.chen4393c.vicinity.utils.AddressFetcher;
import com.chen4393c.vicinity.utils.LocationTracker;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class ControlPanelActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LocationTracker mLocationTracker;
    private AddressFetcher mAddressFetcher;

    private TextView mAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationTracker = new LocationTracker(this);
        mAddressFetcher = new AddressFetcher();

        setContentView(R.layout.activity_control_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

                // Respond when the drawer is opened
                if (Config.username == null) {
                    userNameTextView.setText(getResources().getText(R.string.nav_header_title));
                    mAddressTextView.setText(getResources().getText(R.string.nav_header_subtitle));
                } else {
                    if (Config.address == null) {
                        mLocationTracker.getLocation();
                        final double latitude = mLocationTracker.getLatitude();
                        final double longitude = mLocationTracker.getLongitude();
                        new FetchAddressTask(latitude, longitude).execute();
                    } else {
                        setupAddress();
                    }
                    userNameTextView.setText(Config.username);
                }
            }
        });
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private final int NUM_ITEMS = 2;
        private String tabTitles[] = new String[] { "Account", "Map" };

        public PagerAdapter(FragmentManager fragmentManager) {
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

    private class FetchAddressTask extends AsyncTask<Void, Void, String> {

        private double mLatitude;
        private double mLongitude;

        public FetchAddressTask(double latitude, double longitude) {
            mLatitude = latitude;
            mLongitude = longitude;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return mAddressFetcher.fetchAddress(mLatitude, mLongitude);
        }

        @Override
        protected void onPostExecute(String s) {
            Config.address = s;
            setupAddress();
        }
    }

    private void setupAddress() {
        mAddressTextView.setText(Config.address);
    }
}
