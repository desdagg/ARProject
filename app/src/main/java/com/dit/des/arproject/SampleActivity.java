package com.dit.des.arproject;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;

import java.io.IOException;

/**
 * Created by Des on 07/02/2018.
 */

public class SampleActivity extends Activity {
    private ArchitectView architectView;
    private LocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_cam);

        architectView = (ArchitectView)this.findViewById( R.id.architectView );
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setFeatures(ArchitectStartupConfiguration.Features.Geo);
        config.setLicenseKey( "YOUR-LICENCE-KEY" );

        architectView.onCreate( config );

        locationProvider = new LocationProvider(this, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location!=null && SampleActivity.this.architectView != null ) {
                    // check if location has altitude at certain accuracy level & call right architect method (the one with altitude information)
                    if ( location.hasAltitude() && location.hasAccuracy() && location.getAccuracy()<7) {
                        SampleActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy() );
                    } else {
                        SampleActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.hasAccuracy() ? location.getAccuracy() : 1000 );
                    }
                }
            }

            @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override public void onProviderEnabled(String s) {}
            @Override public void onProviderDisabled(String s) {}
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        architectView.onPostCreate();
        try {
            architectView.load( "YOUR-AR-EXPERIENCE" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        architectView.onResume();
        // start location updates
        locationProvider.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        architectView.onPause();
        // stop location updates
        locationProvider.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        architectView.onDestroy();
    }
}
