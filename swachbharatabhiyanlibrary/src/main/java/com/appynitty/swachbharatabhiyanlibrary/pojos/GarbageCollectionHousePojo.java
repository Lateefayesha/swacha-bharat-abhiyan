package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class GarbageCollectionHousePojo {

    private String Long;

    private String image2;

    private String image1;

    private String Lat ;

    private String AfterImage;

    private String beforeImage;

    private String houseId;

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
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

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
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

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    @Override
    public String toString() {
        return "GarbageCollectionHousePojo{" +
                "Long='" + Long + '\'' +
                ", image2='" + image2 + '\'' +
                ", image1='" + image1 + '\'' +
                ", Lat='" + Lat + '\'' +
                ", AfterImage='" + AfterImage + '\'' +
                ", beforeImage='" + beforeImage + '\'' +
                ", houseId='" + houseId + '\'' +
                '}';
    }
}
