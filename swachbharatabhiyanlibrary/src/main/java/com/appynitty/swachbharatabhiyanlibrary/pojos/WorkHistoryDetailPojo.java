package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class WorkHistoryDetailPojo {

    private String time;

    private String area;

    private String houseNumber;

    private String type;

    private String vehicleNumber;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    @Override
    public String toString() {
        return "WorkHistoryDetailPojo{" +
                "time='" + time + '\'' +
                ", area='" + area + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", type='" + type + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                '}';
    }
}
