package com.example.moderndaypharmacy.Models;

public class CategoryModel {
    private String name,desc, image,id;


    public CategoryModel(){

    }
    public CategoryModel(String id,String name, String desc, String image){
        this.name=name;
        this.desc=desc;
        this.image=image;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
