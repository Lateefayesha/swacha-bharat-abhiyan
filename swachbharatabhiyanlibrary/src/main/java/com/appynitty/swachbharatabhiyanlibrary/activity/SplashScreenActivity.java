package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.appynitty.swachbharatabhiyanlibrary.BuildConfig;
import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;

import quickutils.core.QuickUtils;

public class SplashScreenActivity extends AppCompatActivity {

    private Snackbar mSnackbar;

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

        QuickUtils.prefs.save(AUtils.APP_ID, "1003");
        QuickUtils.prefs.save(AUtils.VERSION_CODE, 1);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Check internet connection and accrding to state change the
        // text of activity by calling method
        if (networkInfo != null && networkInfo.isConnected()) {
            if(mSnackbar.isShown())
            {
                mSnackbar.dismiss();
            }
        } else {
            View view = this.findViewById(R.id.parent);
            mSnackbar = Snackbar.make(view, "\u00A9"+"  "+ getResources().getString(R.string.no_internet_error), Snackbar.LENGTH_INDEFINITE);

            mSnackbar.show();
        }

        setDefaultLanguage();

        checkVersionDetails();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AUtils.mApplication.activityResumed();// On Resume notify the Application
    }

    @Override
    protected void onPause() {
        super.onPause();
        AUtils.mApplication.activityPaused();// On Pause notify the Application
        finish();
    }

    private void setDefaultLanguage() {
        AUtils.changeLanguage(SplashScreenActivity.this, Integer.parseInt(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID)));
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

    private void checkVersionDetails() {

        new MyAsyncTask(SplashScreenActivity.this, false, new MyAsyncTask.AsynTaskListener() {
            Boolean doUpdate = false;
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {
                doUpdate = syncServer.checkVersionUpdate();
            }

            @Override
            public void onFinished() {
                if(doUpdate){
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
                }else{
                    if(QuickUtils.prefs.getBoolean(AUtils.PREFS.IS_USER_LOGIN,false))
                    {
                        loadDashboard();
                    }  else {
                        loadLogin();
                    }
                }
            }
        }).execute();

    }


}