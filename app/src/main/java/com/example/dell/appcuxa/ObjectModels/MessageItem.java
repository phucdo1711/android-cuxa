package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

public class MessageItem extends ObjectCommon {
    @SerializedName("user")
    public UserObject userObject;
    @SerializedName("type")
    public String type;
    @SerializedName("content")
    public String content;
    @SerializedName("chatRoom")
    public String chatRoom;

    public MessageItem(String chatRoom, String type, String content) {
        this.type = type;
        this.content = content;
        this.chatRoom = chatRoom;
    }

    public UserObject getUserObject() {
        return userObject;
    }

    public void setUserObject(UserObject userObject) {
        this.userObject = userObject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }
}



