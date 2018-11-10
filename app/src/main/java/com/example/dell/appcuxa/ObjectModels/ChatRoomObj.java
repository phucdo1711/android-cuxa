package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

public class ChatRoomObj {
    @SerializedName("count")
    private String count;
    @SerializedName("rows")
    private ObjectChat[] rows;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ObjectChat[] getRows() {
        return rows;
    }

    public void setRows(ObjectChat[] rows) {
        this.rows = rows;
    }
}
