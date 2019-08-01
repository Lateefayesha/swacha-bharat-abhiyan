package com.appynitty.swachbharatabhiyanlibrary.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.appynitty.swachbharatabhiyanlibrary.DAO.UserLocationDao;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.utils.SbaRoomDatabase;

import java.util.List;

public class LocationRepository {

    private UserLocationDao mUserLocationDao;
    private LiveData<List<UserLocationEntity>> mUserLocationEntityList;

    public LocationRepository(Application application) {
        SbaRoomDatabase db = SbaRoomDatabase.getDatabase(application);
        mUserLocationDao = db.userLocationDao();
        mUserLocationEntityList = mUserLocationDao.getAllLocations();
    }

    public LiveData<List<UserLocationEntity>> getUserLocationEntityList() {
        return mUserLocationEntityList;
    }

    public void deleteSelectedRecord (int index_id) {
        new DeleteSelectedRecordAsyncTask(mUserLocationDao).execute(index_id);
    }

    private class DeleteSelectedRecordAsyncTask extends AsyncTask<Integer, Void, Void> {

        private UserLocationDao mAsyncTaskDao;

        public DeleteSelectedRecordAsyncTask(UserLocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteSelectedRecord(params[0]);
            return null;
        }
    }

    public void insert (UserLocationEntity word) {
        new InsertAsyncTask(mUserLocationDao).execute(word);
    }

    private class InsertAsyncTask extends AsyncTask<UserLocationEntity, Void, Void> {

        private UserLocationDao mAsyncTaskDao;

        public InsertAsyncTask(UserLocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserLocationEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void updateSelectedRecord (UserLocationEntity word) {
        new UpdateSelectedRecordAsyncTask(mUserLocationDao).execute(word);
    }

    private class UpdateSelectedRecordAsyncTask extends AsyncTask<UserLocationEntity, Void, Void> {

        private UserLocationDao mAsyncTaskDao;

        public UpdateSelectedRecordAsyncTask(UserLocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserLocationEntity... params) {
            mAsyncTaskDao.updateSelectedRecord(params[0]);
            return null;
        }
    }

    public void deleteAllRecord () {
        new DeleteAllRecordAsyncTask(mUserLocationDao).execute();
    }

    private class DeleteAllRecordAsyncTask extends AsyncTask<UserLocationEntity, Void, Void> {

        private UserLocationDao mAsyncTaskDao;

        public DeleteAllRecordAsyncTask(UserLocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserLocationEntity... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

}