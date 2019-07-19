package com.appynitty.swachbharatabhiyanlibrary.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.mithsoft.lib.components.MyProgressDialog;
import com.mithsoft.lib.components.Toasty;

public class MyAsyncTask extends AsyncTask {

    public SyncServer syncServer;
    private Context context;
    private boolean isNetworkAvail = false;
    private boolean isShowPrgressDialog;
    private MyProgressDialog myProgressDialog;
    private AsynTaskListener asynTaskListener;


    public MyAsyncTask(Context context, boolean isShowPrgressDialog, AsynTaskListener asynTaskListener) {

        this.asynTaskListener = asynTaskListener;
        this.context = context;
        this.syncServer = new SyncServer(context);
        this.isShowPrgressDialog = isShowPrgressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myProgressDialog = new MyProgressDialog(context, R.drawable.progress_bar, false);
        if (!AUtils.isNull(myProgressDialog) && isShowPrgressDialog) {
            myProgressDialog.show();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        if (AUtils.isNetWorkAvailable(context)) {
            try {

                isNetworkAvail = true;
                asynTaskListener.doInBackgroundOpration(syncServer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (!AUtils.isNull(myProgressDialog) && myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
        if (isNetworkAvail) {

            asynTaskListener.onFinished();
        } else {

            if (!AUtils.isNull(isShowPrgressDialog) && isShowPrgressDialog) {
                AUtils.showWarning(context, context.getString(R.string.noInternet));
                asynTaskListener.onInternetLost();
            }
        }
    }

    public interface AsynTaskListener {

        void doInBackgroundOpration(SyncServer syncServer);

        void onFinished();
        void onInternetLost();
    }
}