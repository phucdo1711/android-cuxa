package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

public class SavedRoom {
    @SerializedName("location")
    public LocationRoom location;
    @SerializedName("images")
    public ImageItem[] imageObject;
    @SerializedName("comments")
    public String[] comment;
    @SerializedName("utilities")
    public UtilityObject[] utilityObjects;


    @SerializedName("id")
    public String id;
    @SerializedName("landlord")
    public String landLord;
    @SerializedName("type")
    public String type;
    @SerializedName("name")
    public String name;
    @SerializedName("price")
    public String price;
    @SerializedName("electricityPrice")
    public String electricityPrice;
    @SerializedName("waterPrice")
    public String waterPrice;
    @SerializedName("downPayment")
    public String downPayment;
    @SerializedName("address")
    public String address;
    @SerializedName("area")
    public String area;
    @SerializedName("amountOfTenant")
    public String amountOfTenant;
    @SerializedName("genderAccepted")
    public String genderAccepted;
    @SerializedName("description")
    public String description;
    @SerializedName("status")
    public String status;
    @SerializedName("isVerified")
    public Boolean isVerified;
    @SerializedName("saved")
    public String[] saved;
    @SerializedName("tenants")
    public String[] tenants;
    @SerializedName("createdAt")
    public String createdAt;
    @SerializedName("updatedAt")
    public String updatedAt;
    @SerializedName("__v")
    public String __v;

    public LocationRoom getLocation() {
        return location;
    }

    public void setLocation(LocationRoom location) {
        this.location = location;
    }

    public ImageItem[] getImageObject() {
        return imageObject;
    }

    public void setImageObject(ImageItem[] imageObject) {
        this.imageObject = imageObject;
    }

    public String[] getComment() {
        return comment;
    }

    public void setComment(String[] comment) {
        this.comment = comment;
    }

    public UtilityObject[] getUtilityObjects() {
        return utilityObjects;
    }

    public void setUtilityObjects(UtilityObject[] utilityObjects) {
        this.utilityObjects = utilityObjects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLandLord() {
        return landLord;
    }

    public void setLandLord(String landLord) {
        this.landLord = landLord;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getElectricityPrice() {
        return electricityPrice;
    }

    public void setElectricityPrice(String electricityPrice) {
        this.electricityPrice = electricityPrice;
    }

    public String getWaterPrice() {
        return waterPrice;
    }

    public void setWaterPrice(String waterPrice) {
        this.waterPrice = waterPrice;
    }

    public String getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(String downPayment) {
        this.downPayment = downPayment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAmountOfTenant() {
        return amountOfTenant;
    }

    public void setAmountOfTenant(String amountOfTenant) {
        this.amountOfTenant = amountOfTenant;
    }

    public String getGenderAccepted() {
        return genderAccepted;
    }

    public void setGenderAccepted(String genderAccepted) {
        this.genderAccepted = genderAccepted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public String[] getSaved() {
        return saved;
    }

    public void setSaved(String[] saved) {
        this.saved = saved;
    }

    public String[] getTenants() {
        return tenants;
    }

    public void setTenants(String[] tenants) {
        this.tenants = tenants;
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

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }
}
