package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

public class RoomCreatedObj {
    @SerializedName("user")
    public String user;
    @SerializedName("name")
    public String name;

    public RoomCreatedObj(String user, String name) {
        this.user = user;
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
