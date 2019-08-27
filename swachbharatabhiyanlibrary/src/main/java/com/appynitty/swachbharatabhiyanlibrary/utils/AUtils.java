package com.appynitty.swachbharatabhiyanlibrary.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.EmpSyncServerAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.ShareLocationAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.SyncServerAdapterClass;
import com.pixplicity.easyprefs.library.Prefs;
import com.riaylibrary.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AUtils extends CommonUtils {

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

    public static final String CONTENT_TYPE = "application/json";

    public static final String APP_ID = "AppId";
    public static final String VERSION_CODE = "AppVersion";

    public static final String DIALOG_TYPE_VEHICLE = "DialogTypeVehicle";
    public static final String DIALOG_TYPE_LANGUAGE = "Dialog_Type_Language";

    public static final String DEFAULT_LANGUAGE_ID = LanguageConstants.ENGLISH;

    public static final int SPLASH_SCREEN_TIME = 3000;

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

    public static final String isFromLogin = "isFromLogin";
    public static final String dumpYardId = "dumpYardId";

    private static final String SERVER_DATE_FORMATE = "MM-dd-yyyy";
    private static final String EMP_SERVER_DATE_FORMATE = "dd-MM-yyyy";

    private static final String TITLE_DATE_FORMATE = "dd MMM yyyy";

    private static final String SEMI_MONTH_FORMATE = "MMM";
    private static final String DATE_VALUE_FORMATE = "dd";

    private static final String SERVER_TIME_FORMATE = "hh:mm a";
    private static final String SERVER_TIME_24HR_FORMATE = "HH:mm";

    private static final String SERVER_DATE_TIME_FORMATE = "MM-dd-yyyy HH:mm:ss";

    public static final long LOCATION_INTERVAL_MINUTES = 10 * 60 * 1000;

    public static final String VEHICLE_NO = "VehicleNumber";

    private static final String TAG = "AUtils";

    public static final String HP_AREA_TYPE_ID = "1";

    public static final String GP_AREA_TYPE_ID = "2";

    public static final String DY_AREA_TYPE_ID = "3";

    public final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public static final String TAG_HTTP_RESPONSE = "HTTPResponse_Error";
    public static final String ADD_DETAILS_TYPE_KEY = "ADD_DETAILS_TYPE_KEY";
    public static final int ADD_DETAILS_REQUEST_KEY = 8986;

    public static final String DATABASE_NAME = "db_sba_offline";
    public static final String LOCATION_TABLE_NAME = "table_location";
    public static final String COLLECTION_TABLE_NAME = "table_gcollection";
    public static final String QR_TABLE_NAME = "table_qr_emp";

    private static SyncServerAdapterClass syncServer;
    private static ShareLocationAdapterClass shareLocationAdapterClass;
    private static EmpSyncServerAdapterClass empSyncServer;

    public static Boolean isSyncServerRequestEnable = false;
    public static Boolean isLocationRequestEnable = false;
    public static Boolean isEmpSyncServerRequestEnable = false;

    public static boolean isIsOnduty() {
        return Prefs.getBoolean(PREFS.IS_ON_DUTY, false);
    }

    public static void setIsOnduty(boolean isOnduty) {
        Prefs.putBoolean(PREFS.IS_ON_DUTY, isOnduty);
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

    public static void saveLocation(Location location) {
        if (!AUtils.isNull(location)) {
            Double latti = location.getLatitude();
            Double longi = location.getLongitude();

            Prefs.putString(AUtils.LONG, longi.toString());
            Prefs.putString(AUtils.LAT, latti.toString());
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

    public static String getSeverDateTime() {

        SimpleDateFormat format = new SimpleDateFormat(AUtils.SERVER_DATE_TIME_FORMATE, Locale.ENGLISH);
        return format.format(Calendar.getInstance().getTime());
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

    public static void hideSnackBar()
    {
        if(mSnackbar != null && mSnackbar.isShown())
        {
            mSnackbar.dismiss();

            if(!Prefs.getString(PREFS.USER_TYPE_ID,"0").equals("1")) {
                syncServer = new SyncServerAdapterClass();
                syncServer.syncServer();
            } else {
                empSyncServer = new EmpSyncServerAdapterClass();
                empSyncServer.syncServer();
            }

            shareLocationAdapterClass = new ShareLocationAdapterClass();
            shareLocationAdapterClass.shareLocation();
        }
    }

    public static SQLiteDatabase sqlDBInstance(Context mContext){

        SbaDatabase databaseHelper = new SbaDatabase(mContext);

        return databaseHelper.getWritableDatabase();
    }
}

