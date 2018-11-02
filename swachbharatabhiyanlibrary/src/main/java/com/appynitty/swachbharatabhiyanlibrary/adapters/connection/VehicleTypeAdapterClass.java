package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.VehicleTypePojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import quickutils.core.QuickUtils;

public class VehicleTypeAdapterClass {

    private VehicleTypeListener mListener;

    private List<VehicleTypePojo> vehicleTypePojoList;

    private static Gson gson = new Gson();

    public VehicleTypeListener getVehicleTypeListener() {
        return mListener;
    }

    public void setVehicleTypeListener(VehicleTypeListener mListener) {
        this.mListener = mListener;
    }

    public List<VehicleTypePojo> getVehicleTypePojoList() {

        Type type = new TypeToken<List<VehicleTypePojo>>() {
        }.getType();

        vehicleTypePojoList = gson.fromJson(
                QuickUtils.prefs.getString(AUtils.PREFS.VEHICLE_TYPE_POJO_LIST, null), type);
        return vehicleTypePojoList;
    }

    public static void setVehicleTypePojoList(List<VehicleTypePojo> vehicleTypePojoList) {
        Type type = new TypeToken<List<VehicleTypePojo>>() {
        }.getType();
        QuickUtils.prefs.save(AUtils.PREFS.VEHICLE_TYPE_POJO_LIST, gson.toJson(vehicleTypePojoList, type));
    }

    public void getVehicleType() {

        new MyAsyncTask(AUtils.mCurrentContext, false, new MyAsyncTask.AsynTaskListener() {
            public boolean isDataPull = false;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                isDataPull = syncServer.pullVehicleTypeListFromServer();
            }

            @Override
            public void onFinished() {

                getVehicleTypePojoList();

                if (!AUtils.isNull(vehicleTypePojoList) && !vehicleTypePojoList.isEmpty())
                {
                    if(!AUtils.isNull(mListener))
                        mListener.onSuccessCallBack();
                }
                else
                {
                    if(!AUtils.isNull(mListener))
                        mListener.onFailureCallBack();
                }

            }
        }).execute();
    }

    public interface VehicleTypeListener {
        void onSuccessCallBack();
        void onFailureCallBack();
    }
}
