package com.example.moderndaypharmacy.Models;

import java.io.Serializable;

public class AdsModel implements Serializable {
    private String adsId;
    private String adsImage;
    private String proId;

    public AdsModel(){

    }

    public AdsModel(String adsId, String adsImage,String proId) {
        this.adsId = adsId;
       this.adsImage=adsImage;
       this.proId=proId;
    }
    public String getAdsId() {
        return adsId;
    }

    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }
    public String getAdsImage() {
        return adsImage;
    }

    public void setAdsImage(String adsImage) {
        this.adsImage = adsImage;
    }

    public String getProId(){return proId;}

    public void setProId(String proId) {
        this.proId = proId;
    }
}
