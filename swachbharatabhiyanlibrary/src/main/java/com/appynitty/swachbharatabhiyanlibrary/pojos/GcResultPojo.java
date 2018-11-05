package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class GcResultPojo {

    private String name;
    private String status;
    private String message;
    private String messageMar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "ResultPojo{" +
                "name='" + name + '\'' +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", messageMar='" + messageMar + '\'' +
                '}';
    }
}
