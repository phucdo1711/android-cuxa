package com.example.dell.appcuxa.ObjectModels;

public class UpdateUserObj {
    public String name;
    public String picture;
    public String phone;
    public String gender;

    public UpdateUserObj(){}
    public UpdateUserObj(String name, String picture, String phone, String gender) {
        this.name = name;
        this.picture = picture;
        this.phone = phone;
        this.gender = gender;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
