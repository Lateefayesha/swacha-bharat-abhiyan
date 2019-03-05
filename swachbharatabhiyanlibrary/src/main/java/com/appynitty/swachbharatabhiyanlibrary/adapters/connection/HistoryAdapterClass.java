package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.view.View;

import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import quickutils.core.QuickUtils;

public class HistoryAdapterClass {

    private List<WorkHistoryPojo> workHistoryPojoList;

    private static Gson gson = new Gson();

    private HistoryListener mListener;

    public HistoryListener getHistoryListener() {
        return mListener;
    }

    public void setHistoryListener(HistoryListener mListener) {
        this.mListener = mListener;
    }

    public List<WorkHistoryPojo> getworkHistoryTypePojoList() {

        Type type = new TypeToken<List<WorkHistoryPojo>>() {
        }.getType();

        workHistoryPojoList = gson.fromJson(QuickUtils.prefs.getString(AUtils.PREFS.WORK_HISTORY_POJO_LIST, null), type);
        return workHistoryPojoList;
    }

    public static void setworkHistoryTypePojoList(List<WorkHistoryPojo> workHistoryDetailPojoList) {
        Type type = new TypeToken<List<WorkHistoryPojo>>() {
        }.getType();
        QuickUtils.prefs.save(AUtils.PREFS.WORK_HISTORY_POJO_LIST, gson.toJson(workHistoryDetailPojoList, type));
    }

    public void fetchHistory(final String year, final String month) {
        new MyAsyncTask(AUtils.mCurrentContext, true, new MyAsyncTask.AsynTaskListener() {
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {
                Boolean isSuccess = syncServer.pullWorkHistoryListFromServer(year, month);
            }

            @Override
            public void onFinished() {

                if(!AUtils.isNull(getworkHistoryTypePojoList()) && !getworkHistoryTypePojoList().isEmpty()){
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

    public interface HistoryListener {
        void onSuccessCallBack();

        void onFailureCallBack();
    }
}
