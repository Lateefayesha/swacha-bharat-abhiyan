package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class LoginDetailsPojo {

    private String message;

    private String status;

    private String userId;

    private String userPassword;

    private String userLoginId;

    private String type;

    private String messageMar;

    private boolean gtFeatures;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageMar() {
        return messageMar;
    }

    public void setMessageMar(String messageMar) {
        this.messageMar = messageMar;
    }

    public boolean getGtFeatures() {
        return gtFeatures;
    }

    public void setGtFeatures(boolean gtFeatures) {
        this.gtFeatures = gtFeatures;
    }

    @Override
    public String toString() {
        return "LoginDetailsPojo{" + "message='" + message + '\''
                                   + ", status='" + status + '\''
                                   + ", userId='" + userId + '\''
                                   + ", userPassword='" + userPassword + '\''
                                   + ", userLoginId='" + userLoginId + '\''
                                   + ", type='" + type + '\''
                                   + ", messageMar='" + messageMar + '\''
                                   + ", gtFeatures='" + gtFeatures + '\'' + '}';
    }
}
