package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.widget.Toast;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.InPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OutPunchPojo;
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

        new MyAsyncTask(AUtils.mCurrentContext, true, new MyAsyncTask.AsynTaskListener() {
            ResultPojo resultPojo = null;
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                resultPojo = syncServer.saveUserLocation();

            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        QuickUtils.prefs.save(AUtils.PREFS.IS_ON_DUTY, false);
                        if(!AUtils.isNull(mListener))
                        {
                            mListener.onSuccessCallBack();
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
        }).execute();
    }

    public interface ShareLocationListener {
        void onSuccessCallBack();
        void onFailureCallBack();
    }
}
