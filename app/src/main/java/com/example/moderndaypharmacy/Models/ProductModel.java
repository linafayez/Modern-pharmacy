package com.example.moderndaypharmacy.Models;

import java.util.ArrayList;
import java.util.Locale;

public class ProductModel {
    private String name;
    private int price;
    private String desc;
    private int itemNumber;
    private int itemNumberInCart;
    private String ID;
    private ArrayList<String> thumbnailPic;
    private ArrayList<String> pic;
    private String category_id ;
    private float rating;
    private ArrayList<String> note;


     public ProductModel(){
         setItemNumberInCart(0);
     }
    public ProductModel(String ID,String name, int price,String desc, int itemNumber,ArrayList<String> pic,String category_id) {
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.pic =pic;
        this.ID =ID;
        this.itemNumber = itemNumber;
        this.category_id = category_id;
        setItemNumberInCart(0);

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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getItemNumberInCart() {
        return itemNumberInCart;
    }

    public void setItemNumberInCart(int itemNumberInCart) {
        this.itemNumberInCart = itemNumberInCart;
    }

    public ArrayList<String> getNote() {
        return note;
    }

    public void setNote(ArrayList<String> note) {
        this.note = note;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
