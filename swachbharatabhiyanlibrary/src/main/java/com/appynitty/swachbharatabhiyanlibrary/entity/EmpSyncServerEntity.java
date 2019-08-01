package com.appynitty.swachbharatabhiyanlibrary.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

@Entity(tableName = AUtils.QR_TABLE_NAME)
public class EmpSyncServerEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int index_id;

    @ColumnInfo(name = "RefId")
    private String ref_id;

    @ColumnInfo(name = "gcType")
    private int gcType;

    @ColumnInfo(name = "Long")
    private String Long;

    @ColumnInfo(name = "Lat")
    private String Lat;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "nameMar")
    private String nameMar;

    @ColumnInfo(name = "address")
    private String Address;

    @ColumnInfo(name = "zoneId")
    private String zoneId;

    @ColumnInfo(name = "wardId")
    private String wardId;

    @ColumnInfo(name = "areaId")
    private String areaId;

    @ColumnInfo(name = "houseNumber")
    private String houseNumber;

    @ColumnInfo(name = "mobileno")
    private String mobileno;

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

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameMar() {
        return nameMar;
    }

    public void setNameMar(String nameMar) {
        this.nameMar = nameMar;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    @Override
    public String toString() {
        return "EmpSyncServerEntity{"
                + "index_id=" + index_id + '\''
                + ", ref_id='" + ref_id + '\''
                + ", gcType=" + gcType + '\''
                + ", Long='" + Long + '\''
                + ", Lat='" + Lat + '\''
                + ", date='" + date + '\''
                + ", name='" + name + '\''
                + ", nameMar='" + nameMar + '\''
                + ", Address='" + Address + '\''
                + ", zoneId='" + zoneId + '\''
                + ", wardId='" + wardId + '\''
                + ", areaId='" + areaId + '\''
                + ", houseNumber='" + houseNumber + '\''
                + ", mobileno='" + mobileno + '\'' + '}';
    }
}
