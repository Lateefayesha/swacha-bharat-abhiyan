package com.appynitty.swachbharatabhiyanlibrary.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appynitty.swachbharatabhiyanlibrary.entity.EmpSyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.ArrayList;
import java.util.List;

public class EmpSyncServerRepository {

    private Context mContext;
    private SQLiteDatabase database;

    public EmpSyncServerRepository(Context context) {
        mContext = context;
        database = AUtils.sqlDBInstance(context);
    }

    public void insertEmpSyncServerEntity(String pojo) {

        ContentValues values = new ContentValues();
        values.put(EmpSyncServerEntity.COLUMN_DATA, pojo);

        database.insert(AUtils.QR_TABLE_NAME, null, values);
        database.close();
    }

    public List<EmpSyncServerEntity> getAllEmpSyncServerEntity() {
        List<EmpSyncServerEntity> list = new ArrayList<>();

        String sql = "SELECT * FROM " + AUtils.QR_TABLE_NAME + " ORDER BY " + EmpSyncServerEntity.COLUMN_ID + " DESC";

        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                EmpSyncServerEntity empSyncServerEntity = new EmpSyncServerEntity();
                empSyncServerEntity.setIndex_id(cursor.getInt(cursor.getColumnIndex(EmpSyncServerEntity.COLUMN_ID)));
                empSyncServerEntity.setPojo(cursor.getString(cursor.getColumnIndex(EmpSyncServerEntity.COLUMN_DATA)));

                list.add(empSyncServerEntity);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return list;
    }

    public void deleteEmpSyncServerEntity(int id) {

        String whereClause = EmpSyncServerEntity.COLUMN_ID + "= ?";
        String[] args = new String[]{String.valueOf(id)};

        database.delete(AUtils.QR_TABLE_NAME, whereClause, args);
        database.close();
    }

    public void deleteAllEmpSyncServerEntity() {

        database.delete(AUtils.QR_TABLE_NAME, null, null);
        database.close();
    }
}
