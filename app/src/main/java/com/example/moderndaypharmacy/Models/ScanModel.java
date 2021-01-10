package com.example.moderndaypharmacy.Models;

public class ScanModel {
    private String id;
    private String image;
    private String price;
    public ScanModel(){

    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
