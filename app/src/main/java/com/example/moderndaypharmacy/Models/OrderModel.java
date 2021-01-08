package com.example.moderndaypharmacy.Models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderModel implements Serializable {
    private String id;
    private String userId;
    private ArrayList<ProductModel> productList;
    private Timestamp time;
    private String orderState;
    private double total;
    private String note;
    private FeedbackModel feedbackModel;
    private double latitude ;
    private double longitude ;
    public OrderModel(String id, String userId, ArrayList<ProductModel> productList, Timestamp time, String orderState, double total) {
        this.id = id;
        this.userId = userId;
        this.productList = productList;
        this.time = time;
        this.orderState = orderState;
        this.total = total;
    }
   public OrderModel(){
   }
    public ArrayList<ProductModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductModel> productList) {
        this.productList = productList;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public FeedbackModel getFeedbackModel() {
        return feedbackModel;
    }

    public void setFeedbackModel(FeedbackModel feedbackModel) {
        this.feedbackModel = feedbackModel;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
