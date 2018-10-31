package com.appynitty.swachbharatabhiyanlibrary.connection;

import android.content.Context;

import com.appynitty.retrofitconnectionlibrary.connection.Connection;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GarbageCollectionPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GcResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.ImagePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginDetailsPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.webservices.GarbageCollectionWebService;
import com.appynitty.swachbharatabhiyanlibrary.webservices.LoginWebService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import quickutils.core.QuickUtils;

public class SyncServer {

    private static final String TAG = "SyncServer";
    private static Gson gson;
    private Context context;

    public SyncServer(Context context) {

        this.context = context;
        gson = new Gson();
    }

    public LoginDetailsPojo saveLoginDetails(LoginPojo loginPojo) {

        LoginDetailsPojo resultPojo = null;
        try {

            LoginWebService service = Connection.createService(LoginWebService.class, AUtils.SERVER_URL);
            resultPojo = service.saveLoginDetails(QuickUtils.prefs.getString(AUtils.APP_ID, "1"),AUtils.CONTENT_TYPE,
                    loginPojo).execute().body();

        } catch (Exception e) {

            resultPojo = null;
            e.printStackTrace();
        }
        return resultPojo;
    }

    public GcResultPojo saveGarbageCollection(GarbageCollectionPojo garbageCollectionPojo) {

        GcResultPojo resultPojo = null;

        try {

            RequestBody requestBody1 = null;
            MultipartBody.Part imageFileMultiBody1 = null;

            if (!AUtils.isNull(garbageCollectionPojo.getImage1())) {
                File startImageFile = new File(garbageCollectionPojo.getImage1());
                requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), startImageFile);
                imageFileMultiBody1 = MultipartBody.Part.createFormData("vmImage1", startImageFile.getName(), requestBody1);
            }

            RequestBody requestBody2 = null;
            MultipartBody.Part imageFileMultiBody2 = null;

            if (!AUtils.isNull(garbageCollectionPojo.getImage2())) {
                File startImageFile = new File(garbageCollectionPojo.getImage2());
                requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), startImageFile);
                imageFileMultiBody2 = MultipartBody.Part.createFormData("vmImage1", startImageFile.getName(), requestBody2);
            }

            RequestBody id = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionPojo.getId());

            RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, QuickUtils.prefs.getString(AUtils.PREFS.USER_ID,""));
            RequestBody Lat = RequestBody.create(okhttp3.MultipartBody.FORM, QuickUtils.prefs.getString(AUtils.LAT, ""));
            RequestBody Long = RequestBody.create(okhttp3.MultipartBody.FORM, QuickUtils.prefs.getString(AUtils.LONG, ""));

            RequestBody comment = null;
            if(!AUtils.isNull(garbageCollectionPojo.getComment())){
                comment = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionPojo.getComment());
            }

            RequestBody beforeImage = null;
            if (!AUtils.isNullString(garbageCollectionPojo.getBeforeImage())) {
                beforeImage = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionPojo.getBeforeImage());
            }

            RequestBody afterImage = null;
            if (!AUtils.isNullString(garbageCollectionPojo.getAfterImage())) {
                afterImage = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionPojo.getAfterImage());
            }


            GarbageCollectionWebService service = Connection.createService(GarbageCollectionWebService.class, AUtils.SERVER_URL);

            int vehicleId = QuickUtils.prefs.getInt(AUtils.VEHICLE_ID, 1);

            if(vehicleId == 1){
                resultPojo = service.saveGarbageCollectionH(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                        userId, id, Lat, Long, beforeImage, afterImage, comment, imageFileMultiBody1,
                        imageFileMultiBody2).execute().body();
            }else if(vehicleId == 2){
                resultPojo = service.saveGarbageCollectionGP(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                        userId, id, Lat, Long, beforeImage, afterImage, comment, imageFileMultiBody1,
                        imageFileMultiBody2).execute().body();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return resultPojo;
    }

    public static boolean saveImage(ImagePojo imagePojo)
    {
        if (!AUtils.isNull(imagePojo)) {

            Type type = new TypeToken<ImagePojo>() {
            }.getType();
            QuickUtils.prefs.save(AUtils.PREFS.IMAGE_POJO + QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID), gson.toJson(imagePojo, type));

            return true;
        }
        else {
            return false;
        }
    }
}
