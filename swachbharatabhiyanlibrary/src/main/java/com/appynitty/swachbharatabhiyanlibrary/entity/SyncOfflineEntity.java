package com.appynitty.swachbharatabhiyanlibrary.entity;

import com.appynitty.swachbharatabhiyanlibrary.repository.SyncOfflineRepository;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

/**
 * Created by Ayan Dey on 17/9/19.
 * Contains getters and setters for offline sync of data from Ghanta Gadi Employee
 */
public class SyncOfflineEntity {

    private String offlineId;
    private String offlineData;
    private String offlineRefId;
    private String offlineGcType;
    private String offlineIsLocation;
    private String offlineDate;

    public String getOfflineId() {
        return offlineId;
    }

    public void setOfflineId(String offlineId) {
        this.offlineId = offlineId;
    }

    public String getOfflineData() {
        return offlineData;
    }

    public void setOfflineData(String offlineData) {
        this.offlineData = offlineData;
    }

    public String getOfflineRefId() {
        return offlineRefId;
    }

    public void setOfflineRefId(String offlineRefId) {
        this.offlineRefId = offlineRefId;
    }

    public String getOfflineGcType() {
        return offlineGcType;
    }

    public void setOfflineGcType(String offlineGcType) {
        this.offlineGcType = offlineGcType;
    }

    public String getOfflineIsLocation() {
        return offlineIsLocation;
    }

    public void setOfflineIsLocation(String offlineIsLocation) {
        this.offlineIsLocation = offlineIsLocation;
    }

    public String getOfflineDate() {
        return offlineDate;
    }

    public void setOfflineDate(String offlineDate) {
        this.offlineDate = offlineDate;
    }

    @Override
    public String toString() {
        return "SyncOfflineEntity{" +
                "offlineId='" + offlineId + '\'' +
                ", offlineData='" + offlineData + '\'' +
                ", offlineRefId='" + offlineRefId + '\'' +
                ", offlineGcType='" + offlineGcType + '\'' +
                ", offlineIsLocation='" + offlineIsLocation + '\'' +
                ", offlineDate='" + offlineDate + '\'' +
                '}';
    }
}
