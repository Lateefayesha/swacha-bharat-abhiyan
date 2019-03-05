package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GarbageCollectionPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GcResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.mithsoft.lib.components.Toasty;

public class GarbageCollectionAdapterClass {

    private GcResultPojo resultPojo;

    private GarbageCollectionListener mListener;

    public GarbageCollectionListener getGarbageCollectionListener() {
        return mListener;
    }

    public void setGarbageCollectionListener(GarbageCollectionListener mListener) {
        this.mListener = mListener;
    }

    public GcResultPojo getResultPojo() {
        return resultPojo;
    }

    private void setResultPojo(GcResultPojo resultPojo) {
        this.resultPojo = resultPojo;
    }

    public void submitQR(final GarbageCollectionPojo garbageCollectionPojo){

        new MyAsyncTask(AUtils.mCurrentContext, true, new MyAsyncTask.AsynTaskListener() {
            GcResultPojo resultPojo = null;
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                setResultPojo(syncServer.saveGarbageCollection(garbageCollectionPojo));
            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(getResultPojo())){
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

    public interface GarbageCollectionListener {
        void onSuccessCallBack();
        void onFailureCallBack();
    }
}
