package com.appynitty.swachbharatabhiyanlibrary.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

@Entity(tableName = AUtils.LOCATION_TABLE_NAME)
public class UserLocationEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private int index_id;

    @ColumnInfo(name = "long")
    private String Long;

    @ColumnInfo(name = "date_time")
    private String datetime;

    @ColumnInfo(name = "lat")
    private String lat;

    public int getIndex_id() {
        return index_id;
    }

    public void setIndex_id(int index_id) {
        this.index_id = index_id;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "UserLocationPojo{" +
                "index_id='" + index_id + '\'' +
                ", Long='" + Long + '\'' +
                ", datetime='" + datetime + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
