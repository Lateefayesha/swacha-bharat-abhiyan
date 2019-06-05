package com.appynitty.swachbharatabhiyanlibrary.adapters.connection;

import android.widget.Toast;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.connection.EmpSyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.EmpInPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.EmpOutPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.EmpMyAsyncTask;
import com.mithsoft.lib.components.Toasty;

import quickutils.core.QuickUtils;

public class EmpAttendanceAdapterClass {

    private AttendanceListener mListener;

    public AttendanceListener getAttendanceListener() {
        return mListener;
    }

    public void setAttendanceListener(AttendanceListener mListener) {
        this.mListener = mListener;
    }

    public void MarkInPunch(final EmpInPunchPojo empInPunchPojo) {

        new EmpMyAsyncTask(AUtils.mCurrentContext, true, new EmpMyAsyncTask.AsynTaskListener() {
            ResultPojo resultPojo = null;
            @Override
            public void doInBackgroundOpration(EmpSyncServer empSyncServer) {

                if(!AUtils.isNull(empInPunchPojo)) {
                    try {
                        resultPojo = empSyncServer.saveInPunch(empInPunchPojo);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
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
                    if(!AUtils.isNull(mListener))
                    {
                        mListener.onFailureCallBack(1);
                    }
                    Toasty.error(AUtils.mCurrentContext, "" + AUtils.mCurrentContext.getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onInternetLost() {

            }
        }).execute();
    }

    public void MarkOutPunch() {

        new EmpMyAsyncTask(AUtils.mCurrentContext, true, new EmpMyAsyncTask.AsynTaskListener() {
            ResultPojo resultPojo = null;
            @Override
            public void doInBackgroundOpration(EmpSyncServer empSyncServer) {

                EmpOutPunchPojo empOutPunchPojo = new EmpOutPunchPojo();
                empOutPunchPojo.setEndDate(AUtils.getSeverDate());
                empOutPunchPojo.setEndTime(AUtils.getSeverTime());
                resultPojo = empSyncServer.saveOutPunch(empOutPunchPojo);

            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
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

            @Override
            public void onInternetLost() {

            }
        }).execute();
    }

    public interface AttendanceListener {
        void onSuccessCallBack(int type);
        void onFailureCallBack(int type);
    }
}
