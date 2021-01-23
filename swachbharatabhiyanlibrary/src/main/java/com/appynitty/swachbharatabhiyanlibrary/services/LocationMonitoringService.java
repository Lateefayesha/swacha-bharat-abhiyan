package com.appynitty.swachbharatabhiyanlibrary.services;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.appynitty.swachbharatabhiyanlibrary.activity.DashboardActivity;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.ShareLocationAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.pojos.TableDataCountPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.repository.LocationRepository;
import com.appynitty.swachbharatabhiyanlibrary.repository.SyncOfflineAttendanceRepository;
import com.appynitty.swachbharatabhiyanlibrary.repository.SyncOfflineRepository;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class LocationMonitoringService implements LocationListener, GpsStatus.Listener {


    private static final String TAG = LocationMonitoringService.class.getSimpleName();

    private Context mContext;

    private LocationRepository mLocationRepository;

    private ShareLocationAdapterClass mAdapter;

    private SyncOfflineRepository syncOfflineRepository;

    private long updatedTime = 0;

    private List<UserLocationPojo> mUserLocationPojoList;

    private SyncOfflineAttendanceRepository syncOfflineAttendanceRepository;


    public LocationMonitoringService(final Context context) {
        mContext = context;

        mLocationRepository = new LocationRepository(AUtils.mainApplicationConstant.getApplicationContext());

        syncOfflineRepository = new SyncOfflineRepository(AUtils.mainApplicationConstant.getApplicationContext());

        syncOfflineAttendanceRepository = new SyncOfflineAttendanceRepository(AUtils.mainApplicationConstant.getApplicationContext());

        mUserLocationPojoList = new ArrayList<>();

        mAdapter = new ShareLocationAdapterClass();

        mAdapter.setShareLocationListener(new ShareLocationAdapterClass.ShareLocationListener() {

            @Override
            public void onSuccessCallBack(boolean isAttendanceOff) {
                if (isAttendanceOff && !syncOfflineAttendanceRepository.checkIsAttendanceIn()) {
                    AUtils.setIsOnduty(false);
                    ((MyApplication) AUtils.mainApplicationConstant).stopLocationTracking();
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

            assert locationManager != null;
            locationManager.addGpsStatusListener(this);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, AUtils.LOCATION_INTERVAL,
                    gpsFreqInDistance, this, null);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, AUtils.LOCATION_INTERVAL,
                    gpsFreqInDistance, this, null);


        } catch (IllegalArgumentException | SecurityException e) {
            Log.e(TAG, Objects.requireNonNull(e.getLocalizedMessage()));
            Log.d(TAG, "onStartTacking: " + e.getMessage());
            Log.d(TAG, "onStartTacking: " + e.getLocalizedMessage());

            e.printStackTrace();
        } catch (RuntimeException e) {
            Log.e(TAG, Objects.requireNonNull(e.getLocalizedMessage()));
            Log.d(TAG, "onStartTacking: " + e.getMessage());
            e.printStackTrace();

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

                if (Prefs.getBoolean(AUtils.PREFS.IS_ON_DUTY, false)) {
                    if (updatedTime == 0) {
                        updatedTime = System.currentTimeMillis();
                        Log.d(TAG, "updated Time ==== " + updatedTime);
                    }

                    if ((updatedTime + AUtils.LOCATION_INTERVAL_MINUTES) <= System.currentTimeMillis()) {
                        updatedTime = System.currentTimeMillis();
                        Log.d(TAG, "updated Time ==== " + updatedTime);
                    }

                    sendLocation();
                }
            }
        } else {
            Log.d(TAG, "onLocationChanged:  no location found !!");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged" + provider + "Status" + status);

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, " onProviderEnabled" + provider);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, " onProviderDisabled" + provider);
    }


    @Override
    public void onGpsStatusChanged(int event) {

    }

    private void sendLocation() {
        Log.d(TAG, "sendLocation: Current Time In Millies"+ System.currentTimeMillis());

        try {
            Calendar CurrentTime = AUtils.getCurrentTime();
            Calendar DutyOffTime = AUtils.getDutyEndTime();

            if (CurrentTime.before(DutyOffTime)) {

                Log.i(TAG, "Before");

                UserLocationPojo userLocationPojo = new UserLocationPojo();

                userLocationPojo.setUserId(Prefs.getString(AUtils.PREFS.USER_ID, ""));
                userLocationPojo.setLat(Prefs.getString(AUtils.LAT, ""));
                userLocationPojo.setLong(Prefs.getString(AUtils.LONG, ""));
                double startLat = Double.parseDouble(Prefs.getString(AUtils.LAT, "0"));
                double startLng = Double.parseDouble(Prefs.getString(AUtils.LONG, "0"));
                userLocationPojo.setDistance(String.valueOf(AUtils.calculateDistance(
                        AUtils.mainApplicationConstant.getApplicationContext(), startLat, startLng)));
//                userLocationPojo.setDatetime(AUtils.getServerDateTime()); //TODO
                userLocationPojo.setDatetime(AUtils.getServerDateTimeLocal());
                userLocationPojo.setOfflineId("0");

                if (AUtils.isInternetAvailable() && AUtils.isConnectedFast(mContext))
                    userLocationPojo.setIsOffline(true);
                else
                    userLocationPojo.setIsOffline(false);

                String UserTypeId = Prefs.getString(AUtils.PREFS.USER_TYPE_ID, AUtils.USER_TYPE.USER_TYPE_GHANTA_GADI);
                if (AUtils.isInternetAvailable()) {
                    TableDataCountPojo.LocationCollectionCount count = syncOfflineRepository.getLocationCollectionCount(AUtils.getLocalDate());
                    if ((UserTypeId.equals(AUtils.USER_TYPE.USER_TYPE_GHANTA_GADI) || UserTypeId.equals(AUtils.USER_TYPE.USER_TYPE_WASTE_MANAGER)) && (count.getLocationCount() > 0 || count.getCollectionCount() > 0)) {
                        syncOfflineRepository.insetUserLocation(userLocationPojo);
                    } else {
                        mUserLocationPojoList.add(userLocationPojo);
                        mAdapter.shareLocation(mUserLocationPojoList);
                        mUserLocationPojoList.clear();
                    }
                } else {
                    if (UserTypeId.equals(AUtils.USER_TYPE.USER_TYPE_EMP_SCANNIFY)) {
                        Type type = new TypeToken<UserLocationPojo>() {
                        }.getType();
                        mLocationRepository.insertUserLocationEntity(new Gson().toJson(userLocationPojo, type));
                    } else {
                        syncOfflineRepository.insetUserLocation(userLocationPojo);
                    }
                    mUserLocationPojoList.clear();
                }
            } else {
                Log.i(TAG, "After");

                syncOfflineAttendanceRepository.performCollectionInsert(mContext,
                        syncOfflineAttendanceRepository.checkAttendance(), AUtils.getCurrentDateDutyOffTime());

                AUtils.setIsOnduty(false);
                ((MyApplication) AUtils.mainApplicationConstant).stopLocationTracking();

                Activity activity = ((Activity) AUtils.currentContextConstant);

                if (activity instanceof DashboardActivity) {
                    ((Activity) AUtils.currentContextConstant).recreate();
                    AUtils.DutyOffFromService = true;
                }

                if (!AUtils.isNull(AUtils.currentContextConstant)) {
                    ((Activity) AUtils.currentContextConstant).recreate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


    }
}