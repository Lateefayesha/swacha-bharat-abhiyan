package com.appynitty.swachbharatabhiyanlibrary.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.appynitty.swachbharatabhiyanlibrary.DAO.SyncServerDao;
import com.appynitty.swachbharatabhiyanlibrary.DAO.UserLocationDao;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.entity.SyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.utils.SbaRoomDatabase;

import java.util.List;

public class SyncServerRepository {

    private SyncServerDao mSyncServerDao;
    private LiveData<List<SyncServerEntity>> mSyncServerEntityList;

    public SyncServerRepository(Application application) {
        SbaRoomDatabase db = SbaRoomDatabase.getDatabase(application);
        mSyncServerDao = db.syncServerDao();
        mSyncServerEntityList = mSyncServerDao.getAllRecord();
    }

    public LiveData<List<SyncServerEntity>> getSysncServerEntityList() {
        return mSyncServerEntityList;
    }

    public void deleteSelectedRecord (int index_id) {
        new DeleteSelectedRecordAsyncTask(mSyncServerDao).execute(index_id);
    }

    private class DeleteSelectedRecordAsyncTask extends AsyncTask<Integer, Void, Void> {

        private SyncServerDao mAsyncTaskDao;

        public DeleteSelectedRecordAsyncTask(SyncServerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteSelectedRecord(params[0]);
            return null;
        }
    }

    public void insert (SyncServerEntity word) {
        new InsertAsyncTask(mSyncServerDao).execute(word);
    }

    private class InsertAsyncTask extends AsyncTask<SyncServerEntity, Void, Void> {

        private SyncServerDao mAsyncTaskDao;

        public InsertAsyncTask(SyncServerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SyncServerEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void updateSelectedRecord (SyncServerEntity word) {
        new UpdateSelectedRecordAsyncTask(mSyncServerDao).execute(word);
    }

    private class UpdateSelectedRecordAsyncTask extends AsyncTask<SyncServerEntity, Void, Void> {

        private SyncServerDao mAsyncTaskDao;

        public UpdateSelectedRecordAsyncTask(SyncServerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SyncServerEntity... params) {
            mAsyncTaskDao.updateSelectedRecord(params[0]);
            return null;
        }
    }

    public void deleteAllRecord () {
        new DeleteAllRecordAsyncTask(mSyncServerDao).execute();
    }

    private class DeleteAllRecordAsyncTask extends AsyncTask<UserLocationEntity, Void, Void> {

        private SyncServerDao mAsyncTaskDao;

        public DeleteAllRecordAsyncTask(SyncServerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserLocationEntity... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

}
