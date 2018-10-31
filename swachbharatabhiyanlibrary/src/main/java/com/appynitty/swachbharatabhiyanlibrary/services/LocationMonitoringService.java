package com.appynitty.swachbharatabhiyanlibrary.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import quickutils.core.QuickUtils;

public class LocationMonitoringService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {


    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = LocationRequest.create();
    Context mContext;

    public LocationMonitoringService (Context context)
    {
        mContext = context;
    }

    public void onStartTacking(){

        mLocationClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mLocationRequest.setInterval(AUtils.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(AUtils.FASTEST_LOCATION_INTERVAL);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationClient.connect();
    }

    public void onStopTracking()
    {
        mLocationClient.disconnect();
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {

    }


    //to get the location change
    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {

            if (!AUtils.isNullString(String.valueOf(location.getLatitude())) && !AUtils.isNullString(String.valueOf(location.getLongitude()))) {

                QuickUtils.prefs.save(AUtils.LAT, String.valueOf(location.getLatitude()));
                QuickUtils.prefs.save(AUtils.LONG, String.valueOf(location.getLongitude()));
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


    }


}