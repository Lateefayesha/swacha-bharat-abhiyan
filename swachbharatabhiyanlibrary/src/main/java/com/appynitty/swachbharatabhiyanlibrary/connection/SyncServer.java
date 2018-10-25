package com.appynitty.swachbharatabhiyanlibrary.connection;

import android.content.Context;
import android.util.Log;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.retrofitconnectionlibrary.Connection.Connection;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.webservices.LoginWebService;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import quickutils.core.QuickUtils;

public class SyncServer {

    private static final String TAG = "SyncServer";
    private Gson gson;
    private Context context;

    public SyncServer(Context context) {

        this.context = context;
        gson = new Gson();
    }

    public ResultPojo saveLoginDetails(LoginPojo loginPojo) {

        ResultPojo resultPojo = null;

        try {

            RequestBody username = RequestBody.create(MediaType.parse(AUtils.CONTENT_TYPE), loginPojo.getUserLoginId());
            RequestBody userpwd = RequestBody.create(okhttp3.MultipartBody.FORM, loginPojo.getUserPassword());

            LoginWebService service = Connection.createService(LoginWebService.class, AUtils.SERVER_URL);
            resultPojo = service.saveLoginDetails(QuickUtils.prefs.getString(AUtils.APP_ID, ""), AUtils.CONTENT_TYPE,
                    username, userpwd).execute().body();


        } catch (Exception e) {

            e.printStackTrace();
        }
        return resultPojo;
    }
}
