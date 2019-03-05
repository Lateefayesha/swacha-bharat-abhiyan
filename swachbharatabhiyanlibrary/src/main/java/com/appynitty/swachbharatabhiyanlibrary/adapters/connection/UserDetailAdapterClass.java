package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.VehicleTypePojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import quickutils.core.QuickUtils;

public class UserDetailAdapterClass {

    private UserDetailListener mListener;

    private UserDetailPojo userDetailPojo;

    private static Gson gson = new Gson();

    public UserDetailListener getUserDetailListener() {
        return mListener;
    }

    public void setUserDetailListener(UserDetailListener mListener) {
        this.mListener = mListener;
    }

    public UserDetailPojo getUserDetailPojo() {
        Type type = new TypeToken<UserDetailPojo>() {
        }.getType();

        userDetailPojo = new Gson().fromJson(
                QuickUtils.prefs.getString(AUtils.PREFS.USER_DETAIL_POJO, null), type);

        return userDetailPojo;
    }

    public static void setUserDetailPojo(UserDetailPojo userDetailPojo) {
        Type type = new TypeToken<UserDetailPojo>() {
        }.getType();
        QuickUtils.prefs.save(AUtils.PREFS.USER_DETAIL_POJO, gson.toJson(userDetailPojo, type));
    }

    public void getUserDetail() {

        new MyAsyncTask(AUtils.mCurrentContext, false, new MyAsyncTask.AsynTaskListener() {
            public boolean isDataPull = false;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                isDataPull = syncServer.pullUserDetailsFromServer();
            }

            @Override
            public void onFinished() {

                getUserDetailPojo();


                if (!AUtils.isNull(userDetailPojo)) {
                    if(!AUtils.isNull(mListener))
                        mListener.onSuccessCallBack();

                }
                else {
                    if(!AUtils.isNull(mListener))
                        mListener.onFailureCallBack();
                }

            }

            @Override
            public void onInternetLost() {

            }
        }).execute();
    }

    public interface UserDetailListener {
        void onSuccessCallBack();
        void onFailureCallBack();
    }
}
