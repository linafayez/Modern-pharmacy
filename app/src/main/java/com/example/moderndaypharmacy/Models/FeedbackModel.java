package com.example.moderndaypharmacy.Models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class FeedbackModel {
    private ArrayList<ProductModel> productModels;
    private Timestamp timestamp;
    private String userId;
    private String OrderId;
    public FeedbackModel(){

    }
    FeedbackModel(ArrayList<ProductModel> productsModels, Timestamp timestamp, String userId){
        this.productModels =productModels;
        this.timestamp =timestamp;
        this.userId = userId;
    }

    public ArrayList<ProductModel> getProductsModels() {
        return productModels;
    }

    public void setProductsModels(ArrayList<ProductModel> productsModels) {
        this.productModels = productsModels;
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
