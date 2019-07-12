package com.appynitty.swachbharatabhiyanlibrary.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import quickutils.core.QuickUtils;

public class ForgroundService extends Service {

    private LocationMonitoringService monitoringService;

    private Handler locationHandler;

    private Runnable locationThread;

    private static final String PACKAGE_NAME =
            "com.appynitty.swachbharatabhiyanlibrary.services";

    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private static String channelId;

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

        locationThread = new Runnable() {
            public void run() {

                monitoringService.onStartTacking();
            }
        };

        locationHandler = new Handler(Looper.getMainLooper());
        locationHandler.post(locationThread);

        startLocationForeground();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        locationHandler.removeCallbacks(locationThread);
        monitoringService.onStopTracking();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            stopForeground(STOP_FOREGROUND_REMOVE);
        }

        if(QuickUtils.prefs.getBoolean(AUtils.PREFS.IS_ON_DUTY,false))
        {
            Intent broadcastIntent = new Intent(this, RestarterBroadcastReceiver.class);

            sendBroadcast(broadcastIntent);
        }
    }

    private void startLocationForeground() {


        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? this.createNotificationChannel("my_service", "My Background Service")
                : "";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder((Context)this,
                channelId);

        Notification notification = notificationBuilder
                .setOngoing(true)
                .setContentText("Please don't kill the app from background. Thank you!!")
                .setSmallIcon(R.drawable.ic_noti_icon)
                .setPriority(-2)
                .setCategory("service")
                .build();

        this.startForeground(101, notification);
    }

    @RequiresApi(26)
    private String createNotificationChannel(String channelId, String channelName) {

        NotificationChannel chan = new NotificationChannel(channelId, (CharSequence)channelName,
                NotificationManager.IMPORTANCE_NONE);

        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        service.createNotificationChannel(chan);
        return channelId;
    }
}
