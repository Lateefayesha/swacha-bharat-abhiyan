package com.appynitty.swachbharatabhiyanlibrary.services;

import android.util.Log;

import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.ShareLocationAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import quickutils.core.QuickUtils;

public class ShareLocationService implements Runnable {

    private static final String TAG = "ShareLocationService";
    ShareLocationAdapterClass mAdapter;

    public ShareLocationService() {
        mAdapter = new ShareLocationAdapterClass();

        mAdapter.setShareLocationListener(new ShareLocationAdapterClass.ShareLocationListener() {
            @Override
            public void onSuccessCallBack() {

            }

            @Override
            public void onFailureCallBack() {

            }
        });
    }


    @Override
    public void run() {

        try {
            String lat = QuickUtils.prefs.getString(AUtils.LAT, "");
            String Long = QuickUtils.prefs.getString(AUtils.LONG, "");

            if (!AUtils.isNullString(lat) && !AUtils.isNullString(Long)) {
                mAdapter.shareLocation();
            }

            wait(AUtils.SHARE_LOCATION_WAIT_TIME);
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }

    }
}
