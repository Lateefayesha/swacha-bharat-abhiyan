package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.VersionDetailsAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;

import quickutils.core.QuickUtils;

public class SplashScreenActivity extends AppCompatActivity {

    VersionDetailsAdapterClass mAdapter;

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

        AUtils.mCurrentContext = this;

        mAdapter = new VersionDetailsAdapterClass();

        QuickUtils.prefs.save(AUtils.APP_ID, "1");
        QuickUtils.prefs.save(AUtils.VERSION_CODE, 10);

        setDefaultLanguage();

        mAdapter.checkVersionDetails();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAdapter.setVersionDetailsListener(new VersionDetailsAdapterClass.VersionDetailsListener() {
            @Override
            public void onSuccessCallBack() {
                AUtils.showConfirmationDialog(SplashScreenActivity.this, AUtils.VERSION_CODE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        AUtils.rateApp(SplashScreenActivity.this);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        finish();
                    }
                });
            }

            @Override
            public void onFailureCallBack() {
                if(QuickUtils.prefs.getBoolean(AUtils.PREFS.IS_USER_LOGIN,false))
                {
                    loadDashboard();
                }  else {
                    loadLogin();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(AUtils.isNetWorkAvailable(this))
        {
            AUtils.hideSnackBar();
        }
        else {
            AUtils.showSnackBar(this);
        }
    }

    private void setDefaultLanguage() {
        AUtils.changeLanguage(SplashScreenActivity.this, Integer.parseInt(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID)));
    }

    private void loadDashboard() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(QuickUtils.prefs.getString(AUtils.PREFS.USER_TYPE_ID, "0").equals("1"))
                    startActivity(new Intent(SplashScreenActivity.this, EmpDashboardActivity.class));
                else
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
}