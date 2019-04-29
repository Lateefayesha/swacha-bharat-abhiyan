package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class GarbageCollectionPojo {

    private String image2;

    private String image1;

    private String AfterImage;

    private String beforeImage;

    private String id;

    private String comment;

    private int garbageType;

    private int weightTotal;

    private int weightTotalDry;

    private int weightTotalWet;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGarbageType() {
        return garbageType;
    }

    public void setGarbageType(int garbageType) {
        this.garbageType = garbageType;
    }

    public int getWeightTotal() {
        return weightTotal;
    }

    public void setWeightTotal(int weightTotal) {
        this.weightTotal = weightTotal;
    }

    public int getWeightTotalDry() {
        return weightTotalDry;
    }

    public void setWeightTotalDry(int weightTotalDry) {
        this.weightTotalDry = weightTotalDry;
    }

    public int getWeightTotalWet() {
        return weightTotalWet;
    }

    public void setWeightTotalWet(int weightTotalWet) {
        this.weightTotalWet = weightTotalWet;
    }

    @Override
    public String toString() {
        return "GarbageCollectionPojo{"
                + "image2='" + image2 + '\''
                + ", image1='" + image1 + '\''
                + ", AfterImage='" + AfterImage + '\''
                + ", beforeImage='" + beforeImage + '\''
                + ", id='" + id + '\''
                + ", comment='" + comment + '\''
                + ", weightTotal='" + weightTotal + '\''
                + ", weightTotalDry='" + weightTotalDry + '\''
                + ", weightTotalWet='" + weightTotalWet + '\''
                + ", garbageType=" + garbageType + '}';
    }
}
