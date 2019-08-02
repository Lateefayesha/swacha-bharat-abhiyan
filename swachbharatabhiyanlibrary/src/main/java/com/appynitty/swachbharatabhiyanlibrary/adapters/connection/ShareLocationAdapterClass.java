package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.util.Log;

import com.appynitty.retrofitconnectionlibrary.connection.Connection;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.repository.LocationRepository;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.webservices.UserLocationWebService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareLocationAdapterClass {

    private ShareLocationListener mListener;

    private LocationRepository mLocationRepository;

    private List<UserLocationPojo> userLocationPojoList;

    public ShareLocationAdapterClass(){
        mLocationRepository = new LocationRepository(AUtils.mApplication.getApplicationContext());
        userLocationPojoList = new ArrayList<>();
    }

    public ShareLocationListener getShareLocationListener() {
        return mListener;
    }

    public void setShareLocationListener(ShareLocationListener mListener) {
        this.mListener = mListener;
    }

    public void shareLocation(final List<UserLocationPojo> userLocationPojos) {



        UserLocationWebService service = Connection.createService(UserLocationWebService.class, AUtils.SERVER_URL);

        service.saveUserLocation(QuickUtils.prefs.getString(AUtils.APP_ID, "1"),
                AUtils.CONTENT_TYPE,
                QuickUtils.prefs.getString(AUtils.PREFS.USER_TYPE_ID, "0"),
                AUtils.getBatteryStatus(), userLocationPojos)
                .enqueue(new Callback<List<UserLocationResultPojo>>() {
            @Override
            public void onResponse(Call<List<UserLocationResultPojo>> call, Response<List<UserLocationResultPojo>> response) {

                if (response.code() == 200) {
                    onResponseReceived(response.body(), userLocationPojos);
                } else {
                    mListener.onFailureCallBack();
                    Log.i(AUtils.TAG_HTTP_RESPONSE, "onFailureCallback: Response Code-" + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserLocationResultPojo>> call, Throwable t) {
                for (UserLocationPojo pojo : userLocationPojos) {

                    if(pojo.getOfflineId().equals("0")) {
                        Type type = new TypeToken<UserLocationPojo>() {}.getType();
                        mLocationRepository.insertUserLocationEntity(new Gson().toJson(pojo,type));
                    }
                }
                mListener.onFailureCallBack();
                Log.i(AUtils.TAG_HTTP_RESPONSE, "onFailureCallback: Response Code-" + t.getMessage());
            }
        });

    }

    public void shareLocation() {

        getDBList();

        if(userLocationPojoList.size() > 0) {

            UserLocationWebService service = Connection.createService(UserLocationWebService.class, AUtils.SERVER_URL);

            service.saveUserLocation(QuickUtils.prefs.getString(AUtils.APP_ID, "1"),
                    AUtils.CONTENT_TYPE,
                    QuickUtils.prefs.getString(AUtils.PREFS.USER_TYPE_ID, "0"),
                    AUtils.getBatteryStatus(),
                    userLocationPojoList).enqueue(new Callback<List<UserLocationResultPojo>>() {
                @Override
                public void onResponse(Call<List<UserLocationResultPojo>> call, Response<List<UserLocationResultPojo>> response) {

                    if (response.code() == 200) {
                        onResponseReceived(response.body(), userLocationPojoList);
                    } else {
                        mListener.onFailureCallBack();
                        Log.i(AUtils.TAG_HTTP_RESPONSE, "onFailureCallback: Response Code-" + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<UserLocationResultPojo>> call, Throwable t) {
                    mListener.onFailureCallBack();
                    Log.i(AUtils.TAG_HTTP_RESPONSE, "onFailureCallback: Response Code-" + t.getMessage());
                }
            });
        }

    }

    public List<UserLocationResultPojo> saveUserLocation(List<UserLocationPojo> userLocationPojoList) {

        List<UserLocationResultPojo> resultPojo = null;
        try {

            UserLocationWebService service = Connection.createService(UserLocationWebService.class, AUtils.SERVER_URL);


            resultPojo = service.saveUserLocation(QuickUtils.prefs.getString(AUtils.APP_ID, "1"), AUtils.CONTENT_TYPE, QuickUtils.prefs.getString(AUtils.PREFS.USER_ID, null), AUtils.getBatteryStatus(), userLocationPojoList).execute().body();

        } catch (Exception e) {

            e.printStackTrace();
        }
        return resultPojo;
    }

    private void onResponseReceived(List<UserLocationResultPojo> results, List<UserLocationPojo> userLocationPojos) {

        UserLocationResultPojo finalResult = null;

        if (!AUtils.isNull(results) && results.size() > 0) {

            for (UserLocationResultPojo result : results) {

                if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                    if (Integer.parseInt(result.getId()) != 0) {
                        mLocationRepository.deleteUserLocationEntity(Integer.parseInt(result.getId()));
                    } else {
                        finalResult = result;
                    }
                    for (int i = 0; i < userLocationPojos.size(); i++) {
                        if (userLocationPojos.get(i).getOfflineId().equals(result.getId())) {
                            userLocationPojos.remove(i);
                            break;
                        }
                    }

                } else {
                    if (!AUtils.isNull(mListener)) {
                        for (UserLocationPojo pojo : userLocationPojos) {
                            mLocationRepository.deleteAllUserLocationEntity();
                            finalResult = result;
                            break;
                        }
                    }
                }
            }

            if (!AUtils.isNull(mListener)) {
                if (finalResult != null && finalResult.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                    mListener.onSuccessCallBack(finalResult.getIsAttendenceOff());
                    userLocationPojos.clear();
                } else {
                    mListener.onFailureCallBack();
                }
            }
        }
    }

    private void getDBList() {
        List<UserLocationEntity> userLocationEntities = mLocationRepository.getAllUserLocationEntity();

        if(userLocationEntities.size() > 0) {

            userLocationPojoList.clear();

            for (UserLocationEntity entity : userLocationEntities) {

                Type type = new TypeToken<UserLocationPojo>() {}.getType();
                UserLocationPojo pojo = new Gson().fromJson(entity.getPojo(), type);
                pojo.setOfflineId(String.valueOf(entity.getIndex_id()));

                userLocationPojoList.add(pojo);
            }
        }
    }

    public interface ShareLocationListener {
        void onSuccessCallBack(boolean isAttendanceOff);

        void onFailureCallBack();
    }
}
