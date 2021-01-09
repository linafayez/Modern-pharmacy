package com.example.moderndaypharmacy.Models;

import com.example.moderndaypharmacy.User.UserInfo;
import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class FeedbackModel {
    private ArrayList<ProductModel> productsModels;
    private Timestamp timestamp;
    private String userId;
    private String OrderId;
    private UserInfoModel user;
    private String id;

    public static class feed {
      private ProductModel product;
        private String note;
        private String rating;
        public feed(){}
         public feed(ProductModel product,
                     String note,
                     String rating){
             this.note= note;
             this.product= product;
             this.rating= rating;
         }

        public ProductModel getProduct() {
            return product;
        }

        public void setProduct(ProductModel product) {
            this.product = product;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }
    }
    private ArrayList<feed> models;
    public UserInfoModel getUser() {
        return user;
    }

    public void setUser(UserInfoModel user) {
        this.user = user;
    }

    public ArrayList<feed> getModels() {
        return models;
    }

    public void setModels(ArrayList<feed> models) {
        this.models = models;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public FeedbackModel(){

    }
    FeedbackModel(ArrayList<ProductModel> productsModels, Timestamp timestamp, String userId){
        this.productsModels =productsModels;
        this.timestamp =timestamp;
        this.userId = userId;
    }

    public ArrayList<ProductModel> getProductsModels() {
        return productsModels;
    }

    public void setProductsModels(ArrayList<ProductModel> productsModels) {
        this.productsModels = productsModels;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
