package com.appynitty.swachbharatabhiyanlibrary.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.appynitty.swachbharatabhiyanlibrary.DAO.EmpSyncServerDao;
import com.appynitty.swachbharatabhiyanlibrary.DAO.SyncServerDao;
import com.appynitty.swachbharatabhiyanlibrary.DAO.UserLocationDao;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.entity.EmpSyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.SyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;

import quickutils.core.QuickUtils;

@Database(entities = {UserLocationEntity.class, SyncServerEntity.class, EmpSyncServerEntity.class}, version = 2, exportSchema = false)
public abstract class SbaRoomDatabase extends RoomDatabase {
    public abstract UserLocationDao userLocationDao();
    public abstract SyncServerDao syncServerDao();
    public abstract EmpSyncServerDao empSyncServerDao();

    private static volatile SbaRoomDatabase INSTANCE;

    public static SbaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SbaRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SbaRoomDatabase.class, AUtils.DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //Logic of marging Database

//            database.execSQL("ALTER TABLE "+AUtils.LOCATION_TABLE_NAME+" RENAME TO locTemp");
//            database.execSQL("ALTER TABLE "+AUtils.COLLECTION_TABLE_NAME+" RENAME TO gcTemp");
//            new PopulateDbAsync(INSTANCE).execute();
            new PopulateDb2Async(INSTANCE).execute();

//            database.execSQL("ALTER TABLE locTemp RENAME TO "+AUtils.LOCATION_TABLE_NAME);
//            database.execSQL("ALTER TABLE gcTemp RENAME TO "+AUtils.COLLECTION_TABLE_NAME);
        }
    };

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final UserLocationDao mDao;
        private final SyncServerDao mSyncDao;
//        private final EmpSyncServerDao mEmpSyncDao;

        PopulateDbAsync(SbaRoomDatabase db) {
            mDao = db.userLocationDao();
            mSyncDao = db.syncServerDao();
//            mEmpSyncDao = db.empSyncServerDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.

            return null;
        }
    }

    private static class PopulateDb2Async extends AsyncTask<Void, Void, Void> {

        private final EmpSyncServerDao mEmpSyncDao;

        PopulateDb2Async(SbaRoomDatabase db) {
            mEmpSyncDao = db.empSyncServerDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.

            return null;
        }
    }
}
