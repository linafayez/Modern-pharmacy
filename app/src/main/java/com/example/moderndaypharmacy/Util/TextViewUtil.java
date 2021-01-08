package com.example.moderndaypharmacy.Util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.Html;
import android.text.Spanned;

import com.example.moderndaypharmacy.Models.FeedbackModel;
import com.example.moderndaypharmacy.Models.ProductModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TextViewUtil {
    static DecimalFormat df2 = new DecimalFormat("#.##");
    public static String getPriceToDisplay(int price, int i){
        return df2.format(price*i / 100.0) + "JD";
    }
    public static Spanned feedbackDara(FeedbackModel data){
        String text ="<ul>";
        for(int i=0;i < data.getModels().size();i++) {
            text +="<li>"+data.getModels().get(i).getProduct().getName()+ "</li><tab>"+data.getModels().get(i).getRating();
        }
        text+="</ul>";

        return Html.fromHtml(text);
    }
    public static String getDiscountToDisplay(int price,int discount, int i){
        Double p =price/100.0 ;
        p-=p*(discount/100.0);
        return df2.format(p*i)+"JD";
    }
    public static double setSubTotal(ArrayList<ProductModel> data) {
        Double sum = 0.0;
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                ProductModel P = data.get(i);
                double price = P.getPrice() / 100.0;
                int number = P.getItemNumberInCart();
                sum += Double.parseDouble(df2.format(price * number));
                // sum += price*number;
            }
        }
        return sum;
    }
    public static String totalWithDis(ArrayList<ProductModel> data,int dis){
        Double p =setSubTotal(data);
        p-=p*(dis/100.0);
        return df2.format(p)+"JD";
    }
    public static Spanned ItemsName(ArrayList<ProductModel> data){
        String text ="<ul>";
        for(int i=0;i < data.size();i++) {
            text +="<li>"+data.get(i).getName()+ "</li>";
        }
        text+="</ul>";

        return Html.fromHtml(text);

    }

    public static String getCompleteAddressString(double LATITUDE, double LONGITUDE, Context context) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for(int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
}
