package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.LoginAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.dialogs.PopUpDialog;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LanguagePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginDetailsPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.mithsoft.lib.components.Toasty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import quickutils.core.QuickUtils;

/**
 * Created by Richali Pradhan Gupte on 24-10-2018.
 */
public class LoginActivity extends AppCompatActivity implements PopUpDialog.PopUpDialogListener {

    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private Context mContext = null;

    private EditText txtUserName = null;
    private EditText txtUserPwd = null;

    private Button btnLogin = null;
    private Button btnChangeLang = null;

    private LoginPojo loginPojo = null;

    private LoginAdapterClass mAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.attachBaseContext(LocaleHelper.onAttach(newBase));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:

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

                    // write your logic here
                } else {
                    Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),
                            "Please Grant Permissions to upload profile photo",
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(
                                                new String[]{Manifest.permission
                                                        .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                                PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }
                            }).show();
                }

                break;
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
    }

    @Override
    public void onPopUpDismissed(String type, Object listItemSelected, @Nullable String vehicleNo) {

        if (!AUtils.isNull(listItemSelected)) {
            switch (type) {
                case AUtils.DIALOG_TYPE_LANGUAGE: {

                    onLanguageTypeDialogClose(listItemSelected);
                }
                break;
            }
        }
    }

    private void initComponents(){
        generateId();
        registerEvents();
        initData();
    }

    protected void generateId() {

        getPermission();

        mContext = LoginActivity.this;
        AUtils.mCurrentContext = mContext;

        mAdapter = new LoginAdapterClass();

        setContentView(R.layout.activity_login_layout);

        txtUserName = findViewById(R.id.txt_user_name);
        txtUserPwd = findViewById(R.id.txt_password);

        btnLogin = findViewById(R.id.btn_login);
        btnChangeLang = findViewById(R.id.btn_change_lang);

    }

    protected void registerEvents() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });

        btnChangeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage();
            }
        });

        mAdapter.setLoginListener(new LoginAdapterClass.LoginListener() {
            @Override
            public void onSuccessCallBack() {
                String message = "";

                if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID).equals("2")){
                    message = mAdapter.getLoginDetailsPojo().getMessageMar();
                }else{
                    message = mAdapter.getLoginDetailsPojo().getMessage();
                }

                QuickUtils.prefs.save(AUtils.PREFS.USER_ID, mAdapter.getLoginDetailsPojo().getUserId());
                QuickUtils.prefs.save(AUtils.PREFS.USER_TYPE, mAdapter.getLoginDetailsPojo().getType());
                QuickUtils.prefs.save(AUtils.PREFS.USER_TYPE_ID, mAdapter.getLoginDetailsPojo().getTypeId());

                QuickUtils.prefs.save(AUtils.PREFS.IS_GT_FEATURE, (boolean)mAdapter.getLoginDetailsPojo().getGtFeatures());

                QuickUtils.prefs.save(AUtils.PREFS.IS_USER_LOGIN, true);

                Toasty.success(mContext, message, Toast.LENGTH_SHORT).show();
                Intent intent;

                if(mAdapter.getLoginDetailsPojo().getTypeId().equals("1"))
                    intent = new Intent(LoginActivity.this, EmpDashboardActivity.class);
                else
                    intent = new Intent(LoginActivity.this, DashboardActivity.class);

                intent.putExtra(AUtils.isFromLogin, true);
                startActivity(intent);
                LoginActivity.this.finish();
            }

            @Override
            public void onSuccessFailureCallBack() {
                String message = "";

                if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID).equals("2")){
                    message = mAdapter.getLoginDetailsPojo().getMessageMar();
                }else{
                    message = mAdapter.getLoginDetailsPojo().getMessage();
                }

                QuickUtils.prefs.save(AUtils.PREFS.IS_USER_LOGIN, false);

                Toasty.error(mContext, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailureCallBack() {
                QuickUtils.prefs.save(AUtils.PREFS.IS_USER_LOGIN, false);
                Toasty.error(mContext, "" + mContext.getString(R.string.serverError), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initData() {

    }

    private void onLogin() {

        if (validateForm()) {
            getFormData();

            mAdapter.onLogin(loginPojo);
        }

    }

    private boolean validateForm() {

        if (AUtils.isNullString(txtUserName.getText().toString())) {
            AUtils.showWarning(mContext, mContext.getString(R.string.plz_ent_username));
            return false;
        }

        if (AUtils.isNullString(txtUserPwd.getText().toString())) {
            AUtils.showWarning(mContext, mContext.getString(R.string.plz_ent_pwd));
            return false;
        }
        return true;
    }

    private void getFormData() {

        loginPojo = new LoginPojo();
        /*loginPojo.setMessage("");
        loginPojo.setStatus("");
        loginPojo.setType("");
        loginPojo.setUserId("");*/
        loginPojo.setUserLoginId(txtUserName.getText().toString());
        loginPojo.setUserPassword(txtUserPwd.getText().toString());
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        @SuppressLint("MissingPermission") String deviceid = telephonyManager.getDeviceId();

        loginPojo.setImiNo(deviceid);
    }

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ActivityCompat
                .checkSelfPermission(LoginActivity.this,
                        Manifest.permission.CAMERA) + ActivityCompat
                .checkSelfPermission(LoginActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) + ActivityCompat
                .checkSelfPermission(LoginActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) + ActivityCompat
                .checkSelfPermission(LoginActivity.this,
                        Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.READ_PHONE_STATE)) {

                Snackbar.make(LoginActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions to start using application",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.CAMERA,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.READ_PHONE_STATE},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                                }
                            }
                        }).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.READ_PHONE_STATE},
                            PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        } else {
            // write your logic code if permission already granted
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

    private void changeLanguage(){
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

            PopUpDialog dialog = new PopUpDialog(LoginActivity.this, AUtils.DIALOG_TYPE_LANGUAGE, mLanguage, this);
            dialog.show();
        }
    }

    public void changeLanguage(int type) {

        AUtils.changeLanguage(this, type);
        recreate();
    }
}
