package com.appynitty.swachbharatabhiyanlibrary.services;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.ShareLocationAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.repository.LocationRepository;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationMonitoringService implements LocationListener, GpsStatus.Listener {


    private static final String TAG = LocationMonitoringService.class.getSimpleName();

    private Context mContext;

    private LocationRepository mLocationRepository;

    private ShareLocationAdapterClass mAdapter;

    private long updatedTime = 0;

    private List<UserLocationPojo> mUserLocationPojoList;


    public LocationMonitoringService (final Context context) {
        mContext = context;

        mLocationRepository = new LocationRepository(AUtils.mainApplicationConstant.getApplicationContext());

        mUserLocationPojoList = new ArrayList<>();

        mAdapter = new ShareLocationAdapterClass();

        mAdapter.setShareLocationListener(new ShareLocationAdapterClass.ShareLocationListener() {

            @Override
            public void onSuccessCallBack(boolean isAttendanceOff) {
                if(isAttendanceOff)
                {
                    AUtils.setIsOnduty(false);
                    ((MyApplication)AUtils.mainApplicationConstant).stopLocationTracking();
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

                Prefs.putString(AUtils.LAT, String.valueOf(location.getLatitude()));
                Prefs.putString(AUtils.LONG, String.valueOf(location.getLongitude()));

                if(Prefs.getBoolean(AUtils.PREFS.IS_ON_DUTY,false)) {
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

        try {
            Date CurrentTime = AUtils.getCurrentTime();
            Date DutyOffTime = AUtils.getDutyEndTime();

            if(CurrentTime.before(DutyOffTime)) {

                Log.i(TAG,"Before");

                UserLocationPojo userLocationPojo = new UserLocationPojo();

                userLocationPojo.setUserId(Prefs.getString(AUtils.PREFS.USER_ID, ""));
                userLocationPojo.setLat(Prefs.getString(AUtils.LAT, ""));
                userLocationPojo.setLong(Prefs.getString(AUtils.LONG, ""));
                userLocationPojo.setDatetime(AUtils.getSeverDateTime());
                userLocationPojo.setOfflineId("0");

                if(AUtils.isInternetAvailable()) {

                    mUserLocationPojoList.add(userLocationPojo);
                    mAdapter.shareLocation(mUserLocationPojoList);
                }else {
                    Type type = new TypeToken<UserLocationPojo>() {}.getType();
                    mLocationRepository.insertUserLocationEntity(new Gson().toJson(userLocationPojo,type));

                    mUserLocationPojoList.clear();
                }
            }
            else {
                Log.i(TAG,"After");

                AUtils.setIsOnduty(false);
                ((MyApplication)AUtils.mainApplicationConstant).stopLocationTracking();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}