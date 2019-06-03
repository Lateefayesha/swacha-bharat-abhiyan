package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.content.Intent;
import android.widget.Toast;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.activity.DashboardActivity;
import com.appynitty.swachbharatabhiyanlibrary.activity.EmpDashboardActivity;
import com.appynitty.swachbharatabhiyanlibrary.activity.SplashScreenActivity;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.InPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OutPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.mithsoft.lib.components.Toasty;

import quickutils.core.QuickUtils;

public class ShareLocationAdapterClass {

    private ShareLocationListener mListener;

    public ShareLocationListener getShareLocationListener() {
        return mListener;
    }

    public void setShareLocationListener(ShareLocationListener mListener) {
        this.mListener = mListener;
    }

    public void shareLocation() {

        if(!QuickUtils.prefs.getString(AUtils.PREFS.USER_TYPE_ID, "0").equals("1")){
            new MyAsyncTask(AUtils.mApplication.getApplicationContext(), false, new MyAsyncTask.AsynTaskListener() {
                UserLocationResultPojo resultPojo = null;
                @Override
                public void doInBackgroundOpration(SyncServer syncServer) {

                    resultPojo = syncServer.saveUserLocation();

                }

                @Override
                public void onFinished() {
                    if(!AUtils.isNull(resultPojo)) {
                        if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                            if(!AUtils.isNull(mListener))
                            {
                                mListener.onSuccessCallBack(resultPojo.getIsAttendenceOff());
                            }
                        } else {
                            if(!AUtils.isNull(mListener))
                            {
                                mListener.onFailureCallBack();
                            }
                        }
                    } else {
                        if(!AUtils.isNull(mListener))
                        {
                            mListener.onFailureCallBack();
                        }
                    }
                }

                @Override
                public void onInternetLost() {

                }
            }).execute();
        }
    }

    public interface ShareLocationListener {
        void onSuccessCallBack(boolean isAttendanceOff);
        void onFailureCallBack();
    }
}
