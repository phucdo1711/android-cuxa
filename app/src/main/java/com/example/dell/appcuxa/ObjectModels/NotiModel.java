package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

public class NotiModel {
    @SerializedName("count")
    public int count;
    @SerializedName("rows")
    public NotiObject[] notiObjects;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public NotiObject[] getNotiObjects() {
        return notiObjects;
    }

    public void setNotiObjects(NotiObject[] notiObjects) {
        this.notiObjects = notiObjects;
    }
}
