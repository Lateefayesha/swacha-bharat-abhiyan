package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class WorkHistoryPojo {

    private String houseCollection;

    private String date;

    private String pointCollection;

    public String getHouseCollection() {
        return houseCollection;
    }

    public void setHouseCollection(String houseCollection) {
        this.houseCollection = houseCollection;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPointCollection() {
        return pointCollection;
    }

    public void setPointCollection(String pointCollection) {
        this.pointCollection = pointCollection;
    }

    @Override
    public String toString() {
        return "WorkHistoryPojo{" +
                "houseCollection='" + houseCollection + '\'' +
                ", date='" + date + '\'' +
                ", pointCollection='" + pointCollection + '\'' +
                '}';
    }
}
