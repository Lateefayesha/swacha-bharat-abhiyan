package com.appynitty.swachbharatabhiyanlibrary.entity;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.SbaDatabase;

import java.util.ArrayList;
import java.util.List;

public class EmpSyncServerEntity {

    public static final String COLUMN_ID = "_qrid";
    public static final String COLUMN_DATA = "qr_pojo";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + AUtils.QR_TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_DATA + " TEXT DEFAULT NULL)";

    private int index_id;

    private String pojo;

    public int getIndex_id() {
        return index_id;
    }

    public void setIndex_id(int index_id) {
        this.index_id = index_id;
    }

    public String getPojo() {
        return pojo;
    }

    public void setPojo(String pojo) {
        this.pojo = pojo;
    }

    @Override
    public String toString() {
        return "EmpSyncServerEntity{"
                + "index_id=" + index_id
                + ", pojo='" + pojo + '\'' + '}';
    }
}
