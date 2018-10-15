package com.example.dell.appcuxa.ObjectModels;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

public class UtilityObject {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("code")
    public String code;
    @SerializedName("__v")
    public String __v;
    public Drawable image;
    public boolean isChecked;

    public UtilityObject(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public boolean isIsChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }
}
