package com.appynitty.swachbharatabhiyanlibrary.connection;

import android.content.Context;

import com.appynitty.retrofitconnectionlibrary.connection.Connection;
import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.UserDetailAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.VehicleTypeAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GarbageCollectionPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GcResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.ImagePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.InPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginDetailsPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LoginPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OutPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.VehicleTypePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.webservices.GarbageCollectionWebService;
import com.appynitty.swachbharatabhiyanlibrary.webservices.LoginWebService;
import com.appynitty.swachbharatabhiyanlibrary.webservices.PunchWebService;
import com.appynitty.swachbharatabhiyanlibrary.webservices.UserDetailsWebService;
import com.appynitty.swachbharatabhiyanlibrary.webservices.UserLocationWebService;
import com.appynitty.swachbharatabhiyanlibrary.webservices.VehicleTypeWebService;
import com.appynitty.swachbharatabhiyanlibrary.webservices.WorkHistoryWebService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

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
            String lat = QuickUtils.prefs.getString(AUtils.LAT, "");
            String LongStr = QuickUtils.prefs.getString(AUtils.LONG, "");
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

            int vehicleId = QuickUtils.prefs.getInt(AUtils.VEHICLE_ID, 0);

            if(vehicleId == 2){
                resultPojo = service.saveGarbageCollectionH(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                        userId, id, Lat, Long, beforeImage, afterImage, comment, imageFileMultiBody1,
                        imageFileMultiBody2).execute().body();
            }else if(vehicleId == 1){
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

    public ResultPojo saveInPunch(InPunchPojo inPunchPojo) {

        ResultPojo resultPojo = null;
        try {

            PunchWebService service = Connection.createService(PunchWebService.class, AUtils.SERVER_URL);

            inPunchPojo.setUserId(QuickUtils.prefs.getString(AUtils.PREFS.USER_ID,""));
            inPunchPojo.setVtId(String.valueOf(QuickUtils.prefs.getInt(AUtils.VEHICLE_ID, 0)));
            inPunchPojo.setStartLat(QuickUtils.prefs.getString(AUtils.LAT,""));
            inPunchPojo.setStartLong(QuickUtils.prefs.getString(AUtils.LONG,""));

            Type type = new TypeToken<InPunchPojo>() {
            }.getType();
            QuickUtils.prefs.save(AUtils.PREFS.IN_PUNCH_POJO, gson.toJson(inPunchPojo, type));

            resultPojo = service.saveInPunchDetails(QuickUtils.prefs.getString(AUtils.APP_ID, "1"), AUtils.CONTENT_TYPE,
                    inPunchPojo).execute().body();

        } catch (Exception e) {

            resultPojo = null;
            e.printStackTrace();
        }
        return resultPojo;
    }

    public ResultPojo saveOutPunch(OutPunchPojo outPunchPojo) {

        ResultPojo resultPojo = null;
        try {

            PunchWebService service = Connection.createService(PunchWebService.class, AUtils.SERVER_URL);

            outPunchPojo.setUserId(QuickUtils.prefs.getString(AUtils.PREFS.USER_ID,""));
            outPunchPojo.setEndLat(QuickUtils.prefs.getString(AUtils.LAT,""));
            outPunchPojo.setEndLong(QuickUtils.prefs.getString(AUtils.LONG,""));

            resultPojo = service.saveOutPunchDetails(QuickUtils.prefs.getString(AUtils.APP_ID, "1"), AUtils.CONTENT_TYPE,
                    outPunchPojo).execute().body();

        } catch (Exception e) {

            resultPojo = null;
            e.printStackTrace();
        }
        return resultPojo;
    }

    public boolean pullVehicleTypeListFromServer() {

        List<VehicleTypePojo> vehicleTypePojoList = null;

        try {

            VehicleTypeWebService service = Connection.createService(VehicleTypeWebService.class, AUtils.SERVER_URL);
            vehicleTypePojoList = service.pullVehicleTypeList(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                    AUtils.CONTENT_TYPE).execute().body();

            if (!AUtils.isNull(vehicleTypePojoList) && !vehicleTypePojoList.isEmpty()) {

                VehicleTypeAdapterClass.setVehicleTypePojoList(vehicleTypePojoList);

                return true;
            } else {

                QuickUtils.prefs.save(AUtils.PREFS.VEHICLE_TYPE_POJO_LIST, null);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    public boolean pullUserDetailsFromServer() {

        UserDetailPojo userDetailPojo = null;

        try {

            UserDetailsWebService service = Connection.createService(UserDetailsWebService.class, AUtils.SERVER_URL);
            userDetailPojo = service.pullUserDetails(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                    AUtils.CONTENT_TYPE, QuickUtils.prefs.getString(AUtils.PREFS.USER_ID,null)).execute().body();

            if (!AUtils.isNull(userDetailPojo)) {

                UserDetailAdapterClass.setUserDetailPojo(userDetailPojo);

                return true;
            } else {

                QuickUtils.prefs.save(AUtils.PREFS.USER_DETAIL_POJO, null);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    public boolean pullWorkHistoryListFromServer(String year, String month) {

        List<WorkHistoryPojo> workHistoryPojoList = null;

        try {

            WorkHistoryWebService service = Connection.createService(WorkHistoryWebService.class, AUtils.SERVER_URL);
            workHistoryPojoList = service.pullWorkHistoryList(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                    AUtils.CONTENT_TYPE, QuickUtils.prefs.getString(AUtils.PREFS.USER_ID,null), year, month).execute().body();

            if (!AUtils.isNull(workHistoryPojoList) && !workHistoryPojoList.isEmpty()) {

                Type type = new TypeToken<List<WorkHistoryPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.PREFS.WORK_HISTORY_POJO_LIST, gson.toJson(workHistoryPojoList, type));

                return true;
            } else {

                QuickUtils.prefs.save(AUtils.PREFS.WORK_HISTORY_POJO_LIST, null);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    public boolean pullWorkHistoryDetailListFromServer(String fDate, String languageId) {

        List<WorkHistoryDetailPojo> workHistoryDetailPojoList = null;

        try {

            WorkHistoryWebService service = Connection.createService(WorkHistoryWebService.class, AUtils.SERVER_URL);
            workHistoryDetailPojoList = service.pullWorkHistoryDetailList(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                    AUtils.CONTENT_TYPE, QuickUtils.prefs.getString(AUtils.PREFS.USER_ID,null), fDate, languageId).execute().body();

            if (!AUtils.isNull(workHistoryDetailPojoList) && !workHistoryDetailPojoList.isEmpty()) {

                Type type = new TypeToken<List<WorkHistoryDetailPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.PREFS.WORK_HISTORY_DETAIL_POJO_LIST, gson.toJson(workHistoryDetailPojoList, type));

                return true;
            } else {

                QuickUtils.prefs.save(AUtils.PREFS.WORK_HISTORY_DETAIL_POJO_LIST, null);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    public ResultPojo saveUserLocation() {

        ResultPojo resultPojo = null;
        try {

            UserLocationWebService service = Connection.createService(UserLocationWebService.class, AUtils.SERVER_URL);

            UserLocationPojo userLocationPojo = new UserLocationPojo();
            userLocationPojo.setUserId(QuickUtils.prefs.getString(AUtils.PREFS.USER_ID,""));
            userLocationPojo.setLat(QuickUtils.prefs.getString(AUtils.LAT,""));
            userLocationPojo.setLong(QuickUtils.prefs.getString(AUtils.LONG,""));
            userLocationPojo.setDatetime(AUtils.getSeverDateTime());

            resultPojo = service.saveUserLocation(QuickUtils.prefs.getString(AUtils.APP_ID, "1"), AUtils.CONTENT_TYPE,
                    QuickUtils.prefs.getString(AUtils.PREFS.USER_ID, null),userLocationPojo).execute().body();

        } catch (Exception e) {

            resultPojo = null;
            e.printStackTrace();
        }
        return resultPojo;
    }
}
