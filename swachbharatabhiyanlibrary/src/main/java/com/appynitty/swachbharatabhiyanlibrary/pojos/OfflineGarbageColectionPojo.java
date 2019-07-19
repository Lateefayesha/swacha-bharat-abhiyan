package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class OfflineGarbageColectionPojo {

    private String note;

    private String totalDryWeight;

    private String totalGcWeight;

    private String ReferenceID;

    private String garbageType;

    private String OfflineID;

    private String Long;

    private String gcType;

    private String vehicleNumber;

    private String totalWetWeight;

    private String userId;

    private String Lat;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTotalDryWeight() {
        return totalDryWeight;
    }

    public void setTotalDryWeight(String totalDryWeight) {
        this.totalDryWeight = totalDryWeight;
    }

    public String getTotalGcWeight() {
        return totalGcWeight;
    }

    public void setTotalGcWeight(String totalGcWeight) {
        this.totalGcWeight = totalGcWeight;
    }

    public String getReferenceID() {
        return ReferenceID;
    }

    public void setReferenceID(String referenceID) {
        ReferenceID = referenceID;
    }

    public String getGarbageType() {
        return garbageType;
    }

    public void setGarbageType(String garbageType) {
        this.garbageType = garbageType;
    }

    public String getOfflineID() {
        return OfflineID;
    }

    public void setOfflineID(String offlineID) {
        OfflineID = offlineID;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getGcType() {
        return gcType;
    }

    public void setGcType(String gcType) {
        this.gcType = gcType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getTotalWetWeight() {
        return totalWetWeight;
    }

    public void setTotalWetWeight(String totalWetWeight) {
        this.totalWetWeight = totalWetWeight;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    @Override
    public String toString() {
        return "OfflineGarbageColectionPojo{"
                + "note='" + note + '\''
                + ", totalDryWeight='" + totalDryWeight + '\''
                + ", totalGcWeight='" + totalGcWeight + '\''
                + ", ReferenceID='" + ReferenceID + '\''
                + ", garbageType='" + garbageType + '\''
                + ", OfflineID='" + OfflineID + '\''
                + ", Long='" + Long + '\''
                + ", gcType='" + gcType + '\''
                + ", vehicleNumber='" + vehicleNumber + '\''
                + ", totalWetWeight='" + totalWetWeight + '\''
                + ", userId='" + userId + '\''
                + ", Lat='" + Lat + '\'' + '}';
    }
}
