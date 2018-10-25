package com.appynitty.retrofitconnectionlibrary.pojos;

public class ResultPojo {

    private String status;
    private String message;

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
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
