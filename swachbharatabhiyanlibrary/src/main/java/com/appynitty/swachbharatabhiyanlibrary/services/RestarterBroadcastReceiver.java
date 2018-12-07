package com.appynitty.swachbharatabhiyanlibrary.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.util.Log;

import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import quickutils.core.QuickUtils;

public class RestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(RestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        if(QuickUtils.prefs.getBoolean(AUtils.PREFS.IS_ON_DUTY,false))
        {
            if(!AUtils.isMyServiceRunning(ForgroundService.class))
            {
                AUtils.mApplication.startLocationTracking();
            }
        }
        //context.startService(new Intent(context, ForgroundService.class));
    }
}