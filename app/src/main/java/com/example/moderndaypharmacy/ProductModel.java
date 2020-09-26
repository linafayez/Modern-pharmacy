package com.example.moderndaypharmacy;

import java.util.ArrayList;

public class ProductModel {
    private String name;
    private int price;
    private String desc;
    private int itemNumber;
    private String ID;
    private ArrayList<String> thumbnailPic;
    private ArrayList<String> pic;

     public ProductModel(){
     }
    public ProductModel(String ID,String name, int price,String desc, int itemNumber,ArrayList<String> pic) {
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.pic =pic;
        this.ID =ID;
        this.itemNumber = itemNumber;

    }
    public ProductModel(String ID,String name, int price,String desc, int itemNumber,ArrayList<String> pic,ArrayList<String> thumbnailPic) {
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.pic =pic;
        this.ID =ID;
        this.itemNumber = itemNumber;
        this.thumbnailPic = thumbnailPic;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<String> getThumbnailPic() {
        return thumbnailPic;
    }

    public void setThumbnailPic(ArrayList<String> thumbnailPic) {
        this.thumbnailPic = thumbnailPic;
    }

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }
}