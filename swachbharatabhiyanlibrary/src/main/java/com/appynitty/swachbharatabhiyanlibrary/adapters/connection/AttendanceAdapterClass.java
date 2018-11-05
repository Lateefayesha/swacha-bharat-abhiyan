package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.widget.Toast;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.activity.DashboardActivity;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.InPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.OutPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.mithsoft.lib.components.Toasty;

import quickutils.core.QuickUtils;

public class AttendanceAdapterClass {

    private AttendanceListener mListener;

    public AttendanceListener getAttendanceListener() {
        return mListener;
    }

    public void setAttendanceListener(AttendanceListener mListener) {
        this.mListener = mListener;
    }

    public void MarkInPunch(final InPunchPojo inPunchPojo) {

        new MyAsyncTask(AUtils.mCurrentContext, true, new MyAsyncTask.AsynTaskListener() {
            ResultPojo resultPojo = null;
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                if(!AUtils.isNull(inPunchPojo)) {
                    resultPojo = syncServer.saveInPunch(inPunchPojo);
                }
            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        QuickUtils.prefs.save(AUtils.PREFS.IS_ON_DUTY, true);
                        if(!AUtils.isNull(mListener))
                        {
                            mListener.onSuccessCallBack(1);
                        }

                        String message = null;
                        if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID,AUtils.DEFAULT_LANGUAGE_ID).equals(AUtils.DEFAULT_LANGUAGE_ID))
                        {
                            message = resultPojo.getMessageMar();
                        }
                        else
                        {
                            message = resultPojo.getMessage();
                        }
                        Toasty.success(AUtils.mCurrentContext, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        QuickUtils.prefs.save(AUtils.PREFS.IS_ON_DUTY, false);
                        if(!AUtils.isNull(mListener))
                        {
                            mListener.onFailureCallBack(1);
                        }
                        String message = null;
                        if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID,AUtils.DEFAULT_LANGUAGE_ID).equals(AUtils.DEFAULT_LANGUAGE_ID))
                        {
                            message = resultPojo.getMessageMar();
                        }
                        else
                        {
                            message = resultPojo.getMessage();
                        }
                        Toasty.error(AUtils.mCurrentContext, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    QuickUtils.prefs.save(AUtils.PREFS.IS_ON_DUTY, false);
                    if(!AUtils.isNull(mListener))
                    {
                        mListener.onFailureCallBack(1);
                    }
                    Toasty.error(AUtils.mCurrentContext, "" + AUtils.mCurrentContext.getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

    public void MarkOutPunch() {

        new MyAsyncTask(AUtils.mCurrentContext, true, new MyAsyncTask.AsynTaskListener() {
            ResultPojo resultPojo = null;
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                OutPunchPojo outPunchPojo = new OutPunchPojo();
                outPunchPojo.setDaDate(AUtils.getSeverDate());
                outPunchPojo.setEndTime(AUtils.getSeverTime());
                resultPojo = syncServer.saveOutPunch(outPunchPojo);

            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        QuickUtils.prefs.save(AUtils.PREFS.IS_ON_DUTY, false);
                        if(!AUtils.isNull(mListener))
                        {
                            mListener.onSuccessCallBack(2);
                        }
                        String message = null;
                        if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID,AUtils.DEFAULT_LANGUAGE_ID).equals(AUtils.DEFAULT_LANGUAGE_ID))
                        {
                            message = resultPojo.getMessageMar();
                        }
                        else
                        {
                            message = resultPojo.getMessage();
                        }

                        Toasty.success(AUtils.mCurrentContext, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        if(!AUtils.isNull(mListener))
                        {
                            mListener.onFailureCallBack(2);
                        }
                        String message = null;
                        if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID,AUtils.DEFAULT_LANGUAGE_ID).equals(AUtils.DEFAULT_LANGUAGE_ID))
                        {
                            message = resultPojo.getMessageMar();
                        }
                        else
                        {
                            message = resultPojo.getMessage();
                        }
                        Toasty.error(AUtils.mCurrentContext, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(!AUtils.isNull(mListener))
                    {
                        mListener.onFailureCallBack(2);
                    }
                    Toasty.error(AUtils.mCurrentContext, "" + AUtils.mCurrentContext.getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

    public interface AttendanceListener {
        void onSuccessCallBack(int type);
        void onFailureCallBack(int type);
    }
}
