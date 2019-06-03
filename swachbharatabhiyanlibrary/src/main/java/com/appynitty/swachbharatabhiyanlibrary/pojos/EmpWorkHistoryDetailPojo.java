package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class EmpWorkHistoryDetailPojo {

    private String time;

    private String Date;

    private String type;

    private String HouseNo;

    private String DumpYardNo;

    private String PointNo;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHouseNo() {
        return HouseNo;
    }

    public void setHouseNo(String houseNo) {
        HouseNo = houseNo;
    }

    public String getDumpYardNo() {
        return DumpYardNo;
    }

    public void setDumpYardNo(String dumpYardNo) {
        DumpYardNo = dumpYardNo;
    }

    public String getPointNo() {
        return PointNo;
    }

    public void setPointNo(String pointNo) {
        PointNo = pointNo;
    }

    @Override
    public String toString() {
        return "EmpWorkHistoryDetailPojo{" +
                "time='" + time + '\'' +
                ", Date='" + Date + '\'' +
                ", type='" + type + '\'' +
                ", HouseNo='" + HouseNo + '\'' +
                ", DumpYardNo='" + DumpYardNo + '\'' +
                ", PointNo='" + PointNo + '\'' +
                '}';
    }
}
