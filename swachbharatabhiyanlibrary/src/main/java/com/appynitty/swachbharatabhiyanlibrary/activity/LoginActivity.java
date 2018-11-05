package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginDetailsPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.components.Toasty;

import quickutils.core.QuickUtils;

/**
 * Created by Richali Pradhan Gupte on 24-10-2018.
 */
public class LoginActivity extends BaseActivity {

    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private Context mContext = null;

    private EditText txtUserName = null;
    private EditText txtUserPwd = null;

    private Button btnLogin = null;

    private LoginPojo loginPojo = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.attachBaseContext(LocaleHelper.onAttach(newBase));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    protected void generateId() {

        getPermission();

        QuickUtils.prefs.save(AUtils.APP_ID, "1");
        mContext = LoginActivity.this;
        AUtils.mCurrentContext = mContext;

        setContentView(R.layout.activity_login_layout);

        txtUserName = findViewById(R.id.txt_user_name);
        txtUserPwd = findViewById(R.id.txt_password);

        btnLogin = findViewById(R.id.btn_login);

    }

    @Override
    protected void registerEvents() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void onLogin() {

        if (validateForm()) {
            getFormData();

            new MyAsyncTask(mContext, true, new MyAsyncTask.AsynTaskListener() {
                public LoginDetailsPojo loginDetailsPojo = null;

                @Override
                public void doInBackgroundOpration(SyncServer syncServer) {

                    loginDetailsPojo = syncServer.saveLoginDetails(loginPojo);
                }

                @Override
                public void onFinished() {

                    if (!AUtils.isNull(loginDetailsPojo)) {

                        String message = "";

                        if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID).equals("2")){
                            message = loginDetailsPojo.getMessageMar();
                        }else{
                            message = loginDetailsPojo.getMessage();
                        }

                        if (loginDetailsPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                            QuickUtils.prefs.save(AUtils.PREFS.USER_ID, loginDetailsPojo.getUserId());
                            QuickUtils.prefs.save(AUtils.PREFS.USER_TYPE, loginDetailsPojo.getType());

                            QuickUtils.prefs.save(AUtils.PREFS.IS_USER_LOGIN, true);

                            Toasty.success(mContext, message, Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            LoginActivity.this.finish();
                        } else {
                            QuickUtils.prefs.save(AUtils.PREFS.IS_USER_LOGIN, false);

                            Toasty.error(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        QuickUtils.prefs.save(AUtils.PREFS.IS_USER_LOGIN, false);
                        Toasty.error(mContext, "" + mContext.getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();
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

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ActivityCompat
                .checkSelfPermission(LoginActivity.this,
                        Manifest.permission.CAMERA) + ActivityCompat
                .checkSelfPermission(LoginActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) + ActivityCompat
                .checkSelfPermission(LoginActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

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
                                                    Manifest.permission.ACCESS_FINE_LOCATION},
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
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        } else {
            // write your logic code if permission already granted
        }
    }
}
