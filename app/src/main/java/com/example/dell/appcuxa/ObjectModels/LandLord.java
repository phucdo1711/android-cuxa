package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

public class LandLord {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("picture")
    public String picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
