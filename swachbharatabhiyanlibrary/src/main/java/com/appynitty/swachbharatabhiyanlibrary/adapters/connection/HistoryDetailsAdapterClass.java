package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.VehicleTypePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import quickutils.core.QuickUtils;

public class HistoryDetailsAdapterClass {

    private List<WorkHistoryDetailPojo> workHistoryDetailPojoList;

    private static Gson gson = new Gson();

    private HistoryDetailsListener mListener;

    public HistoryDetailsListener getHistoryDetailsListener() {
        return mListener;
    }

    public void setHistoryDetailsListener(HistoryDetailsListener mListener) {
        this.mListener = mListener;
    }

    public List<WorkHistoryDetailPojo> getWorkHistoryDetailTypePojoList() {

        Type type = new TypeToken<List<WorkHistoryDetailPojo>>() {
        }.getType();

        workHistoryDetailPojoList = gson.fromJson(
                QuickUtils.prefs.getString(AUtils.PREFS.WORK_HISTORY_DETAIL_POJO_LIST, null), type);
        return workHistoryDetailPojoList;
    }

    public static void setWorkHistoryDetailTypePojoList(List<WorkHistoryDetailPojo> workHistoryDetailPojoList) {
        Type type = new TypeToken<List<WorkHistoryDetailPojo>>() {
        }.getType();
        QuickUtils.prefs.save(AUtils.PREFS.WORK_HISTORY_DETAIL_POJO_LIST, gson.toJson(workHistoryDetailPojoList, type));
    }

    public void fetchHistoryDetails(final String historyDate) {
        new MyAsyncTask(AUtils.mCurrentContext, true, new MyAsyncTask.AsynTaskListener() {
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {
                Boolean isSuccess = syncServer.pullWorkHistoryDetailListFromServer(historyDate);
            }

            @Override
            public void onFinished() {

                if(!AUtils.isNull(getWorkHistoryDetailTypePojoList())){
                    mListener.onSuccessCallBack();
                }else{
                    mListener.onFailureCallBack();
                }
            }

            @Override
            public void onInternetLost() {

            }
        }).execute();
    }

    public interface HistoryDetailsListener {
        void onSuccessCallBack();

        void onFailureCallBack();
    }
}

