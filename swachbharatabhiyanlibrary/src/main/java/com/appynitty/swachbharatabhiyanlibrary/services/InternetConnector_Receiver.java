package com.appynitty.swachbharatabhiyanlibrary.services;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyApplication;

public class InternetConnector_Receiver extends BroadcastReceiver {

    private Snackbar mSnackbar;

    public InternetConnector_Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            boolean isVisible = MyApplication.isActivityVisible();// Check if
            // activity
            // is
            // visible
            // or not
            Log.i("Activity is Visible ", "Is activity visible : " + isVisible);

            // If it is visible then trigger the task else do nothing
            if (isVisible == true) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager
                        .getActiveNetworkInfo();

                // Check internet connection and accrding to state change the
                // text of activity by calling method
                if (networkInfo != null && networkInfo.isConnected()) {
                    if(mSnackbar.isShown())
                    {
                        mSnackbar.dismiss();
                    }
                } else {
                    Activity currentAct = (Activity)AUtils.mCurrentContext;
                    View view = currentAct.findViewById(R.id.parent);
                    mSnackbar = Snackbar.make(view, "\u00A9"+"  "+ currentAct.getResources().getString(R.string.no_internet_error), Snackbar.LENGTH_INDEFINITE);

                    mSnackbar.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}