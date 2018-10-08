package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoomSearchResult implements Serializable{
    @SerializedName("byLocaction")
    public ObjectListByOption objectByLocation;
    @SerializedName("byPrice")
    public ObjectListByOption objectByPrice;

    public ObjectListByOption getObjectByLocation() {
        return objectByLocation;
    }

    public void setObjectByLocation(ObjectListByOption objectByLocation) {
        this.objectByLocation = objectByLocation;
    }

    public ObjectListByOption getObjectByPrice() {
        return objectByPrice;
    }

    public void setObjectByPrice(ObjectListByOption objectByPrice) {
        this.objectByPrice = objectByPrice;
    }
}
