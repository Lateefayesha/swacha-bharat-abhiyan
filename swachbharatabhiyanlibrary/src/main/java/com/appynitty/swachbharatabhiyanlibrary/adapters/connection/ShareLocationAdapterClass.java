package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.appynitty.swachbharatabhiyanlibrary.view_model.LocationViewModel;

import java.util.ArrayList;
import java.util.List;

import quickutils.core.QuickUtils;

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

        UserLocationPojo userLocationPojo = new UserLocationPojo();
        userLocationPojo.setUserId(QuickUtils.prefs.getString(AUtils.PREFS.USER_ID, ""));
        userLocationPojo.setLat(QuickUtils.prefs.getString(AUtils.LAT, ""));
        userLocationPojo.setLong(QuickUtils.prefs.getString(AUtils.LONG, ""));
        userLocationPojo.setDatetime(AUtils.getSeverDateTime());

        final List<UserLocationPojo> userLocationPojoList = new ArrayList<>();

        userLocationPojoList.add(userLocationPojo);

        if(AUtils.isNetWorkAvailable(AUtils.mCurrentContext)) {

            mLocationViewModel.getUserLocationEntityList().observe((LifecycleOwner) AUtils.mCurrentContext, new Observer<List<UserLocationEntity>>() {
                @Override
                public void onChanged(@Nullable final List<UserLocationEntity> userLocationEntities) {
                    // Update list

                    for(UserLocationEntity entity : userLocationEntities) {
                        UserLocationPojo userLocationPojo = new UserLocationPojo();
                        userLocationPojo.setUserId(QuickUtils.prefs.getString(AUtils.PREFS.USER_ID, ""));
                        userLocationPojo.setLat(entity.getLat());
                        userLocationPojo.setLong(entity.getLong());
                        userLocationPojo.setDatetime(entity.getDatetime());

                        userLocationPojoList.add(userLocationPojo);
                    }
                }
            });
            LiveData<List<UserLocationEntity>> databaseList = mLocationViewModel.getUserLocationEntityList();

            if (!QuickUtils.prefs.getString(AUtils.PREFS.USER_TYPE_ID, "0").equals("1")) {
                new MyAsyncTask(AUtils.mApplication.getApplicationContext(), false, new MyAsyncTask.AsynTaskListener() {
                    UserLocationResultPojo resultPojo = null;

                    @Override
                    public void doInBackgroundOpration(SyncServer syncServer) {

                        resultPojo = syncServer.saveUserLocation(userLocationPojoList);

                    }

                    @Override
                    public void onFinished() {
                        if (!AUtils.isNull(resultPojo)) {
                            if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                                if (!AUtils.isNull(mListener)) {
                                    mListener.onSuccessCallBack(resultPojo.getIsAttendenceOff());
                                }
                            } else {
                                if (!AUtils.isNull(mListener)) {
                                    mListener.onFailureCallBack();
                                }
                            }
                        } else {
                            if (!AUtils.isNull(mListener)) {
                                mListener.onFailureCallBack();
                            }
                        }
                    }

                    @Override
                    public void onInternetLost() {

                    }
                }).execute();
            }
        } else {

            UserLocationEntity entity = new UserLocationEntity();

            entity.setLat(userLocationPojo.getLat());
            entity.setLong(userLocationPojo.getLong());
            entity.setDatetime(userLocationPojo.getDatetime());

            mLocationViewModel.insert(entity);
        }
    }

    public interface ShareLocationListener {
        void onSuccessCallBack(boolean isAttendanceOff);

        void onFailureCallBack();
    }
}
