package com.appynitty.swachbharatabhiyanlibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.activity.TakePhotoActivity;
import com.mithsoft.lib.components.Toasty;
import com.mithsoft.lib.utils.MsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import quickutils.core.QuickUtils;

public class AUtils extends MsUtils {

    //    Local URL
    public static final String SERVER_URL = "http://192.168.200.4:6077/";

    //    Staging URL
//    public static final String SERVER_URL = "http://115.115.153.117:6088/";

    //    Relese URL
//    public static final String SERVER_URL = "http://115.115.153.117:7055/";


    //    Genral Constant
    public static final String STATUS_SUCCESS = "success";

    public static final String STATUS_ERROR = "error";
    public static final String STATUS_EXPIRED = "expired";

    public static final String LANGUAGE_NAME = "LanguageName";

    public static final String DEFAULT_LANGUAGE_NAME = "mi";

    public static final String CONTENT_TYPE = "application/json";

    public static final String APP_ID = "AppId";


    public static final String DIALOG_TYPE_VEHICLE = "DialogTypeVehicle";
    public static final String DIALOG_TYPE_LANGUAGE = "Dialog_Type_Language";

    public static final String DEFAULT_LANGUAGE_ID = "2";
    public static final String LANGUAGE_ID = "LanguageId";

    public static final int SPLASH_SCREEN_TIME = 3000;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 444;
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 555;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 666;

    public static final int MY_RESULT_REQUEST_QR = 55;

    public static final String REQUEST_CODE = "RequestCode";

    public static final String VEHICLE_ID = "VehicleId";

    public static final String LAT = "Lat";
    public static final String LONG = "Long";

    public static final int LOCATION_INTERVAL = 10000;
    public static final int FASTEST_LOCATION_INTERVAL = 5000;

    public static final String LOCATION = "LocationLatLog";
    public static final String HISTORY_DETAILS = "HistoryDetails";

    public static MyApplication mApplication;

    private static final String SERVER_DATE_FORMATE = "MM-dd-yyyy";

    private static final String SERVER_TIME_FORMATE = "HH:mm";

    private static final String SERVER_DATE_TIME_FORMATE = "MM-dd-yyyy HH:mm:ss";

    public static Context mCurrentContext;

    public static final int SHARE_LOCATION_WAIT_TIME = 10000;

    // Language Change of an application
    public static void changeLanguage(Activity context, int languageId) {

        String languageStr = "";
        switch (languageId) {
            case 1:
                languageStr = "en";
                QuickUtils.prefs.save(AUtils.LANGUAGE_NAME,languageStr);
                break;
            case 2:
                languageStr = "mi";
                QuickUtils.prefs.save(AUtils.LANGUAGE_NAME,languageStr);
                break;
            case 3:
                languageStr = "hi";
                QuickUtils.prefs.save(AUtils.LANGUAGE_NAME,languageStr);
                break;
            case 4:
                languageStr = "gu";
                QuickUtils.prefs.save(AUtils.LANGUAGE_NAME,languageStr);
                break;
            case 5:
                languageStr = "pa";
                QuickUtils.prefs.save(AUtils.LANGUAGE_NAME,languageStr);
                break;
        }

        Locale locale = new Locale(languageStr);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.setDefault(Locale.Category.DISPLAY, locale);
        } else {
            Locale.setDefault(locale);
        }
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
        context.onConfigurationChanged(config);
    }

    //app setting for permissions dialog
    public static void showPermissionDialog(Context context, String message, DialogInterface.OnClickListener okListener) {

        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle("Need Permission")
                .setMessage("App needs a permission to access " + message)
                .setPositiveButton("Grant", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    public interface PREFS {

        //    Save Data Constant
        String IS_USER_LOGIN = "UserLoginStatus";
        String USER_ID = "UserId";
        String USER_TYPE = "UserType";
        String IS_ON_DUTY = "isOnDuty";

        String IMAGE_POJO = "ImagePojo";

        String IN_PUNCH_POJO = "InPunchPull";
        String VEHICLE_TYPE_POJO_LIST = "VehicleTypePullList";
        String USER_DETAIL_POJO = "UserDetailPull";
        String WORK_HISTORY_POJO_LIST = "WorkHistoryPullList";
        String WORK_HISTORY_DETAIL_POJO_LIST = "WorkHistoryDetailPullList";
    }

    public static boolean isCameraPermissionGiven(final Context context)
    {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.CAMERA)) {
                //Show Information about why you need the permission

                AUtils.showPermissionDialog(context, "CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ((Activity)context).requestPermissions(new String[]{Manifest.permission.CAMERA}, AUtils.MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                });

            } else if (QuickUtils.prefs.getBoolean(Manifest.permission.CAMERA, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission

                AUtils.showPermissionDialog(context, "CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        AUtils.goToAppSettings(context);
                    }
                });

            } else {
                //just request the permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity)context).requestPermissions(new String[]{Manifest.permission.CAMERA}, AUtils.MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }

            QuickUtils.prefs.save(Manifest.permission.CAMERA, true);
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isStoragePermissionGiven(final Context context)
    {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission

                AUtils.showPermissionDialog(context, "EXTERNAL STORAGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ((Activity)context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AUtils.MY_PERMISSIONS_REQUEST_STORAGE);
                        }
                    }
                });

            } else if (QuickUtils.prefs.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission

                AUtils.showPermissionDialog(context, "EXTERNAL STORAGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        AUtils.goToAppSettings(context);
                    }
                });

            } else {
                //just request the permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity)context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AUtils.MY_PERMISSIONS_REQUEST_STORAGE);
                }
            }

            QuickUtils.prefs.save(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isLocationPermissionGiven(final Context context)
    {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Show Information about why you need the permission

                AUtils.showPermissionDialog(context, "LOCATION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ((Activity)context).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AUtils.MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }
                });

            } else if (QuickUtils.prefs.getBoolean(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission

                AUtils.showPermissionDialog(context, "LOCATION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        AUtils.goToAppSettings(context);
                    }
                });

            } else {
                //just request the permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity)context).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AUtils.MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }

            QuickUtils.prefs.save(Manifest.permission.ACCESS_FINE_LOCATION, true);
            return false;
        }
        else {
            return true;
        }
    }

    public static void runOnUIThread(Runnable r)
    {
        new Handler(Looper.getMainLooper()).post(r);
    }

    public static void saveLocation(Location location){
        if(!AUtils.isNull(location)){
            Double latti = location.getLatitude();
            Double longi = location.getLongitude();

            QuickUtils.prefs.save(AUtils.LONG, longi.toString());
            QuickUtils.prefs.save(AUtils.LAT, latti.toString());
        }
    }

    public static String getSeverDate() {

        SimpleDateFormat format = new SimpleDateFormat(AUtils.SERVER_DATE_FORMATE, Locale.ENGLISH);
        return format.format(Calendar.getInstance().getTime());
    }

    public static String getSeverTime() {

        SimpleDateFormat format = new SimpleDateFormat(AUtils.SERVER_TIME_FORMATE, Locale.ENGLISH);
        return format.format(Calendar.getInstance().getTime());
    }

    public static ArrayList<String> getMonthSpinnerList(){
        ArrayList<String> spinnerList = new ArrayList<>();

        spinnerList.add("Select Month");
        spinnerList.add("January");
        spinnerList.add("February");
        spinnerList.add("March");
        spinnerList.add("May");
        spinnerList.add("June");
        spinnerList.add("July");
        spinnerList.add("August");
        spinnerList.add("September");
        spinnerList.add("Qctober");
        spinnerList.add("November");
        spinnerList.add("December");

        return spinnerList;
    }

    public static ArrayList<String> getYearSpinnerList(){
        ArrayList<String> spinnerList = new ArrayList<>();

        spinnerList.add("Select Year");
        for(int i = 0; i > -5; i--){
            Calendar currYear = Calendar.getInstance();
            currYear.add(Calendar.YEAR, i);
            spinnerList.add(String.valueOf(currYear.get(Calendar.YEAR)));
        }

        return spinnerList;
    }

    public static String getSeverDateTime() {

        SimpleDateFormat format = new SimpleDateFormat(AUtils.SERVER_DATE_TIME_FORMATE, Locale.ENGLISH);
        return format.format(Calendar.getInstance().getTime());
    }

    public static void showWarning(Context context, String message)
    {
        Toasty.custom(context,message, R.drawable.ic_error_outline_white_48dp,Color.parseColor("#C8FE973C"),
                Toast.LENGTH_SHORT,true,true).show();
    }
}
