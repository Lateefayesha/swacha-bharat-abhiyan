package com.appynitty.swachbharatabhiyanlibrary.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyApplication;
import com.pixplicity.easyprefs.library.Prefs;

public class RestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(RestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        if(Prefs.getBoolean(AUtils.PREFS.IS_ON_DUTY,false))
        {
            if(!AUtils.isMyServiceRunning(AUtils.mainApplicationConstant,ForgroundService.class))
            {
                ((MyApplication)AUtils.mainApplicationConstant).startLocationTracking();
            }
        }
        //context.startService(new Intent(context, ForgroundService.class));
    }
}