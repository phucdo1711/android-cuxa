package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ObjectChat implements Serializable{
    @SerializedName("id")
    private String id;
    @SerializedName("users")
    private UserObject[] users;
    @SerializedName("name")
    private String name;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
