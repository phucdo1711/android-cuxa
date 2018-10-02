package com.example.dell.appcuxa.ObjectModels;

import java.io.Serializable;
import java.util.List;

public class RoomInfo implements Serializable {
    private List<String> image;
    private String nameRoom;
    private String price;
    private String address;
    private String purpose;

    public RoomInfo() {
    }

    public RoomInfo(List<String> image, String nameRoom, String price, String address, String purpose) {
        this.image = image;
        this.nameRoom = nameRoom;
        this.price = price;
        this.address = address;
        this.purpose = purpose;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
