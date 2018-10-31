package com.appynitty.swachbharatabhiyanlibrary.pojos;

public class VehicleTypePojo {

    private String description;

    private String vtId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVtId() {
        return vtId;
    }

    public void setVtId(String vtId) {
        this.vtId = vtId;
    }

    @Override
    public String toString() {
        return "VehicleTypePojo{" +
                "description='" + description + '\'' +
                ", vtId='" + vtId + '\'' +
                '}';
    }
}
