package com.appynitty.swachbharatabhiyanlibrary.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;

import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.ShareLocationAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.view_model.LocationViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import quickutils.core.QuickUtils;

public class LocationMonitoringService implements LocationListener, GpsStatus.Listener {


    private static final String TAG = LocationMonitoringService.class.getSimpleName();

    private Context mContext;

    private LocationViewModel mLocationViewModel;

    private ShareLocationAdapterClass mAdapter;

    private long updatedTime = 0;


    public LocationMonitoringService (final Context context) {
        mContext = context;

        mLocationViewModel = ViewModelProviders.of((AppCompatActivity) AUtils.mCurrentContext).get(LocationViewModel.class);

        mLocationViewModel.getUserLocationEntityList().observeForever(new Observer<List<UserLocationEntity>>() {
            @Override
            public void onChanged(@Nullable final List<UserLocationEntity> userLocationEntities) {
                // Update list
                AUtils.UserLocationPojoList.clear();

                for(UserLocationEntity entity : userLocationEntities) {
                    UserLocationPojo userLocationPojo = new UserLocationPojo();
                    userLocationPojo.setOfflineId(String.valueOf(entity.getIndex_id()));
                    userLocationPojo.setUserId(QuickUtils.prefs.getString(AUtils.PREFS.USER_ID, ""));
                    userLocationPojo.setUserId(QuickUtils.prefs.getString(AUtils.PREFS.USER_TYPE_ID, ""));
                    userLocationPojo.setLat(entity.getLat());
                    userLocationPojo.setLong(entity.getLong());
                    userLocationPojo.setDatetime(entity.getDatetime());

                    AUtils.UserLocationPojoList.add(userLocationPojo);
                }
            }
        });

        mAdapter = new ShareLocationAdapterClass();

        mAdapter.setShareLocationListener(new ShareLocationAdapterClass.ShareLocationListener() {

            @Override
            public void onSuccessCallBack(boolean isAttendanceOff) {
                if(isAttendanceOff)
                {
                    AUtils.setIsOnduty(false);
                    AUtils.mApplication.stopLocationTracking();
                }
            }

            @Override
            public void onFailureCallBack() {

            }
        });
    }

    public void onStartTacking() {

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        //Exception thrown when GPS or Network provider were not available on the user's device.
        try {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
            criteria.setAltitudeRequired(false);
            criteria.setSpeedRequired(true);
            criteria.setCostAllowed(false);
            criteria.setBearingRequired(false);

            //API level 9 and up
            criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

            int gpsFreqInDistance = 0;

            locationManager.addGpsStatusListener(this);

            locationManager.requestLocationUpdates(AUtils.LOCATION_INTERVAL, gpsFreqInDistance, criteria, this, null);

        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getLocalizedMessage());
        } catch (SecurityException e) {
            Log.e(TAG, e.getLocalizedMessage());
        } catch (RuntimeException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    public void onStopTracking() {
        mAdapter.shareLocation();
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }

    /*
     * LOCATION CALLBACKS
     */


    //to get the location change
    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {

            Log.d(TAG, String.valueOf(location.getAccuracy()));

            if (!AUtils.isNullString(String.valueOf(location.getLatitude())) && !AUtils.isNullString(String.valueOf(location.getLongitude()))) {

                QuickUtils.prefs.save(AUtils.LAT, String.valueOf(location.getLatitude()));
                QuickUtils.prefs.save(AUtils.LONG, String.valueOf(location.getLongitude()));

                if(QuickUtils.prefs.getBoolean(AUtils.PREFS.IS_ON_DUTY,false)) {
                    if (updatedTime == 0) {
                        updatedTime = System.currentTimeMillis();

                        sendLocation();

                        Log.d(TAG, "updated Time ==== " + updatedTime);
                    }

                    if ((updatedTime + AUtils.LOCATION_INTERVAL_MINUTES) <= System.currentTimeMillis()) {
                        updatedTime = System.currentTimeMillis();

                        sendLocation();

                        Log.d(TAG, "updated Time ==== " + updatedTime);
                    }
                }
            }
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


    @Override
    public void onGpsStatusChanged(int event) {

    }

    private void sendLocation() {

        UserLocationPojo userLocationPojo = new UserLocationPojo();

        userLocationPojo.setUserId(QuickUtils.prefs.getString(AUtils.PREFS.USER_ID, ""));
        userLocationPojo.setLat(QuickUtils.prefs.getString(AUtils.LAT, ""));
        userLocationPojo.setLong(QuickUtils.prefs.getString(AUtils.LONG, ""));
        userLocationPojo.setDatetime(AUtils.getSeverDateTime());
        userLocationPojo.setOfflineId("0");

        if(AUtils.isInternetAvailable()) {

            AUtils.UserLocationPojoList.add(userLocationPojo);
            mAdapter.shareLocation();
        }else {
            UserLocationEntity entity = new UserLocationEntity();

            entity.setLat(userLocationPojo.getLat());
            entity.setLong(userLocationPojo.getLong());
            entity.setDatetime(userLocationPojo.getDatetime());

            mLocationViewModel.insert(entity);

            AUtils.UserLocationPojoList.clear();
        }
    }
}