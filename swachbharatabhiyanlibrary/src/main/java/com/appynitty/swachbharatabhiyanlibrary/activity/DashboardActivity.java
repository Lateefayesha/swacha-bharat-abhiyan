package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.InflateMenuAdapter;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.dialogs.PopUpDialog;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GarbageCollectionPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GcResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.ImagePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.InPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.MenuListPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OutPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.VehicleTypePojo;
import com.appynitty.swachbharatabhiyanlibrary.services.ForgroundService;
import com.appynitty.swachbharatabhiyanlibrary.services.LocationMonitoringService;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.components.Toasty;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    private InPunchPojo inPunchPojo = null;

    private boolean isOnDuty = false;

    private List<VehicleTypePojo> vehicleTypePojoList;

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

    private void initComponents() {
        generateId();
        registerEvents();
        initData();
    }

    private void generateId() {
        setContentView(R.layout.activity_dashboard);
        mContext = DashboardActivity.this;
        fab = findViewById(R.id.fab_setting);
        menuGridView = findViewById(R.id.menu_grid);
        toolbar = findViewById(R.id.toolbar);
        attendanceStatus = findViewById(R.id.user_attendance_status);
        vehicleStatus = findViewById(R.id.user_vehicle_type);
        markAttendance = findViewById(R.id.user_attendance_toggle);
        initToolBar();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
    }

    private void registerEvents() {

        fab.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton miniFab, @Nullable TextView label, int itemId) {
                if (itemId == R.id.action_change_language) {
                    changeLanguage();
                } else if (itemId == R.id.action_about_appynitty) {
                    startActivity(new Intent(mContext, AboutAppynittyActivity.class));
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

    }

    private void setMenuClick(int position){
        switch (position){
            case 0:
                startActivity(new Intent(mContext, QRcodeScannerActivity.class));
                break;
            case 1:
                startActivity(new Intent(mContext, TakePhotoActivity.class));
                break;
            case 2:
                startActivity(new Intent(mContext, HistoryPageActivity.class));
                break;
            case 3:
                startActivity(new Intent(mContext, ProfilePageActivity.class));
                break;
        }
    }

    private void initData() {
        List<MenuListPojo> menuPojoList = new ArrayList<MenuListPojo>();

        menuPojoList.add(new MenuListPojo("Scan QR code", R.drawable.ic_qr_code));
        menuPojoList.add(new MenuListPojo("Talking photo", R.drawable.ic_photograph));

        menuPojoList.add(new MenuListPojo("History", R.drawable.ic_history));
        menuPojoList.add(new MenuListPojo("Profile", R.drawable.ic_id_card));

        InflateMenuAdapter mainMenuAdaptor = new InflateMenuAdapter(DashboardActivity.this, menuPojoList);
        menuGridView.setAdapter(mainMenuAdaptor);

        Type type = new TypeToken<InPunchPojo>(){}.getType();
        inPunchPojo = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.PREFS.IN_PUNCH_POJO, null), type);

        getVehicleType();
    }

    private void performLogout() {

        QuickUtils.prefs.save(AUtils.PREFS.IS_USER_LOGIN, false);

        QuickUtils.prefs.save(AUtils.PREFS.USER_ID,"");
        QuickUtils.prefs.save(AUtils.PREFS.USER_TYPE,"");

        openLogin();

    }

    private void openLogin() {

        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        DashboardActivity.this.finish();
    }

    private void changeLanguage() {

        List<String> mLanguage = new ArrayList<>();
        mLanguage.add("English");
        mLanguage.add("मराठी");

        PopUpDialog dialog = new PopUpDialog(DashboardActivity.this, AUtils.DIALOG_TYPE_LANGUAGE, mLanguage, this);
        dialog.show();

    }

    @Override
    public void onPopUpDismissed(String type, String listItemSelected, @Nullable String vehicleNo) {

        if (!AUtils.isNullString(listItemSelected)){
            switch (type) {
                case AUtils.DIALOG_TYPE_VEHICLE: {
                    onVehicleTypeDialogClose(listItemSelected,vehicleNo);
                }
                break;
                case AUtils.DIALOG_TYPE_LANGUAGE: {
                    if (listItemSelected.equals("English")) {
                        changeLanguage(1);
                    } else if (listItemSelected.equals("मराठी")) {
                        changeLanguage(2);
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        isOnDuty = QuickUtils.prefs.getBoolean(AUtils.PREFS.IS_ON_DUTY,false);

        if(isOnDuty)
        {
            markAttendance.setChecked(true);

            attendanceStatus.setText(this.getResources().getString(R.string.status_on_duty));
            attendanceStatus.setTextColor(this.getResources().getColor(R.color.colorONDutyGreen));

            String vehicleName = "";

            switch (QuickUtils.prefs.getInt(AUtils.VEHICLE_ID,0))
            {
                case 1:
                    vehicleName = "Van";
                    break;
                case 2:
                    vehicleName = "Truck";
                    break;
            }

            if(!AUtils.isNullString(inPunchPojo.getVehicleNumber())) {

                vehicleStatus.setText(String.format("%s%s %s %s%s", this.getResources().getString(R.string.opening_round_bracket), vehicleName, this.getResources().getString(R.string.hyphen), inPunchPojo.getVehicleNumber(), this.getResources().getString(R.string.closing_round_bracket)));
            } else {
                vehicleStatus.setText(String.format("%s%s%s", this.getResources().getString(R.string.opening_round_bracket), vehicleName, this.getResources().getString(R.string.closing_round_bracket)));
            }
        }
    }

    public void changeLanguage(int type) {

        AUtils.changeLanguage(this, type);


        initComponents();
    }

    private void onSwitchStatus(boolean isChecked) {

        if (isChecked) {
            List<String> mLanguage = new ArrayList<>();
            mLanguage.add("Van");
            mLanguage.add("Truck");

            if(!isOnDuty) {
                AUtils.mApplication.startLocationTracking();

                PopUpDialog dialog = new PopUpDialog(DashboardActivity.this, AUtils.DIALOG_TYPE_VEHICLE, mLanguage, this);
                dialog.show();
            }
        } else {
            if(isOnDuty) {
                attendanceStatus.setText(this.getResources().getString(R.string.status_off_duty));
                attendanceStatus.setTextColor(this.getResources().getColor(R.color.colorOFFDutyRed));

                vehicleStatus.setText("");

                AUtils.mApplication.stopLocationTracking();

                MarkOutPunch();
            }
        }
    }


    private void onVehicleTypeDialogClose(String listItemSelected, String vehicleNo)
    {
        attendanceStatus.setText(this.getResources().getString(R.string.status_on_duty));
        attendanceStatus.setTextColor(this.getResources().getColor(R.color.colorONDutyGreen));

        if(!AUtils.isNullString(vehicleNo)) {

            vehicleStatus.setText(String.format("%s%s %s %s%s", this.getResources().getString(R.string.opening_round_bracket), listItemSelected, this.getResources().getString(R.string.hyphen), vehicleNo, this.getResources().getString(R.string.closing_round_bracket)));
        } else {
            vehicleStatus.setText(String.format("%s%s%s", this.getResources().getString(R.string.opening_round_bracket), listItemSelected, this.getResources().getString(R.string.closing_round_bracket)));
        }

        switch (listItemSelected)
        {
            case "Van":
                QuickUtils.prefs.save(AUtils.VEHICLE_ID,1);
                break;

            case "Truck":
                QuickUtils.prefs.save(AUtils.VEHICLE_ID,2);
                break;
        }

        if(!AUtils.isNull(inPunchPojo))
        {
            inPunchPojo.setDaDate(AUtils.getSeverDate());
            inPunchPojo.setStartTime(AUtils.getSeverTime());

            inPunchPojo.setVehicleNumber(vehicleNo);
        } else {
            inPunchPojo = new InPunchPojo();

            inPunchPojo.setDaDate(AUtils.getSeverDate());
            inPunchPojo.setStartTime(AUtils.getSeverTime());

            inPunchPojo.setVehicleNumber(vehicleNo);
        }

        MarkInPunch();
    }

    private void MarkInPunch() {

        new MyAsyncTask(mContext, true, new MyAsyncTask.AsynTaskListener() {
            ResultPojo resultPojo = null;
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                if(!AUtils.isNull(inPunchPojo)) {
                    resultPojo = syncServer.saveInPunch(inPunchPojo);
                }
            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        QuickUtils.prefs.save(AUtils.PREFS.IS_ON_DUTY, true);
                        Toasty.success(mContext, "" + resultPojo.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        QuickUtils.prefs.save(AUtils.PREFS.IS_ON_DUTY, false);
                        markAttendance.setChecked(false);
                        Toasty.error(mContext, "" + resultPojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    QuickUtils.prefs.save(AUtils.PREFS.IS_ON_DUTY, false);
                    markAttendance.setChecked(false);
                    Toasty.error(mContext, "" + mContext.getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

    private void MarkOutPunch() {

        new MyAsyncTask(mContext, true, new MyAsyncTask.AsynTaskListener() {
            ResultPojo resultPojo = null;
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                OutPunchPojo outPunchPojo = new OutPunchPojo();
                outPunchPojo.setDaDate(AUtils.getSeverDate());
                outPunchPojo.setEndTime(AUtils.getSeverTime());
                    resultPojo = syncServer.saveOutPunch(outPunchPojo);

            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        QuickUtils.prefs.save(AUtils.PREFS.IS_ON_DUTY, false);
                        markAttendance.setChecked(false);
                        Toasty.success(mContext, "" + resultPojo.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.error(mContext, "" + resultPojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(mContext, "" + mContext.getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

    private void getVehicleType() {

        new MyAsyncTask(mContext, false, new MyAsyncTask.AsynTaskListener() {
            public boolean isDataPull = false;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                isDataPull = syncServer.pullVehicleTypeListFromServer();
            }

            @Override
            public void onFinished() {

                Type type = new TypeToken<List<VehicleTypePojo>>() {
                }.getType();

                vehicleTypePojoList = new Gson().fromJson(
                        QuickUtils.prefs.getString(AUtils.PREFS.VEHICLE_TYPE_POJO_LIST, null), type);

            }
        }).execute();
    }
}
