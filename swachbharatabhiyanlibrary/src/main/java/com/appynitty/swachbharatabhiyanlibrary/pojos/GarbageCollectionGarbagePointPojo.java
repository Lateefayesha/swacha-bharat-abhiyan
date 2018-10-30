package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class GarbageCollectionGarbagePointPojo {

    private String Long;

    private String image1;

    private String image2;

    private String gpId;

    private String AfterImage;

    private String Lat;

    private String beforeImage;

    private String comment;

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getGpId() {
        return gpId;
    }

    public void setGpId(String gpId) {
        this.gpId = gpId;
    }

    public String getAfterImage() {
        return AfterImage;
    }

    public void setAfterImage(String afterImage) {
        AfterImage = afterImage;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getBeforeImage() {
        return beforeImage;
    }

    public void setBeforeImage(String beforeImage) {
        this.beforeImage = beforeImage;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "GarbageCollectionGarbagePointPojo{" +
                "Long='" + Long + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", gpId='" + gpId + '\'' +
                ", AfterImage='" + AfterImage + '\'' +
                ", Lat='" + Lat + '\'' +
                ", beforeImage='" + beforeImage + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
