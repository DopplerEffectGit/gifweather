package com.myawesomegifweather;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.myawesomegifweather.fragments.OptionsFragment;
import com.myawesomegifweather.fragments.WeatherFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements LocationListener, WeatherFragment.OnFragmentInteractionListener,OptionsFragment.OnFragmentInteractionListener {

    private Presenter mPresenter;
    private LocationManager locationManager;
    private BottomNavigationView mBottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if(mBottomNavigationView.getSelectedItemId() == item.getItemId()) {
                return false;
            }

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_weather:
                    fragment = new WeatherFragment(mPresenter);
                    mPresenter.setWeatherFragment((WeatherFragment) fragment);
                    break;
                case R.id.navigation_options:
                    fragment = new OptionsFragment(mPresenter);
                    break;
            }

            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences preferences = getSharedPreferences("WEATHER_PREF", Context.MODE_PRIVATE);

        mPresenter = new Presenter(preferences);

        WeatherFragment weatherfragment = new WeatherFragment(mPresenter);
        loadFragment(weatherfragment);

        mPresenter.setWeatherFragment(weatherfragment);
    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);
            return;
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder geoCoder = new Geocoder(this, Locale.ENGLISH);

        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            mPresenter.setLocation(address.get(0).getLocality());
            mPresenter.refresh();
        } catch (IOException e) {

        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
