package com.example.dell.appcuxa.ObjectModels;

public class ImageObject {
    public String idImage;
    public String urlImage;

    public ImageObject(String idImage, String urlImage) {
        this.idImage = idImage;
        this.urlImage = urlImage;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
