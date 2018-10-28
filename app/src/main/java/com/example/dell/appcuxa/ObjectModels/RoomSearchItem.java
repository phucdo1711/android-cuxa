package com.example.dell.appcuxa.ObjectModels;

import com.google.gson.annotations.SerializedName;

public class RoomSearchItem {
    @SerializedName("id")
    public String id;
    @SerializedName("landlord")
    public LandLord landLord;
    @SerializedName("type")
    public String type;
    @SerializedName("name")
    public String name;
    @SerializedName("images")
    public ImageItem[] images;
    @SerializedName("price")
    public String price;
    @SerializedName("electricityPrice")
    public String electricityPrice;
    @SerializedName("waterPrice")
    public String waterPrice;
    @SerializedName("downPayment")
    public String downPayment;
    @SerializedName("location")
    public LocationRoom locationRoom;
    @SerializedName("address")
    public String address;
    @SerializedName("area")
    public String area;
    @SerializedName("amountOfTenant")
    public String amountOfTenant;
    @SerializedName("genderAccepted")
    public String genderAccepted;
    @SerializedName("comments")
    public String[] comments;
    @SerializedName("description")
    public String description;
    @SerializedName("status")
    public String status;
    @SerializedName("isVerified")
    public Boolean isVerified;
    @SerializedName("utilities")
    public String[] utilities;
    @SerializedName("saved")
    public String[] saved;
    @SerializedName("tenants")
    public String[] tenants;
    @SerializedName("createdAt")
    public String createdAt;
    @SerializedName("updatedAt")
    public String updatedAt;
    @SerializedName("isSaved")
    public Boolean isSaved;

    public void setSaved(Boolean saved) {
        isSaved = saved;
    }
    public Boolean getIsSaved(){
        return isSaved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LandLord getLandLord() {
        return landLord;
    }

    public void setLandLord(LandLord landLord) {
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

    public ImageItem[] getImages() {
        return images;
    }

    public void setImages(ImageItem[] images) {
        this.images = images;
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

    public LocationRoom getLocationRoom() {
        return locationRoom;
    }

    public void setLocationRoom(LocationRoom locationRoom) {
        this.locationRoom = locationRoom;
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

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
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

    public String[] getUtilities() {
        return utilities;
    }

    public void setUtilities(String[] utilities) {
        this.utilities = utilities;
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
}
