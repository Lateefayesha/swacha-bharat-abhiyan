package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.LoginAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.dialogs.PopUpDialog;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LanguagePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.riaylibrary.utils.CommonUtils;
import com.riaylibrary.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        if (AUtils.isInternetAvailable()) {
            AUtils.hideSnackBar();
        } else {
            AUtils.showSnackBar(findViewById(R.id.parent));
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

    private void initComponents() {
        generateId();
        registerEvents();
        initData();
    }

    protected void generateId() {

        getPermission();

        mContext = LoginActivity.this;
        AUtils.currentContextConstant = mContext;

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

                if (Prefs.getString(AUtils.LANGUAGE_NAME, AUtils.DEFAULT_LANGUAGE_ID).equals("2")) {
                    message = mAdapter.getLoginDetailsPojo().getMessageMar();
                } else {
                    message = mAdapter.getLoginDetailsPojo().getMessage();
                }

                Prefs.putString(AUtils.PREFS.USER_ID, mAdapter.getLoginDetailsPojo().getUserId());
                Prefs.putString(AUtils.PREFS.USER_TYPE, mAdapter.getLoginDetailsPojo().getType());
                Prefs.putString(AUtils.PREFS.USER_TYPE_ID, mAdapter.getLoginDetailsPojo().getTypeId());

                Prefs.putBoolean(AUtils.PREFS.IS_GT_FEATURE, (boolean) mAdapter.getLoginDetailsPojo().getGtFeatures());

                Prefs.putBoolean(AUtils.PREFS.IS_USER_LOGIN, true);

                AUtils.success(mContext, message, Toast.LENGTH_SHORT);
                Intent intent;

                if (mAdapter.getLoginDetailsPojo().getTypeId().equals("1"))
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

                if (Prefs.getString(AUtils.LANGUAGE_NAME, AUtils.DEFAULT_LANGUAGE_ID).equals("2")) {
                    message = mAdapter.getLoginDetailsPojo().getMessageMar();
                } else {
                    message = mAdapter.getLoginDetailsPojo().getMessage();
                }

                Prefs.putBoolean(AUtils.PREFS.IS_USER_LOGIN, false);

                AUtils.error(mContext, message, Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailureCallBack() {
                Prefs.putBoolean(AUtils.PREFS.IS_USER_LOGIN, false);
                AUtils.error(mContext, "" + mContext.getString(R.string.serverError), Toast.LENGTH_SHORT);
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
            AUtils.warning(mContext, mContext.getString(R.string.plz_ent_username));
            return false;
        }

        if (AUtils.isNullString(txtUserPwd.getText().toString())) {
            AUtils.warning(mContext, mContext.getString(R.string.plz_ent_pwd));
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
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

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
        changeLanguage(AUtils.setLanguage(languagePojo.getLanguage()));
    }

    private void changeLanguage() {
        HashMap<Integer, Object> mLanguage = new HashMap<>();

        List<LanguagePojo> mLanguagePojoList = AUtils.getLanguagePojoList();

        AUtils.changeLanguage(this, Prefs.getString(AUtils.LANGUAGE_NAME, AUtils.DEFAULT_LANGUAGE_ID));

        if (!AUtils.isNull(mLanguagePojoList) && !mLanguagePojoList.isEmpty()) {
            for (int i = 0; i < mLanguagePojoList.size(); i++) {
                mLanguage.put(i, mLanguagePojoList.get(i));
            }

            PopUpDialog dialog = new PopUpDialog(LoginActivity.this, AUtils.DIALOG_TYPE_LANGUAGE, mLanguage, this);
            dialog.show();
        }
    }

    public void changeLanguage(String type) {

        AUtils.changeLanguage(this, type);
        recreate();
    }
}
