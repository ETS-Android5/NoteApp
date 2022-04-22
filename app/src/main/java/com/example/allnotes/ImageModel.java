package com.example.allnotes;

public class ImageModel {
    private String imageUrl;

    public ImageModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageModel(){}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
