package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;

import quickutils.core.QuickUtils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context base) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            super.attachBaseContext(LocaleHelper.onAttach(base));
        }else{
            super.attachBaseContext(base);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AUtils.changeLanguage(this, Integer.parseInt(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID)));

        if(QuickUtils.prefs.getBoolean(AUtils.PREFS.IS_USER_LOGIN,false))
        {
            loadDashboard();
        }  else {
            loadLogin();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void loadDashboard() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
            }
        }, AUtils.SPLASH_SCREEN_TIME);
    }

    private void loadLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            }
        }, AUtils.SPLASH_SCREEN_TIME);
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}