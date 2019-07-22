package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class UserLocationPojo {

    private String OfflineId;

    private String userId;

    private String Long;

    private String datetime;

    private String lat;

    private int typeId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getOfflineId() {
        return OfflineId;
    }

    public void setOfflineId(String id) {
        this.OfflineId = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "UserLocationPojo{" +
                "id='" + OfflineId + '\'' +
                "userId='" + userId + '\'' +
                ", Long='" + Long + '\'' +
                ", datetime='" + datetime + '\'' +
                ", lat='" + lat + '\'' +
                ", typeId='" + typeId + '\'' +'}';
    }
}
