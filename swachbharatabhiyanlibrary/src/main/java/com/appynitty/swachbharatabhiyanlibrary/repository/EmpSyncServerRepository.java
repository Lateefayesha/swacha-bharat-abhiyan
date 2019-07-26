package com.appynitty.swachbharatabhiyanlibrary.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.appynitty.swachbharatabhiyanlibrary.DAO.EmpSyncServerDao;
import com.appynitty.swachbharatabhiyanlibrary.DAO.SyncServerDao;
import com.appynitty.swachbharatabhiyanlibrary.entity.EmpSyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.SyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.utils.SbaRoomDatabase;

import java.util.List;

public class EmpSyncServerRepository {

    private EmpSyncServerDao mEmpSyncServerDao;
    private LiveData<List<EmpSyncServerEntity>> mEmpSyncServerEntityList;

    public EmpSyncServerRepository(Application application) {
        SbaRoomDatabase db = SbaRoomDatabase.getDatabase(application);
        mEmpSyncServerDao = db.empSyncServerDao();
        mEmpSyncServerEntityList = mEmpSyncServerDao.getAllRecord();
    }

    public LiveData<List<EmpSyncServerEntity>> getSysncServerEntityList() {
        return mEmpSyncServerEntityList;
    }

    public void deleteSelectedRecord (int index_id) {
        new DeleteSelectedRecordAsyncTask(mEmpSyncServerDao).execute(index_id);
    }

    private class DeleteSelectedRecordAsyncTask extends AsyncTask<Integer, Void, Void> {

        private EmpSyncServerDao mAsyncTaskDao;

        public DeleteSelectedRecordAsyncTask(EmpSyncServerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteSelectedRecord(params[0]);
            return null;
        }
    }

    public void insert (EmpSyncServerEntity word) {
        new InsertAsyncTask(mEmpSyncServerDao).execute(word);
    }

    private class InsertAsyncTask extends AsyncTask<EmpSyncServerEntity, Void, Void> {

        private EmpSyncServerDao mAsyncTaskDao;

        public InsertAsyncTask(EmpSyncServerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EmpSyncServerEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void updateSelectedRecord (EmpSyncServerEntity word) {
        new UpdateSelectedRecordAsyncTask(mEmpSyncServerDao).execute(word);
    }

    private class UpdateSelectedRecordAsyncTask extends AsyncTask<EmpSyncServerEntity, Void, Void> {

        private EmpSyncServerDao mAsyncTaskDao;

        public UpdateSelectedRecordAsyncTask(EmpSyncServerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EmpSyncServerEntity... params) {
            mAsyncTaskDao.updateSelectedRecord(params[0]);
            return null;
        }
    }

    public void deleteAllRecord () {
        new DeleteAllRecordAsyncTask(mEmpSyncServerDao).execute();
    }

    private class DeleteAllRecordAsyncTask extends AsyncTask<EmpSyncServerEntity, Void, Void> {

        private EmpSyncServerDao mAsyncTaskDao;

        public DeleteAllRecordAsyncTask(EmpSyncServerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EmpSyncServerEntity... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

}
