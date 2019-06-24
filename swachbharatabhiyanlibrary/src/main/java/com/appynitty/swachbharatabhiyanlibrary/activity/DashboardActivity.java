package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.UI.InflateMenuAdapter;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.AttendanceAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.CheckAttendanceAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.UserDetailAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.VehicleTypeAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.custom_component.GlideCircleTransformation;
import com.appynitty.swachbharatabhiyanlibrary.dialogs.IdCardDialog;
import com.appynitty.swachbharatabhiyanlibrary.dialogs.PopUpDialog;
import com.appynitty.swachbharatabhiyanlibrary.pojos.InPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LanguagePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.MenuListPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.VehicleTypePojo;
import com.appynitty.swachbharatabhiyanlibrary.services.ForgroundService;
import com.appynitty.swachbharatabhiyanlibrary.services.LocationMonitoringService;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.components.Toasty;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import quickutils.core.QuickUtils;


public class DashboardActivity extends AppCompatActivity implements PopUpDialog.PopUpDialogListener {

    private final static String TAG = "DashboardActivity";

    private Context mContext;
    private FabSpeedDial fab;
    private GridView menuGridView;
    private Toolbar toolbar;
    private TextView attendanceStatus;
    private TextView vehicleStatus;
    private Switch markAttendance;
    private TextView userName;
    private TextView empId;
    private ImageView profilePic;

    private InPunchPojo inPunchPojo = null;

    private List<VehicleTypePojo> vehicleTypePojoList;

    private UserDetailPojo userDetailPojo;

    private boolean isLocationPermission = false;
    private boolean isSwitchOn = false;

    private boolean isFromLogin;

    private CheckAttendanceAdapterClass mCheckAttendanceAdapter;

    private AttendanceAdapterClass mAttendanceAdapter;

    private VehicleTypeAdapterClass mVehicleTypeAdapter;

    private UserDetailAdapterClass mUserDetailAdapter;

    private boolean isFromAttendanceChecked = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.attachBaseContext(LocaleHelper.onAttach(newBase));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_id_card == item.getItemId()) {
            if(!AUtils.isNull(userDetailPojo)) {
                if (QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID).equals(AUtils.DEFAULT_LANGUAGE_ID)) {
                    IdCardDialog cardDialog = new IdCardDialog(mContext, userDetailPojo.getNameMar(), userDetailPojo.getUserId(), userDetailPojo.getProfileImage());
                    cardDialog.show();
                } else {
                    IdCardDialog cardDialog = new IdCardDialog(mContext, userDetailPojo.getName(), userDetailPojo.getUserId(), userDetailPojo.getProfileImage());
                    cardDialog.show();
                }
            }else {
                AUtils.showWarning(mContext,mContext.getResources().getString(R.string.try_after_sometime));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AUtils.mCurrentContext = mContext;
        checkIsFromLogin();

        initUserDetails();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AUtils.MY_PERMISSIONS_REQUEST_LOCATION) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                isLocationPermission = allgranted;
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AUtils.showPermissionDialog(mContext, "Location Service", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, AUtils.MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }
                });
            } else {
                if (isSwitchOn) {
                    markAttendance.setChecked(false);
                }
            }

        }
    }

    @Override
    public void onPopUpDismissed(String type, Object listItemSelected, @Nullable String vehicleNo) {

        if (!AUtils.isNull(listItemSelected)) {
            switch (type) {
                case AUtils.DIALOG_TYPE_VEHICLE: {
                    onVehicleTypeDialogClose(listItemSelected, vehicleNo);
                }
                break;
                case AUtils.DIALOG_TYPE_LANGUAGE: {

                    onLanguageTypeDialogClose(listItemSelected);
                }
                break;
            }
        }
        else
        {
            switch (type) {
                case AUtils.DIALOG_TYPE_VEHICLE: {
                    if(AUtils.isIsOnduty())
                    {

                    }
                    else {
                        markAttendance.setChecked(false);
                        AUtils.mApplication.stopLocationTracking();
                    }
                }
                break;
                case AUtils.DIALOG_TYPE_LANGUAGE: {

                }
                break;
            }
        }
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

        if(!AUtils.isNull(mCheckAttendanceAdapter) && !isFromLogin)
        {
            mCheckAttendanceAdapter.checkAttendance();
        }
        checkDutyStatus();
    }

    private void initComponents() {

        getPermission();

        generateId();
        registerEvents();
        initData();
    }

    private void generateId() {

        setContentView(R.layout.activity_dashboard);

        mContext = DashboardActivity.this;
        AUtils.mCurrentContext = mContext;
        checkIsFromLogin();

        mCheckAttendanceAdapter = new CheckAttendanceAdapterClass();
        mAttendanceAdapter = new AttendanceAdapterClass();
        mVehicleTypeAdapter = new VehicleTypeAdapterClass();
        mUserDetailAdapter = new UserDetailAdapterClass();

        fab = findViewById(R.id.fab_setting);
        menuGridView = findViewById(R.id.menu_grid);
        toolbar = findViewById(R.id.toolbar);
        attendanceStatus = findViewById(R.id.user_attendance_status);
        vehicleStatus = findViewById(R.id.user_vehicle_type);
        markAttendance = findViewById(R.id.user_attendance_toggle);
        userName = findViewById(R.id.user_full_name);
        empId = findViewById(R.id.user_emp_id);
        profilePic = findViewById(R.id.user_profile_pic);

        initToolBar();
    }

    private void initToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_action_icon);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }

    private void registerEvents() {

        mCheckAttendanceAdapter.setCheckAttendanceListener(new CheckAttendanceAdapterClass.CheckAttendanceListener() {
            @Override
            public void onSuccessCallBack(boolean isAttendanceOff, String message, String messageMar) {

                
                if(isAttendanceOff)
                {
                    isFromAttendanceChecked = true;
                    onOutPunchSuccess();
                    if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID,AUtils.DEFAULT_LANGUAGE_ID).equals(AUtils.DEFAULT_LANGUAGE_ID)) {
                        Toasty.info(mContext, messageMar,Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toasty.info(mContext, message,Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailureCallBack() {
                if(!QuickUtils.prefs.getBoolean(AUtils.PREFS.IS_ON_DUTY,false))
                {
                    onInPunchSuccess();
                }
            }

            @Override
            public void onNetworkFailureCallBack() {
                Toasty.error(mContext,getResources().getString(R.string.serverError), Toast.LENGTH_LONG).show();
            }
        });

        mAttendanceAdapter.setAttendanceListener(new AttendanceAdapterClass.AttendanceListener() {
            @Override
            public void onSuccessCallBack(int type) {

                if (type == 1) {
                    onInPunchSuccess();
                } else if (type == 2) {
                    onOutPunchSuccess();
                }
            }

            @Override
            public void onFailureCallBack(int type) {

                if (type == 1) {
                    markAttendance.setChecked(false);
                    AUtils.setIsOnduty(false);
                } else if (type == 2) {
                    markAttendance.setChecked(true);
                    AUtils.setIsOnduty(true);
                }
            }
        });

        fab.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton miniFab, @Nullable TextView label, int itemId) {
                if (itemId == R.id.action_change_language) {
                    changeLanguage();
                } else if (itemId == R.id.action_setting) {
                    startActivity(new Intent(mContext, SettingsActivity.class));
                } else if (itemId == R.id.action_rate_app) {
                    AUtils.rateApp(mContext);
                } else if (itemId == R.id.action_share_app) {
                    AUtils.shareThisApp(mContext, null);
                } else if (itemId == R.id.action_logout) {
                    performLogout();
                }
            }
        });

        markAttendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onSwitchStatus(isChecked);
            }
        });

        menuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                setMenuClick(position);
            }
        });

        mUserDetailAdapter.setUserDetailListener(new UserDetailAdapterClass.UserDetailListener() {
            @Override
            public void onSuccessCallBack() {
                initUserDetails();
            }

            @Override
            public void onFailureCallBack() {

            }
        });

    }

    private void setMenuClick(int position) {

        switch (position) {
            case 0:
                if(AUtils.isIsOnduty())
                    startActivity(new Intent(mContext, QRcodeScannerActivity.class));
                else
                    AUtils.showWarning(mContext, getResources().getString(R.string.be_no_duty));
                break;
            case 1:
                if(AUtils.isIsOnduty())
                    startActivity(new Intent(mContext, TakePhotoActivity.class));
                else
                    AUtils.showWarning(mContext, getResources().getString(R.string.be_no_duty));
                break;
            case 2:
                if(AUtils.isIsOnduty())
                    startActivity(new Intent(mContext, BroadcastActivity.class));
                else
                    AUtils.showWarning(mContext, getResources().getString(R.string.be_no_duty));
                break;
            case 3:
                startActivity(new Intent(mContext, HistoryPageActivity.class));
                break;
            case 4:
                startActivity(new Intent(mContext, ProfilePageActivity.class));
                break;
        }
    }

    private void initData() {

        initUserDetails();

        mVehicleTypeAdapter.getVehicleType();
        mUserDetailAdapter.getUserDetail();

        List<MenuListPojo> menuPojoList = new ArrayList<MenuListPojo>();

        menuPojoList.add(new MenuListPojo(getResources().getString(R.string.title_activity_qrcode_scanner), R.drawable.ic_qr_code));
        menuPojoList.add(new MenuListPojo(getResources().getString(R.string.title_activity_take_photo), R.drawable.ic_photograph));

        menuPojoList.add(new MenuListPojo(getResources().getString(R.string.title_activity_broadcast_page), R.drawable.ic_broadcast_icon));
        menuPojoList.add(new MenuListPojo(getResources().getString(R.string.title_activity_history_page), R.drawable.ic_history));

        menuPojoList.add(new MenuListPojo(getResources().getString(R.string.title_activity_profile_page), R.drawable.ic_id_card));



        InflateMenuAdapter mainMenuAdaptor = new InflateMenuAdapter(DashboardActivity.this, menuPojoList);
        menuGridView.setAdapter(mainMenuAdaptor);

        Type type = new TypeToken<InPunchPojo>() {
        }.getType();
        inPunchPojo = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.PREFS.IN_PUNCH_POJO, null), type);

        checkDutyStatus();
    }

    private void performLogout() {
        AUtils.showConfirmationDialog(mContext, AUtils.CONFIRM_LOGOUT_DIALOG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if(!AUtils.isIsOnduty()){
                    QuickUtils.prefs.remove(AUtils.PREFS.IS_USER_LOGIN);
                    QuickUtils.prefs.remove(AUtils.PREFS.USER_ID);
                    QuickUtils.prefs.remove(AUtils.PREFS.USER_TYPE);
                    QuickUtils.prefs.remove(AUtils.PREFS.VEHICLE_TYPE_POJO_LIST);
                    QuickUtils.prefs.remove(AUtils.PREFS.USER_DETAIL_POJO);
                    //QuickUtils.prefs.remove(AUtils.PREFS.IS_ON_DUTY);
                    AUtils.setIsOnduty(false); //= false;
                    QuickUtils.prefs.remove(AUtils.PREFS.IMAGE_POJO);
                    QuickUtils.prefs.remove(AUtils.PREFS.WORK_HISTORY_DETAIL_POJO_LIST);
                    QuickUtils.prefs.remove(AUtils.PREFS.WORK_HISTORY_POJO_LIST);
                    QuickUtils.prefs.remove(AUtils.LAT);
                    QuickUtils.prefs.remove(AUtils.LONG);
                    QuickUtils.prefs.remove(AUtils.VEHICLE_NO);
                    QuickUtils.prefs.remove(AUtils.VEHICLE_ID);

                    openLogin();
                }else{
                    Toasty.info(mContext, getResources().getString(R.string.off_duty_warning)).show();
                }
            }
        }, null);
    }

    private void openLogin() {

        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        DashboardActivity.this.finish();
    }

    private void changeLanguage() {

        HashMap<Integer,Object> mLanguage = new HashMap<>();

        List<LanguagePojo> mLanguagePojoList = new ArrayList<>();

        LanguagePojo eng = new LanguagePojo();
        eng.setLanguage("English");
        eng.setLanguageId("1");

        LanguagePojo mar = new LanguagePojo();
        mar.setLanguageId("2");
        mar.setLanguage("मराठी");

        mLanguagePojoList.add(eng);
        mLanguagePojoList.add(mar);

        AUtils.changeLanguage(this, Integer.parseInt(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID)));

        if(!AUtils.isNull(mLanguagePojoList) && !mLanguagePojoList.isEmpty()) {
            for (int i = 0; i < mLanguagePojoList.size(); i++) {
                mLanguage.put(i, mLanguagePojoList.get(i));
            }

            PopUpDialog dialog = new PopUpDialog(DashboardActivity.this, AUtils.DIALOG_TYPE_LANGUAGE, mLanguage, this);
            dialog.show();
        }
    }

    public void changeLanguage(int type) {

        AUtils.changeLanguage(this, type);

        recreate();
    }

    private void onSwitchStatus(boolean isChecked) {

        isSwitchOn = isChecked;

        if (isChecked) {
            if (isLocationPermission) {
                if(AUtils.isGPSEnable()) {
                    HashMap<Integer, Object> mLanguage = new HashMap<>();

                    vehicleTypePojoList = mVehicleTypeAdapter.getVehicleTypePojoList();

                    if (!AUtils.isNull(vehicleTypePojoList) && !vehicleTypePojoList.isEmpty()) {
                        for (int i = 0; i < vehicleTypePojoList.size(); i++) {
                            mLanguage.put(i, vehicleTypePojoList.get(i));
                        }

                        if (!AUtils.isIsOnduty()) {
                            AUtils.mApplication.startLocationTracking();

                            PopUpDialog dialog = new PopUpDialog(DashboardActivity.this, AUtils.DIALOG_TYPE_VEHICLE, mLanguage, this);
                            dialog.show();
                        }
                    } else {
                        mVehicleTypeAdapter.getVehicleType();
                        markAttendance.setChecked(false);
                        Toasty.error(mContext, mContext.getString(R.string.something_error), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    markAttendance.setChecked(false);
                    AUtils.showGPSSettingsAlert(mContext);
                }
            } else {
                isLocationPermission = AUtils.isLocationPermissionGiven(DashboardActivity.this);
            }
        } else {
            if (AUtils.isIsOnduty()) {
                if (AUtils.isNetWorkAvailable(this)) {
                    try{
                        if(!isFromAttendanceChecked) {
                        AUtils.showConfirmationDialog(mContext, AUtils.CONFIRM_OFFDUTY_DIALOG, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                mAttendanceAdapter.MarkOutPunch();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                markAttendance.setChecked(true);
                            }
                        }); } else {
                            isFromAttendanceChecked = false;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        markAttendance.setChecked(true);
                    }
                } else {
                    AUtils.showWarning(mContext, mContext.getString(R.string.noInternet));
                    markAttendance.setChecked(true);
                }
            }
        }
    }

    private void onVehicleTypeDialogClose(Object listItemSelected, String vehicleNo) {

        if (AUtils.isNetWorkAvailable(this)) {

            if(!AUtils.isNull(vehicleNo) && !vehicleNo.isEmpty()){
                VehicleTypePojo vehicleTypePojo = (VehicleTypePojo) listItemSelected;

                QuickUtils.prefs.save(AUtils.VEHICLE_ID, vehicleTypePojo.getVtId());

                QuickUtils.prefs.save(AUtils.VEHICLE_NO, vehicleNo);

                if (!AUtils.isNull(inPunchPojo)) {
                    inPunchPojo.setDaDate(AUtils.getSeverDate());
                    inPunchPojo.setStartTime(AUtils.getSeverTime());

                    inPunchPojo.setVehicleNumber(vehicleNo);
                } else {
                    inPunchPojo = new InPunchPojo();

                    inPunchPojo.setDaDate(AUtils.getSeverDate());
                    inPunchPojo.setStartTime(AUtils.getSeverTime());

                    inPunchPojo.setVehicleNumber(vehicleNo);
                }

                try{
                    mAttendanceAdapter.MarkInPunch(inPunchPojo);
                }catch (Exception e){
                    e.printStackTrace();
                    markAttendance.setChecked(false);
                    Toasty.error(mContext, mContext.getString(R.string.something_error), Toast.LENGTH_SHORT).show();
                }
            }else{
                markAttendance.setChecked(false);
            }

        } else {
            AUtils.showWarning(mContext, mContext.getString(R.string.noInternet));
            markAttendance.setChecked(false);
        }
    }

    private void onInPunchSuccess() {
        attendanceStatus.setText(this.getResources().getString(R.string.status_on_duty));
        attendanceStatus.setTextColor(this.getResources().getColor(R.color.colorONDutyGreen));

        String vehicleType = null;

        for (int i = 0; i < vehicleTypePojoList.size(); i++) {
            if(QuickUtils.prefs.getString(AUtils.VEHICLE_ID,"0").equals(vehicleTypePojoList.get(i).getVtId()))
            {
                if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID,AUtils.DEFAULT_LANGUAGE_ID).equals(AUtils.DEFAULT_LANGUAGE_ID))
                    vehicleType = vehicleTypePojoList.get(i).getDescriptionMar();
                else
                    vehicleType = vehicleTypePojoList.get(i).getDescription();
            }
        }

        if (!AUtils.isNullString(inPunchPojo.getVehicleNumber())) {

            vehicleStatus.setText(String.format("%s%s %s %s%s", this.getResources().getString(R.string.opening_round_bracket), vehicleType,
                    this.getResources().getString(R.string.hyphen), inPunchPojo.getVehicleNumber(),
                    this.getResources().getString(R.string.closing_round_bracket)));
        } else {
            vehicleStatus.setText(String.format("%s%s%s", this.getResources().getString(R.string.opening_round_bracket),
                    vehicleType, this.getResources().getString(R.string.closing_round_bracket)));
        }

        AUtils.setIsOnduty(true);
    }

    private void onOutPunchSuccess(){
        attendanceStatus.setText(this.getResources().getString(R.string.status_off_duty));
        attendanceStatus.setTextColor(this.getResources().getColor(R.color.colorOFFDutyRed));

        vehicleStatus.setText("");

        boolean isservicerunning = AUtils.isMyServiceRunning(ForgroundService.class);

        if(isservicerunning)
            AUtils.mApplication.stopLocationTracking();

        markAttendance.setChecked(false);

        AUtils.setIsOnduty(false);
    }

    private void getPermission() {

        isLocationPermission = AUtils.isLocationPermissionGiven(DashboardActivity.this);
    }

    private void initUserDetails(){
        userDetailPojo = mUserDetailAdapter.getUserDetailPojo();

        if(!AUtils.isNull(userDetailPojo)){

            if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID).equals("2")){
                userName.setText(userDetailPojo.getNameMar());
            }else{
                userName.setText(userDetailPojo.getName());
            }

            empId.setText(userDetailPojo.getUserId());
            if (!AUtils.isNullString(userDetailPojo.getProfileImage())) {
                try {
                    Glide.with(mContext).load(userDetailPojo.getProfileImage())
                            .placeholder(R.drawable.ic_user)
                            .error(R.drawable.ic_user)
                            .centerCrop()
                            .bitmapTransform(new GlideCircleTransformation(getApplicationContext()))
                            .into(profilePic);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void onLanguageTypeDialogClose(Object listItemSelected) {

        LanguagePojo languagePojo = (LanguagePojo) listItemSelected;

        if (languagePojo.getLanguage().equals("English")) {
            changeLanguage(1);
        } else if (languagePojo.getLanguage().equals("मराठी")) {
            changeLanguage(2);
        }
    }

    private void checkDutyStatus() {

        if (AUtils.isIsOnduty()) {

            if(!AUtils.isMyServiceRunning(ForgroundService.class))
            {
                AUtils.mApplication.startLocationTracking();
            }
            markAttendance.setChecked(true);

            attendanceStatus.setText(this.getResources().getString(R.string.status_on_duty));
            attendanceStatus.setTextColor(this.getResources().getColor(R.color.colorONDutyGreen));

            String vehicleName = "";

            if(AUtils.isNull(vehicleTypePojoList))
            {
                vehicleTypePojoList = mVehicleTypeAdapter.getVehicleTypePojoList();
            }
            for (int i = 0; i < vehicleTypePojoList.size(); i++) {

                if(QuickUtils.prefs.getString(AUtils.VEHICLE_ID,"").equals(vehicleTypePojoList.get(i).getVtId()))
                {
                    if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID,AUtils.DEFAULT_LANGUAGE_ID).equals(AUtils.DEFAULT_LANGUAGE_ID))
                        vehicleName = vehicleTypePojoList.get(i).getDescriptionMar();
                    else
                        vehicleName = vehicleTypePojoList.get(i).getDescription();
                }
            }

            if (!AUtils.isNullString(QuickUtils.prefs.getString(AUtils.VEHICLE_NO,""))) {

                vehicleStatus.setText(String.format("%s%s %s %s%s", this.getResources().getString(R.string.opening_round_bracket), vehicleName, this.getResources().getString(R.string.hyphen), inPunchPojo.getVehicleNumber(), this.getResources().getString(R.string.closing_round_bracket)));
            } else {
                vehicleStatus.setText(String.format("%s%s%s", this.getResources().getString(R.string.opening_round_bracket), vehicleName, this.getResources().getString(R.string.closing_round_bracket)));
            }
        }
    }

    private void checkIsFromLogin(){

        if(getIntent().hasExtra(AUtils.isFromLogin)){
            isFromLogin = getIntent().getBooleanExtra(AUtils.isFromLogin, true);
        }else{
            isFromLogin = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getIntent().removeExtra(AUtils.isFromLogin);
    }
}
