package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class GcResultPojo {

    private String name;
    private String status;
    private String message;

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

    @Override
    public String toString() {
        return "ResultPojo{" +
                "name='" + name + '\'' +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
