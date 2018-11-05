package com.appynitty.swachbharatabhiyanlibrary.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

public class ForgroundService extends Service {

    LocationMonitoringService monitoringService;


    @Override
        public IBinder onBind(Intent intent) {
            // TODO Auto-generated method stub
            return null;
        }

    @Override
        public void onCreate() {
            //Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
            monitoringService = new LocationMonitoringService(this);
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            //Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
            final int currentId = startId;

            Runnable r = new Runnable() {
                public void run() {

                    monitoringService.onStartTacking();
                }
            };

            Handler H = new Handler(Looper.getMainLooper());
            H.post(r);

            return Service.START_STICKY;
        }

        @Override
        public void onDestroy() {
            //Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();

            monitoringService.onStopTracking();

        }

    }
