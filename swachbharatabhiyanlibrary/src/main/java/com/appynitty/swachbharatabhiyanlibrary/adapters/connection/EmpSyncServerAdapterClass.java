package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.appynitty.retrofitconnectionlibrary.connection.Connection;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OfflineGcResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserLocationResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.view_model.EmpSyncServerViewModel;
import com.appynitty.swachbharatabhiyanlibrary.webservices.QrLocationWebService;
import com.appynitty.swachbharatabhiyanlibrary.webservices.UserLocationWebService;

import java.util.List;

import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ayan Dey on 28/5/19.
 */
public class EmpSyncServerAdapterClass {

    private EmpSyncServerViewModel empSyncServerViewModel;


    public EmpSyncServerAdapterClass(){
        empSyncServerViewModel = ViewModelProviders.of((AppCompatActivity) AUtils.mCurrentContext)
                .get(EmpSyncServerViewModel.class);
    }

    public void syncServer() {

        if (AUtils.qrLocationPojoList.size() > 0) {

            QrLocationWebService service = Connection.createService(QrLocationWebService.class,
                    AUtils.SERVER_URL);

            service.saveQrLocationDetailsOffline(QuickUtils.prefs.getString(AUtils.APP_ID, ""),
                    AUtils.CONTENT_TYPE,
                    AUtils.qrLocationPojoList)
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

    private void onResponseReceived(List<OfflineGcResultPojo> results) {

        if (!AUtils.isNull(results) && results.size() > 0) {

            for (OfflineGcResultPojo result : results) {

                if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                    if (Integer.parseInt(result.getID()) != 0) {
                        empSyncServerViewModel.deleteSelectedRecord(Integer.parseInt(result.getID()));
                    }
                    for (int i = 0; i < AUtils.qrLocationPojoList.size(); i++) {
                        if (AUtils.qrLocationPojoList.get(i).getOfflineID().equals(result.getID())) {
                            AUtils.qrLocationPojoList.remove(i);
                            break;
                        }
                    }
                } else {
                    empSyncServerViewModel.deleteAllRecord();
                }
            }
        }
    }
}
