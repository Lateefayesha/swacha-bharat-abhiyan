package com.appynitty.swachbharatabhiyanlibrary.connection;

import android.content.Context;
import android.util.Log;

import com.appynitty.retrofitconnectionlibrary.connection.Connection;
import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
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


            LoginWebService service = Connection.createService(LoginWebService.class, AUtils.SERVER_URL);
            String appid = QuickUtils.prefs.getString(AUtils.APP_ID, "");
            resultPojo = service.saveLoginDetails(QuickUtils.prefs.getString(AUtils.APP_ID, ""), AUtils.CONTENT_TYPE,
                    loginPojo.getUserLoginId(), loginPojo.getUserPassword()).execute().body();


        } catch (Exception e) {

            e.printStackTrace();
        }
        return resultPojo;
    }
}
