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

    @ColumnInfo(name = "image2")
    private String image2;

    @ColumnInfo(name = "image1")
    private String image1;

    @ColumnInfo(name = "AfterImage")
    private String AfterImage;

    @ColumnInfo(name = "beforeImage")
    private String beforeImage;

    @ColumnInfo(name = "id")
    private String id;

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
    private double gcType;

    public int getIndex_id() {
        return index_id;
    }

    public void setIndex_id(int index_id) {
        this.index_id = index_id;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getAfterImage() {
        return AfterImage;
    }

    public void setAfterImage(String afterImage) {
        AfterImage = afterImage;
    }

    public String getBeforeImage() {
        return beforeImage;
    }

    public void setBeforeImage(String beforeImage) {
        this.beforeImage = beforeImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getGcType() {
        return gcType;
    }

    public void setGcType(double gcType) {
        this.gcType = gcType;
    }

    @Override
    public String toString() {
        return "SyncServerEntity{"
                + "index_id=" + index_id + '\''
                + ", image2='" + image2 + '\''
                + ", image1='" + image1 + '\''
                + ", AfterImage='" + AfterImage + '\''
                + ", beforeImage='" + beforeImage + '\''
                + ", id='" + id + '\''
                + ", comment='" + comment + '\''
                + ", garbageType=" + garbageType + '\''
                + ", weightTotal=" + weightTotal + '\''
                + ", weightTotalDry=" + weightTotalDry + '\''
                + ", weightTotalWet=" + weightTotalWet + '\''
                + ", gcType=" + gcType + '}';
    }
}
