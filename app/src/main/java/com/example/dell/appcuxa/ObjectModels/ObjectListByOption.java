package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

public class ObjectListByOption {
    @SerializedName("count")
    public String count;
    @SerializedName("rows")
    public RoomSearchItem[] lstRoom;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public RoomSearchItem[] getLstRoom() {
        return lstRoom;
    }

    public void setLstRoom(RoomSearchItem[] lstRoom) {
        this.lstRoom = lstRoom;
    }
}
