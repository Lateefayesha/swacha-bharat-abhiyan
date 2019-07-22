package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.appynitty.retrofitconnectionlibrary.connection.Connection;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.view_model.LocationViewModel;
import com.appynitty.swachbharatabhiyanlibrary.webservices.UserLocationWebService;

import java.util.List;

import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareLocationAdapterClass {

    private ShareLocationListener mListener;

    private LocationViewModel mLocationViewModel;

    public ShareLocationListener getShareLocationListener() {
        return mListener;
    }

    public void setShareLocationListener(ShareLocationListener mListener) {
        this.mListener = mListener;
    }

    public void shareLocation() {

        mLocationViewModel = ViewModelProviders.of((AppCompatActivity) AUtils.mCurrentContext).get(LocationViewModel.class);

        if (!QuickUtils.prefs.getString(AUtils.PREFS.USER_TYPE_ID, "0").equals("1")) {

            UserLocationWebService service = Connection.createService(UserLocationWebService.class, AUtils.SERVER_URL);

            service.saveUserLocation(QuickUtils.prefs.getString(AUtils.APP_ID, "1"),
                    AUtils.CONTENT_TYPE,
                    QuickUtils.prefs.getString(AUtils.PREFS.USER_ID, null),
                    AUtils.getBatteryStatus(), AUtils.UserLocationPojoList)
                    .enqueue(new Callback<List<UserLocationResultPojo>>() {
                @Override
                public void onResponse(Call<List<UserLocationResultPojo>> call, Response<List<UserLocationResultPojo>> response) {

                    if (response.code() == 200) {
                        onResponseReceived(response.body());
                    } else {
                        mListener.onFailureCallBack();
                        Log.i(AUtils.TAG_HTTP_RESPONSE, "onFailureCallback: Response Code-" + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<UserLocationResultPojo>> call, Throwable t) {
                    for (UserLocationPojo pojo : AUtils.UserLocationPojoList) {

                        UserLocationEntity entity = new UserLocationEntity();
                        entity.setLat(pojo.getLat());
                        entity.setLong(pojo.getLong());
                        entity.setDatetime(pojo.getDatetime());

                        mLocationViewModel.insert(entity);
                    }
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

    private void onResponseReceived(List<UserLocationResultPojo> results) {

        UserLocationResultPojo finalResult = null;

        if (!AUtils.isNull(results) && results.size() > 0) {

            for (UserLocationResultPojo result : results) {

                if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                    if (Integer.parseInt(result.getId()) != 0) {
                        mLocationViewModel.deleteSelectedRecord(Integer.parseInt(result.getId()));
                    } else {
                        finalResult = result;
                    }
                    for (int i = 0; i < AUtils.UserLocationPojoList.size(); i++) {
                        if (AUtils.UserLocationPojoList.get(i).getOfflineId().equals(result.getId())) {
                            AUtils.UserLocationPojoList.remove(i);
                            break;
                        }
                    }

                } else {
                    if (!AUtils.isNull(mListener)) {
                        for (UserLocationPojo pojo : AUtils.UserLocationPojoList) {
                            mLocationViewModel.deleteAllRecord();
                            finalResult = result;
                            break;
                        }
                    }
                }
            }

            if (!AUtils.isNull(mListener)) {
                if (finalResult != null && finalResult.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                    mListener.onSuccessCallBack(finalResult.getIsAttendenceOff());
                    AUtils.UserLocationPojoList.clear();
                } else {
                    mListener.onFailureCallBack();
                }
            }
        }
    }

    public interface ShareLocationListener {
        void onSuccessCallBack(boolean isAttendanceOff);

        void onFailureCallBack();
    }
}
