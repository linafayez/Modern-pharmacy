package com.example.moderndaypharmacy.User;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.moderndaypharmacy.ProductModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreferance {
    public static void SaveCart(Context context, List<ProductModel> pro) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences("ProductCart",
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String product = gson.toJson(pro);
        editor.putString("Product", product);

        editor.apply();
    }
    public void addToCart(Context context, ProductModel product) {
        ArrayList<ProductModel> productModelList = getCartData(context);



        if (productModelList == null )
            productModelList = new ArrayList<ProductModel>();
        productModelList.add(product);
        SaveCart(context, productModelList);
    }



    public ArrayList<ProductModel> getCartData(Context context) {
        SharedPreferences settings;
        List<ProductModel> productModels;

        settings = context.getSharedPreferences("ProductCart",
                Context.MODE_PRIVATE);

        if (settings.contains("Product")) {
            String jsonFavorites = settings.getString("Product", null);
            Gson gson = new Gson();
            ProductModel[] cartItem = gson.fromJson(jsonFavorites,
                    ProductModel[].class);

            productModels = Arrays.asList(cartItem);
            productModels = new ArrayList<ProductModel>(productModels);
        } else
            return null;

        return (ArrayList<ProductModel>) productModels;
    }

}
