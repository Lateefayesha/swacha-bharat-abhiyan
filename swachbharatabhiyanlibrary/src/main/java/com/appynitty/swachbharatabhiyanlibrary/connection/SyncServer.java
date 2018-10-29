package com.appynitty.swachbharatabhiyanlibrary.connection;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.appynitty.retrofitconnectionlibrary.connection.Connection;
import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GarbageCollectionGarbagePointPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GarbageCollectionHousePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.ImagePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginDetailsPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.webservices.GarbageCollectionHouseWebService;
import com.appynitty.swachbharatabhiyanlibrary.webservices.LoginWebService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public ResultPojo saveGarbageCollectionHouse(GarbageCollectionHousePojo garbageCollectionHousePojo) {

        ResultPojo resultPojo = null;

        try {

            RequestBody requestBody1 = null;
            MultipartBody.Part imageFileMultiBody1 = null;

            if (!AUtils.isNull(garbageCollectionHousePojo.getImage1())) {
                File startImageFile = new File(garbageCollectionHousePojo.getImage1());
                requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), startImageFile);
                imageFileMultiBody1 = MultipartBody.Part.createFormData("vmImage1", startImageFile.getName(), requestBody1);
            }

            RequestBody requestBody2 = null;
            MultipartBody.Part imageFileMultiBody2 = null;

            if (!AUtils.isNull(garbageCollectionHousePojo.getImage2())) {
                File startImageFile = new File(garbageCollectionHousePojo.getImage2());
                requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), startImageFile);
                imageFileMultiBody2 = MultipartBody.Part.createFormData("vmImage1", startImageFile.getName(), requestBody2);
            }

            RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, QuickUtils.prefs.getString(AUtils.PREFS.USER_ID,""));
            RequestBody houseId = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionHousePojo.getHouseId());
            RequestBody Lat = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionHousePojo.getLat());
            RequestBody Long = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionHousePojo.getLong());

            RequestBody beforeImage = null;
            if (!AUtils.isNullString(garbageCollectionHousePojo.getBeforeImage())) {
                beforeImage = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionHousePojo.getBeforeImage());
            }

            RequestBody afterImage = null;
            if (!AUtils.isNullString(garbageCollectionHousePojo.getAfterImage())) {
                afterImage = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionHousePojo.getAfterImage());
            }


            GarbageCollectionHouseWebService service = Connection.createService(GarbageCollectionHouseWebService.class, AUtils.SERVER_URL);

            resultPojo = service.saveGarbageCollectionH(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                    userId, houseId, Lat, Long, beforeImage, afterImage, imageFileMultiBody1,
                    imageFileMultiBody2).execute().body();

        } catch (Exception e) {

            e.printStackTrace();
        }
        return resultPojo;
    }

    public ResultPojo saveGarbageCollectionGP(GarbageCollectionGarbagePointPojo garbageCollectionGarbagePointPojo) {

        ResultPojo resultPojo = null;

        try {

            RequestBody requestBody1 = null;
            MultipartBody.Part imageFileMultiBody1 = null;

            if (!AUtils.isNull(garbageCollectionGarbagePointPojo.getImage1())) {
                File startImageFile = new File(garbageCollectionGarbagePointPojo.getImage1());
                requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), startImageFile);
                imageFileMultiBody1 = MultipartBody.Part.createFormData("vmImage1", startImageFile.getName(), requestBody1);
            }

            RequestBody requestBody2 = null;
            MultipartBody.Part imageFileMultiBody2 = null;

            if (!AUtils.isNull(garbageCollectionGarbagePointPojo.getImage2())) {
                File startImageFile = new File(garbageCollectionGarbagePointPojo.getImage2());
                requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), startImageFile);
                imageFileMultiBody2 = MultipartBody.Part.createFormData("vmImage1", startImageFile.getName(), requestBody2);
            }

            RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, QuickUtils.prefs.getString(AUtils.PREFS.USER_ID,""));
            RequestBody gpId = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionGarbagePointPojo.getGpId());
            RequestBody Lat = RequestBody.create(okhttp3.MultipartBody.FORM, QuickUtils.prefs.getString(AUtils.LAT,""));
            RequestBody Long = RequestBody.create(okhttp3.MultipartBody.FORM, QuickUtils.prefs.getString(AUtils.LONG,""));

            RequestBody beforeImage = null;
            if (!AUtils.isNullString(garbageCollectionGarbagePointPojo.getBeforeImage())) {
                beforeImage = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionGarbagePointPojo.getBeforeImage());
            }

            RequestBody afterImage = null;
            if (!AUtils.isNullString(garbageCollectionGarbagePointPojo.getAfterImage())) {
                afterImage = RequestBody.create(okhttp3.MultipartBody.FORM, garbageCollectionGarbagePointPojo.getAfterImage());
            }


            GarbageCollectionHouseWebService service = Connection.createService(GarbageCollectionHouseWebService.class, AUtils.SERVER_URL);

            resultPojo = service.saveGarbageCollectionH(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                    userId, gpId, Lat, Long, beforeImage, afterImage, imageFileMultiBody1,
                    imageFileMultiBody2).execute().body();

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
