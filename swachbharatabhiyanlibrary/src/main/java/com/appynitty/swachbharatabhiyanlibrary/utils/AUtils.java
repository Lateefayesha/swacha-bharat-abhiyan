package com.appynitty.swachbharatabhiyanlibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.EmpSyncServerAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.SyncServerAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OfflineGarbageColectionPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.QrLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;
import com.google.android.material.snackbar.Snackbar;
import com.mithsoft.lib.components.Toasty;
import com.mithsoft.lib.utils.MsUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import quickutils.core.QuickUtils;

public class AUtils extends MsUtils {

    //    Local URL
//    public static final String SERVER_URL = "http://192.168.200.4:6077/";

    //    Staging URL
//    public static final String SERVER_URL = "http://115.115.153.117:4044/";

    //    Relese URL
    public static final String SERVER_URL = "https://ghantagadi.in:444/";

    //    Relese Backup URL
//    public static final String SERVER_URL = "http://202.65.157.253:4044/";


    //    General Constant
    public static final String STATUS_SUCCESS = "success";

    public static final String STATUS_ERROR = "error";

    public static final String LANGUAGE_NAME = "LanguageName";

    public static final String DEFAULT_LANGUAGE_NAME = "en";

    public static final String CONTENT_TYPE = "application/json";

    public static final String APP_ID = "AppId";
    public static final String VERSION_CODE = "AppVersion";

    public static final String DIALOG_TYPE_VEHICLE = "DialogTypeVehicle";
    public static final String DIALOG_TYPE_LANGUAGE = "Dialog_Type_Language";

    public static final String DEFAULT_LANGUAGE_ID = "1";
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

    public static final String HISTORY_DETAILS_DATE = "HistoryDetailsDate";
    public static final String RADIO_SELECTED_HP = "house_point";
    public static final String RADIO_SELECTED_GP = "garbage_point";
    public static final String RADIO_SELECTED_DY = "garbage_dump_yard";

    public static final String CONFIRM_LOGOUT_DIALOG = "confirmLogout";
    public static final String CONFIRM_OFFDUTY_DIALOG = "confirmOffDuty";

    public static final String isFromLogin = "isFromLogin";
    public static final String dumpYardId = "dumpYardId";

    public static MyApplication mApplication;

    private static final String SERVER_DATE_FORMATE = "MM-dd-yyyy";
    private static final String EMP_SERVER_DATE_FORMATE = "dd-MM-yyyy";

    private static final String TITLE_DATE_FORMATE = "dd MMM yyyy";

    private static final String SEMI_MONTH_FORMATE = "MMM";
    private static final String DATE_VALUE_FORMATE = "dd";

    private static final String SERVER_TIME_FORMATE = "hh:mm a";
    private static final String SERVER_TIME_24HR_FORMATE = "HH:mm";

    private static final String SERVER_DATE_TIME_FORMATE = "MM-dd-yyyy HH:mm:ss";

    public static Context mCurrentContext;

    public static final long LOCATION_INTERVAL_MINUTES = 10 * 60 * 1000;

    public static final String VEHICLE_NO = "VehicleNumber";

    private static final String TAG = "AUtils";

    public static final String HP_AREA_TYPE_ID = "1";

    public static final String GP_AREA_TYPE_ID = "2";

    public static final String DY_AREA_TYPE_ID = "3";

    private static Snackbar mSnackbar;

    public final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public static final String TAG_HTTP_RESPONSE = "HTTPResponse_Error";
    public static final String ADD_DETAILS_TYPE_KEY = "ADD_DETAILS_TYPE_KEY";
    public static final int ADD_DETAILS_REQUEST_KEY = 8986;

    public static final String DATABASE_NAME = "db_sba_offline";
    public static final String LOCATION_TABLE_NAME = "table_location";
    public static final String COLLECTION_TABLE_NAME = "table_gcollection";
    public static final String QR_TABLE_NAME = "table_qr_emp";

    private static SyncServerAdapterClass syncServer;

    private static EmpSyncServerAdapterClass empSyncServer;

    public static boolean isIsOnduty() {
        return QuickUtils.prefs.getBoolean(PREFS.IS_ON_DUTY, false);
    }

    public static void setIsOnduty(boolean isOnduty) {
        QuickUtils.prefs.save(PREFS.IS_ON_DUTY, isOnduty);
    }

    // Language Change of an application
    public static void changeLanguage(Activity context, int languageId) {

        String languageStr = "";
        switch (languageId) {
            case 1:
                languageStr = "en";
                QuickUtils.prefs.save(AUtils.LANGUAGE_ID, String.valueOf(languageId));
                break;
            case 2:
                languageStr = "mr";
                QuickUtils.prefs.save(AUtils.LANGUAGE_ID, String.valueOf(languageId));
                break;
            case 3:
                languageStr = "hi";
                QuickUtils.prefs.save(AUtils.LANGUAGE_ID, String.valueOf(languageId));
                break;
            case 4:
                languageStr = "gu";
                QuickUtils.prefs.save(AUtils.LANGUAGE_ID, String.valueOf(languageId));
                break;
            case 5:
                languageStr = "pa";
                QuickUtils.prefs.save(AUtils.LANGUAGE_ID, String.valueOf(languageId));
                break;
        }

        LocaleHelper.setLocale(context, languageStr);
    }

    //app setting for permissions dialog
    public static void showPermissionDialog(Context context, String message, DialogInterface.OnClickListener okListener) {

        new AlertDialog.Builder(context)
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

    public static void showConfirmationDialog(Context context, String type, DialogInterface.OnClickListener positiveListener, @Nullable DialogInterface.OnClickListener negativeLisner) {

        String message = "";
        String title = "";
        String positiveText = context.getResources().getString(R.string.yes_txt);
        String negativeText = context.getResources().getString(R.string.no_txt);

        if(type.equals(CONFIRM_LOGOUT_DIALOG)){
            title = context.getResources().getString(R.string.logout_confirmation_title);
            message = context.getResources().getString(R.string.logout_confirmation_msg);
        }else if(type.equals(CONFIRM_OFFDUTY_DIALOG)){
            title = context.getResources().getString(R.string.offduty_confirmation_title);
            message = context.getResources().getString(R.string.offduty_confirmation_msg);
        }else if(type.equals(VERSION_CODE)){
            title = context.getResources().getString(R.string.update_title);
            message = context.getResources().getString(R.string.update_message);
            positiveText = context.getResources().getString(R.string.update_txt);
            negativeText = context.getResources().getString(R.string.no_thanks_txt);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(positiveText, positiveListener);

                if(negativeLisner != null){
                    builder.setNegativeButton(negativeText, negativeLisner);
                }else{
                    builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                }

                builder.create()
                    .show();
    }

    public interface PREFS {

        //    Save Data Constant
        String IS_USER_LOGIN = "UserLoginStatus";
        String USER_ID = "UserId";
        String USER_TYPE = "UserType";
        String USER_TYPE_ID = "UserTypeId";
        String IS_GT_FEATURE = "isGtFeature";
        String IS_ON_DUTY = "isOnDuty";

        String IMAGE_POJO = "ImagePojo";

        String IN_PUNCH_POJO = "InPunchPull";
        String VEHICLE_TYPE_POJO_LIST = "VehicleTypePullList";
        String USER_DETAIL_POJO = "UserDetailPull";
        String WORK_HISTORY_POJO_LIST = "WorkHistoryPullList";
        String WORK_HISTORY_DETAIL_POJO_LIST = "WorkHistoryDetailPullList";
        String LANGUAGE_POJO_LIST = "LanguagePullList";
    }

    public interface NondaniLocation{
        String OPEN_FORM_TYPE = "OPEN_FORM_TYPE";
        String SUBMIT_TYPE = "SUBMIT_TYPE";
        String REFERENCE_ID = "REFERENCE_ID";
    }

    public interface DUMPDATA {
        String dumpDataMap = "dump_data_map";
        String dumpYardId = "dump_yard_id";
        String weightTotal = "total_weight";
        String weightTotalDry = "total_weight_dry";
        String weightTotalWet = "total_weight_wet";
    }

    public static boolean isCameraPermissionGiven(final Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.CAMERA)) {
                //Show Information about why you need the permission

                AUtils.showPermissionDialog(context, "CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ((Activity) context).requestPermissions(new String[]{Manifest.permission.CAMERA}, AUtils.MY_PERMISSIONS_REQUEST_CAMERA);
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
                    ((Activity) context).requestPermissions(new String[]{Manifest.permission.CAMERA}, AUtils.MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }

            QuickUtils.prefs.save(Manifest.permission.CAMERA, true);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isStoragePermissionGiven(final Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission

                AUtils.showPermissionDialog(context, "EXTERNAL STORAGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ((Activity) context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AUtils.MY_PERMISSIONS_REQUEST_STORAGE);
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
                    ((Activity) context).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AUtils.MY_PERMISSIONS_REQUEST_STORAGE);
                }
            }

            QuickUtils.prefs.save(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isLocationPermissionGiven(final Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Show Information about why you need the permission

                AUtils.showPermissionDialog(context, "LOCATION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ((Activity) context).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AUtils.MY_PERMISSIONS_REQUEST_LOCATION);
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
                    ((Activity) context).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AUtils.MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }

            QuickUtils.prefs.save(Manifest.permission.ACCESS_FINE_LOCATION, true);
            return false;
        } else {
            return true;
        }
    }

    public static void runOnUIThread(Runnable r) {
        new Handler(Looper.getMainLooper()).post(r);
    }

    public static void saveLocation(Location location) {
        if (!AUtils.isNull(location)) {
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

    public static ArrayList<String> getMonthSpinnerList() {
        ArrayList<String> spinnerList = new ArrayList<>();

        spinnerList.add("Select Month");
        for (int i = 0; i < 12; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.ENGLISH);
            cal.set(Calendar.MONTH, i);
            String month_name = month_date.format(cal.getTime());
            spinnerList.add(month_name);
        }

        return spinnerList;
    }

    public static ArrayList<String> getYearSpinnerList() {
        ArrayList<String> spinnerList = new ArrayList<>();

        spinnerList.add("Select Year");
        for (int i = 0; i > -5; i--) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, i);
            spinnerList.add(String.valueOf(calendar.get(Calendar.YEAR)));
        }

        return spinnerList;
    }

    public static String getSeverDateTime() {

        SimpleDateFormat format = new SimpleDateFormat(AUtils.SERVER_DATE_TIME_FORMATE, Locale.ENGLISH);
        return format.format(Calendar.getInstance().getTime());
    }

    public static Integer getCurrentMonth() {
        Calendar currMonth = Calendar.getInstance();
        currMonth.add(Calendar.MONTH, 0);
        return currMonth.get(Calendar.MONTH);
    }

    public static Integer getCurrentYear() {
        Calendar currYear = Calendar.getInstance();
        currYear.add(Calendar.YEAR, 0);
        return currYear.get(Calendar.YEAR);
    }

    public static String getTitleDateFormat(String date) {

        SimpleDateFormat serverFormat = new SimpleDateFormat(SERVER_DATE_FORMATE, Locale.ENGLISH);
        SimpleDateFormat titleDateFormat = new SimpleDateFormat(TITLE_DATE_FORMATE, Locale.ENGLISH);

        try {
            return titleDateFormat.format(serverFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String extractDate(String date) {

        SimpleDateFormat serverFormat = new SimpleDateFormat(SERVER_DATE_FORMATE, Locale.ENGLISH);
        SimpleDateFormat titleDateFormat = new SimpleDateFormat(DATE_VALUE_FORMATE, Locale.ENGLISH);

        try {
            return titleDateFormat.format(serverFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String extractMonth(String date) {

        SimpleDateFormat serverFormat = new SimpleDateFormat(SERVER_DATE_FORMATE, Locale.ENGLISH);
        SimpleDateFormat titleDateFormat = new SimpleDateFormat(SEMI_MONTH_FORMATE, Locale.ENGLISH);

        try {
            return titleDateFormat.format(serverFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getEmpTimeLineFormat(String date) {

        SimpleDateFormat serverFormat = new SimpleDateFormat(SERVER_TIME_24HR_FORMATE, Locale.ENGLISH);
        SimpleDateFormat timelineFormat = new SimpleDateFormat(SERVER_TIME_FORMATE, Locale.ENGLISH);

        try {
            return timelineFormat.format(serverFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTitleDateFormatEmp(String date) {

        SimpleDateFormat serverFormat = new SimpleDateFormat(EMP_SERVER_DATE_FORMATE, Locale.ENGLISH);
        SimpleDateFormat titleDateFormat = new SimpleDateFormat(TITLE_DATE_FORMATE, Locale.ENGLISH);

        try {
            return titleDateFormat.format(serverFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getHistoryDetailsDateFormat(String date) {

        SimpleDateFormat serverFormat = new SimpleDateFormat(EMP_SERVER_DATE_FORMATE, Locale.ENGLISH);
        SimpleDateFormat titleDateFormat = new SimpleDateFormat(SERVER_DATE_FORMATE, Locale.ENGLISH);

        try {
            return titleDateFormat.format(serverFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String extractDateEmp(String date) {

        SimpleDateFormat serverFormat = new SimpleDateFormat(EMP_SERVER_DATE_FORMATE, Locale.ENGLISH);
        SimpleDateFormat titleDateFormat = new SimpleDateFormat(DATE_VALUE_FORMATE, Locale.ENGLISH);

        try {
            return titleDateFormat.format(serverFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String extractMonthEmp(String date) {

        SimpleDateFormat serverFormat = new SimpleDateFormat(EMP_SERVER_DATE_FORMATE, Locale.ENGLISH);
        SimpleDateFormat titleDateFormat = new SimpleDateFormat(SEMI_MONTH_FORMATE, Locale.ENGLISH);

        try {
            return titleDateFormat.format(serverFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void showWarning(Context context, String message) {
        Toasty.custom(context, message, R.drawable.ic_error_outline_white_48dp, Color.parseColor("#C8FE973C"), Toast.LENGTH_SHORT, true, true).show();
    }

    public static boolean isGPSEnable() {
        LocationManager manager = (LocationManager) mCurrentContext.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mApplication.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.d(TAG, service.toString());
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void showKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            InputMethodManager methodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            InputMethodManager methodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null) {
                methodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialog(Context context, @Nullable String Title, @Nullable String Message, DialogInterface.OnClickListener
            positiveListener, DialogInterface.OnClickListener negativeLisner){

        String positiveText = context.getResources().getString(R.string.yes_txt);
        String negativeText = context.getResources().getString(R.string.no_txt);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false).setPositiveButton(positiveText, positiveListener).setNegativeButton(negativeText, negativeLisner);

        if (!AUtils.isNull(Title)) {
            builder.setTitle(Title);
        }

        if (!AUtils.isNull(Message)) {
            builder.setMessage(Message);
        }

        builder.create().show();
    }

    public static void showDialog(Context context, @Nullable String Title, @Nullable String Message, DialogInterface.OnClickListener positiveListener) {

        String positiveText = context.getResources().getString(R.string.ok_txt);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!AUtils.isNull(Title)) {
            builder.setTitle(Title);
        }

        if (!AUtils.isNull(Message)) {
            builder.setMessage(Message);
        }

        builder.setCancelable(false).setPositiveButton(positiveText, positiveListener).create().show();
    }

    @RequiresApi(23)
    public static void changeBatteryOptimization(final Context context) {
        String packageName = context.getPackageName();
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            showDialog(context, null, "App require non optimized battery saver, \n Click 'OK' > Select All apps from dropdown > Find and click the app > Select 'Don't Optimise' > click 'DONE' > return to app", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    context.startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
                }
            });
        }
    }

    public static void hideSnackBar()
    {
        if(mSnackbar != null && mSnackbar.isShown())
        {
            mSnackbar.dismiss();

            if(!QuickUtils.prefs.getString(PREFS.USER_TYPE_ID,"0").equals("1")) {
                syncServer = new SyncServerAdapterClass();
                syncServer.syncServer();
            } else {
                empSyncServer = new EmpSyncServerAdapterClass();
                empSyncServer.syncServer();
            }
        }
    }

    public static void showSnackBar(Activity activity)
    {
        View view = activity.findViewById(R.id.parent);
        mSnackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout v = (Snackbar.SnackbarLayout) mSnackbar.getView();
        View layout = LayoutInflater.from(mCurrentContext).inflate(R.layout.snackbar_custom_layout, null);
        v.addView(layout, 0);
        mSnackbar.show();
    }

    public static int getBatteryStatus(){
        BatteryManager batteryManager = (BatteryManager) mCurrentContext.getSystemService(Context.BATTERY_SERVICE);
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public static boolean isInternetAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager)
                mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isConnectedFast(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && AUtils.isConnectionFast(info.getType(),info.getSubtype()));
    }

    public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }

    public static SQLiteDatabase sqlDBInstance(Context mContext){

        SbaDatabase databaseHelper = new SbaDatabase(mContext);

        return databaseHelper.getWritableDatabase();
    }
}

