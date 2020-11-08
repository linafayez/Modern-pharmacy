package com.example.moderndaypharmacy.Util;

import android.text.Html;
import android.text.Spanned;

import com.example.moderndaypharmacy.Models.ProductModel;

import java.util.ArrayList;

public class TextViewUtil {
    public static Spanned ItemsName(ArrayList<ProductModel> data){
        String text ="<ul>";
        for(int i=0;i < data.size();i++) {
            text +="<li>"+data.get(i).getName()+ "</li>";
        }
        text+="</ul>";

        return Html.fromHtml(text);

    }
}
