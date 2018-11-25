package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Comment;

import java.io.Serializable;

public class CommentContent extends ObjectCommon implements Serializable {
    @SerializedName("user")
    public UserObject userObject;
    @SerializedName("content")
    public String content;
    @SerializedName("room")
    public String room;
    @SerializedName("parent")
    public String parent;
    @SerializedName("childs")
    public CommentContent[] commentObject;

    public UserObject getUserObject() {
        return userObject;
    }

    public void setUserObject(UserObject userObject) {
        this.userObject = userObject;
    }

    public String getContent() {
        return content;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public CommentContent[] getCommentObject() {
        return commentObject;
    }

    public void setCommentObject(CommentContent[] commentObject) {
        this.commentObject = commentObject;
    }
}
