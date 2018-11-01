package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class UserLocationPojo {

    private String userId;

    private String Long;

    private String datetime;

    private String lat;

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

    @Override
    public String toString() {
        return "UserLocationPojo{" +
                "userId='" + userId + '\'' +
                ", Long='" + Long + '\'' +
                ", datetime='" + datetime + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
