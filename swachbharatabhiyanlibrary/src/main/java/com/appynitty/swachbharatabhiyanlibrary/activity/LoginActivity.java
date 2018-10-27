package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.content.Intent;
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
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.components.Toasty;

import quickutils.core.QuickUtils;

/**
 * Created by Richali Pradhan Gupte on 24-10-2018.
 */
public class LoginActivity extends BaseActivity {

    private Context mContext = null;

    private EditText txtUserName = null;
    private EditText txtUserPwd = null;

    private Button btnLogin = null;

    private LoginPojo loginPojo = null;

    @Override
    protected void generateId() {

        QuickUtils.prefs.save(AUtils.APP_ID,"1");
        mContext = LoginActivity.this;

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

                        if (loginDetailsPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                            QuickUtils.prefs.save(AUtils.USER_ID,loginDetailsPojo.getUserId());
                            QuickUtils.prefs.save(AUtils.TYPE,loginDetailsPojo.getType());

                            Toasty.success(mContext, "" + loginDetailsPojo.getMessage(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            LoginActivity.this.finish();
                        } else {
                            Toasty.error(mContext, "" + loginDetailsPojo.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toasty.error(mContext, "" + mContext.getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();
        }

    }

    private boolean validateForm() {

        if (AUtils.isNullString(txtUserName.getText().toString())) {
            Toasty.warning(mContext, mContext.getString(R.string.plz_ent_username), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (AUtils.isNullString(txtUserPwd.getText().toString())) {
            Toasty.warning(mContext, mContext.getString(R.string.plz_ent_pwd), Toast.LENGTH_SHORT).show();
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
}
