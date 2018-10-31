package com.appynitty.swachbharatabhiyanlibrary.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.appynitty.swachbharatabhiyanlibrary.services.ForgroundService;
import com.appynitty.swachbharatabhiyanlibrary.services.LocationMonitoringService;

import quickutils.core.QuickUtils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        init QuickUtils lib
        QuickUtils.init(getApplicationContext());

        AUtils.setmApplication(this);
        //FirebaseApp.initializeApp(getApplicationContext());

//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "MYRIADPRO-REGULAR.OTF"); // font from assets: "assets/fonts/Roboto-Regular.ttf

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });


        if(QuickUtils.prefs.getBoolean(AUtils.PREFS.IS_ON_DUTY, false))
        {

        }
    }

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(LocaleHelper.onAttach(base, AUtils.DEFAULT_LANGUAGE_NAME));
    }

    public void startLocationTracking()
    {
        Intent intent = new Intent(this, ForgroundService.class);
        startService(intent);
    }

    public void stopLocationTracking()
    {
        stopService(new Intent(this, ForgroundService.class));
    }

}