package com.appynitty.swachbharatabhiyanlibrary.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appynitty.swachbharatabhiyanlibrary.entity.EmpSyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.LastLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.SyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;
import com.appynitty.swachbharatabhiyanlibrary.repository.LastLocationRepository;
import com.appynitty.swachbharatabhiyanlibrary.repository.SyncOfflineAttendanceRepository;
import com.appynitty.swachbharatabhiyanlibrary.repository.SyncOfflineRepository;
import com.pixplicity.easyprefs.library.Prefs;

public class SbaDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    SbaDatabase(Context context) {
        super(context, AUtils.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(EmpSyncServerEntity.CREATE_TABLE);
        sqLiteDatabase.execSQL(UserLocationEntity.CREATE_TABLE);

//        sqLiteDatabase.execSQL(SyncServerEntity.CREATE_TABLE); //TODO
        sqLiteDatabase.execSQL(SyncOfflineRepository.CREATE_TABLE);
        sqLiteDatabase.execSQL(LastLocationRepository.CREATE_TABLE);
        sqLiteDatabase.execSQL(SyncOfflineAttendanceRepository.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int prevVersion, int newVersion) {
        if(prevVersion == 1 && newVersion == 2){
            update_1_2(sqLiteDatabase);
        }
    }

    private void update_1_2(SQLiteDatabase sqLiteDatabase){

        sqLiteDatabase.execSQL(EmpSyncServerEntity.CREATE_TEMP_TABLE);
        sqLiteDatabase.execSQL(UserLocationEntity.CREATE_TEMP_TABLE);
        sqLiteDatabase.execSQL(SyncServerEntity.CREATE_TEMP_TABLE);

        onCreate(sqLiteDatabase);

        if(Prefs.getString(AUtils.PREFS.USER_TYPE_ID, "0").equals("0")){
            SyncOfflineRepository.restoreData_1_2(sqLiteDatabase);
        }else{
            sqLiteDatabase.execSQL(EmpSyncServerEntity.RESTORE_TABLE);
            sqLiteDatabase.execSQL(UserLocationEntity.RESTORE_TABLE);
        }

        sqLiteDatabase.execSQL(EmpSyncServerEntity.DROP_TEMP_TABLE);
        sqLiteDatabase.execSQL(UserLocationEntity.DROP_TEMP_TABLE);
        sqLiteDatabase.execSQL(SyncServerEntity.DROP_TEMP_TABLE);
    }
}