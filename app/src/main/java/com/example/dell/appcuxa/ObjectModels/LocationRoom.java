package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

public class LocationRoom {
    @SerializedName("type")
    public String type;
    @SerializedName("coordinates")
    public Double[] coordinates;

    public LocationRoom(String type, Double[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }
}
