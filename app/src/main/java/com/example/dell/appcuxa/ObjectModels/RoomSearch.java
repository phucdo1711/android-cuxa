package com.example.dell.appcuxa.ObjectModels;

import java.io.Serializable;

public class RoomSearch implements Serializable {
    public LocationRoom location;
    public String distance;
    public Price price;

    public LocationRoom getLocation() {
        return location;
    }

    public void setLocation(LocationRoom location) {
        this.location = location;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
