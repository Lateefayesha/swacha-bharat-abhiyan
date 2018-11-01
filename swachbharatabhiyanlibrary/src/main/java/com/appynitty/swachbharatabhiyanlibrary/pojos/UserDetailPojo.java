package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class UserDetailPojo {

    private String profileImage;

    private String address;

    private String name;

    private String userId;

    private String type;

    private String mobileNumber;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "UserDetailPojo{" +
                "profileImage='" + profileImage + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}
