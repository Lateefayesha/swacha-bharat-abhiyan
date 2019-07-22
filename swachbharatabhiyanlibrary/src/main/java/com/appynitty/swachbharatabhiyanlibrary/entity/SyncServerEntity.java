package com.appynitty.swachbharatabhiyanlibrary.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

@Entity(tableName = AUtils.COLLECTION_TABLE_NAME)
public class SyncServerEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private int index_id;

    @ColumnInfo(name = "RefId")
    private String ref_id;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "garbageType")
    private int garbageType;

    @ColumnInfo(name = "weightTotal")
    private double weightTotal;

    @ColumnInfo(name = "weightTotalDry")
    private double weightTotalDry;

    @ColumnInfo(name = "weightTotalWet")
    private double weightTotalWet;

    @ColumnInfo(name = "gcType")
    private int gcType;

    @ColumnInfo(name = "Long")
    private String Long;

    @ColumnInfo(name = "vhicleNumber")
    private String vehicleNumber;

    @ColumnInfo(name = "Lat")
    private String Lat;

    public int getIndex_id() {
        return index_id;
    }

    public void setIndex_id(int index_id) {
        this.index_id = index_id;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGarbageType() {
        return garbageType;
    }

    public void setGarbageType(int garbageType) {
        this.garbageType = garbageType;
    }

    public double getWeightTotal() {
        return weightTotal;
    }

    public void setWeightTotal(double weightTotal) {
        this.weightTotal = weightTotal;
    }

    public double getWeightTotalDry() {
        return weightTotalDry;
    }

    public void setWeightTotalDry(double weightTotalDry) {
        this.weightTotalDry = weightTotalDry;
    }

    public double getWeightTotalWet() {
        return weightTotalWet;
    }

    public void setWeightTotalWet(double weightTotalWet) {
        this.weightTotalWet = weightTotalWet;
    }

    public int getGcType() {
        return gcType;
    }

    public void setGcType(int gcType) {
        this.gcType = gcType;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    @Override
    public String toString() {
        return "SyncServerEntity{"
                + "index_id=" + index_id + '\''
                + ", ref_id='" + ref_id + '\''
                + ", comment='" + comment + '\''
                + ", garbageType=" + garbageType + '\''
                + ", weightTotal=" + weightTotal + '\''
                + ", weightTotalDry=" + weightTotalDry + '\''
                + ", weightTotalWet=" + weightTotalWet + '\''
                + ", gcType=" + gcType + '\''
                + ", Long='" + Long + '\''
                + ", vehicleNumber='" + vehicleNumber + '\''
                + ", Lat='" + Lat + '}';
    }
}
