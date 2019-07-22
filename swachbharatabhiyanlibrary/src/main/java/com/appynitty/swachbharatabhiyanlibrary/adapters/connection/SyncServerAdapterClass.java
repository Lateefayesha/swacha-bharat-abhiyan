package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.appynitty.retrofitconnectionlibrary.connection.Connection;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.entity.SyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OfflineGarbageColectionPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OfflineGcResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.view_model.SyncServerViewModel;
import com.appynitty.swachbharatabhiyanlibrary.webservices.GarbageCollectionWebService;
import com.appynitty.swachbharatabhiyanlibrary.webservices.UserLocationWebService;

import java.util.ArrayList;
import java.util.List;

import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncServerAdapterClass {

    private SyncServerViewModel mSyncServerViewModel;


    public SyncServerAdapterClass(){
        mSyncServerViewModel = ViewModelProviders.of((AppCompatActivity) AUtils.mCurrentContext)
                .get(SyncServerViewModel.class);
    }

    public void syncServer() {

        if (AUtils.syncGarbageCollectionPojoList.size() > 0) {

            GarbageCollectionWebService service = Connection.createService(GarbageCollectionWebService.class,
                    AUtils.SERVER_URL);

            service.saveGarbageCollectionOffline(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                    AUtils.getBatteryStatus(),
                    AUtils.CONTENT_TYPE,
                    AUtils.syncGarbageCollectionPojoList)
                    .enqueue(new Callback<List<OfflineGcResultPojo>>() {
                @Override
                public void onResponse(Call<List<OfflineGcResultPojo>> call, Response<List<OfflineGcResultPojo>> response) {

                    if (response.code() == 200) {
                        onResponseReceived(response.body());
                    } else {
                        Log.i(AUtils.TAG_HTTP_RESPONSE, "onFailureCallback: Response Code-" + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<OfflineGcResultPojo>> call, Throwable t) {
                    Log.i(AUtils.TAG_HTTP_RESPONSE, "onFailureCallback: Response Code-" + t.getMessage());
                }
            });
        }
    }

    public List<UserLocationResultPojo> saveUserLocation(List<UserLocationPojo> userLocationPojoList) {

        List<UserLocationResultPojo> resultPojo = null;
        try {

            UserLocationWebService service = Connection.createService(UserLocationWebService.class, AUtils.SERVER_URL);


            resultPojo = service.saveUserLocation(QuickUtils.prefs.getString(AUtils.APP_ID, "1"), AUtils.CONTENT_TYPE, QuickUtils.prefs.getString(AUtils.PREFS.USER_ID, null), AUtils.getBatteryStatus(), userLocationPojoList).execute().body();

        } catch (Exception e) {

            e.printStackTrace();
        }
        return resultPojo;
    }

    private void onResponseReceived(List<OfflineGcResultPojo> results) {

        if (!AUtils.isNull(results) && results.size() > 0) {

            for (OfflineGcResultPojo result : results) {

                if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                    if (Integer.parseInt(result.getID()) != 0) {
                        mSyncServerViewModel.deleteSelectedRecord(Integer.parseInt(result.getID()));
                    }
                    for (int i = 0; i < AUtils.UserLocationPojoList.size(); i++) {
                        if (AUtils.UserLocationPojoList.get(i).getOfflineId().equals(result.getID())) {
                            AUtils.UserLocationPojoList.remove(i);
                            break;
                        }
                    }

                } else {
                    for (UserLocationPojo pojo : AUtils.UserLocationPojoList) {
                        mSyncServerViewModel.deleteAllRecord();
                        break;
                    }
                }
            }
        }
    }
}
