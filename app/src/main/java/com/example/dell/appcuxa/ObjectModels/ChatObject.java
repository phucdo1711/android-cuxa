package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

public class ChatObject extends ObjectCommon {
    @SerializedName("users")
    public UserObject[] users;
    @SerializedName("name")
    public  String name;
    @SerializedName("messages")
    public MessageItem[] messageItems;

    public UserObject[] getUsers() {
        return users;
    }

    public void setUsers(UserObject[] users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MessageItem[] getMessageItems() {
        return messageItems;
    }

    public void setMessageItems(MessageItem[] messageItems) {
        this.messageItems = messageItems;
    }
}
