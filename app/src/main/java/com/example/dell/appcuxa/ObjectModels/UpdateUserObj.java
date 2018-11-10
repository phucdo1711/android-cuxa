package com.example.dell.appcuxa.ObjectModels;

public class UpdateUserObj {
    public String name;
    public String picture;
    public String phone;
    public String gender;
    public String birth;
    public String currentResidence;
    public String school;
    public String idCard;

    public UpdateUserObj(String name, String picture, String phone, String gender, String birth, String currentResidence, String school, String idCard) {
        this.name = name;
        this.picture = picture;
        this.phone = phone;
        this.gender = gender;
        this.birth = birth;
        this.currentResidence = currentResidence;
        this.school = school;
        this.idCard = idCard;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getCurrentResidence() {
        return currentResidence;
    }

    public void setCurrentResidence(String currentResidence) {
        this.currentResidence = currentResidence;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

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
