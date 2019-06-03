package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class LoginDetailsPojo {

    private String userId;

    private String type;

    private String typeId;

    private String status;

    private String message;

    private String messageMar;

    private Boolean gtFeatures;

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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageMar() {
        return messageMar;
    }

    public void setMessageMar(String messageMar) {
        this.messageMar = messageMar;
    }

    public Boolean getGtFeatures() {
        return gtFeatures;
    }

    public void setGtFeatures(Boolean gtFeatures) {
        this.gtFeatures = gtFeatures;
    }

    @Override
    public String toString() {
        return "LoginDetailsPojo{" +
                "userId='" + userId + '\'' +
                "type='" + type + '\'' +
                ", typeId='" + typeId + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", messageMar='" + messageMar + '\'' +
                ", gtFeatures=" + gtFeatures +
                '}';
    }
}
