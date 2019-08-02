package com.appynitty.swachbharatabhiyanlibrary.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appynitty.swachbharatabhiyanlibrary.entity.EmpSyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.SyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.entity.UserLocationEntity;

import java.util.ArrayList;

public class SbaDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public SbaDatabase(Context context) {
        super(context, AUtils.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(EmpSyncServerEntity.CREATE_TABLE);
        sqLiteDatabase.execSQL(UserLocationEntity.CREATE_TABLE);
        sqLiteDatabase.execSQL(SyncServerEntity.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}