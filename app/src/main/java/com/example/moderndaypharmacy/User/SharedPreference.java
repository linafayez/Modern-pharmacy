package com.example.moderndaypharmacy.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {
    static Context context;
    public SharedPreference(Context context){
        this.context =context;
    }

    public static void addUser(UserInfoModel userObject){
        SharedPreferences user;
        SharedPreferences.Editor editorUser;
        user = context.getSharedPreferences("User",
                Context.MODE_PRIVATE);
        editorUser = user.edit();
        Gson gson = new Gson();
        String userString = gson.toJson(userObject);
        editorUser.putString("user", userString);

        editorUser.apply();
    }

    public UserInfoModel getUser(){
        SharedPreferences settings;
        UserInfoModel user;

        settings = context.getSharedPreferences("User",
                Context.MODE_PRIVATE);

        if (settings.contains("user")) {
            String u = settings.getString("user", null);
            Gson gson = new Gson();
             user = gson.fromJson(u,
                    UserInfoModel.class);

        }else {
            user = null;
        }

        return user;
    }
    public static void SaveCart(List<ProductModel> pro) {
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
    public void addToCart(ProductModel product) {
        ArrayList<ProductModel> productModelList = getCartData();
        boolean have = false;
        if (productModelList == null ) {
            productModelList = new ArrayList<ProductModel>();
        }else{
           for (int i=0;i<productModelList.size();i++){
              if(productModelList.get(i).getID().equals(product.getID())){
                  have=true;
                  break;
              }
            }
        }
        if(have == false)
           productModelList.add(product);
        else{
            Toast.makeText(context,"from past",Toast.LENGTH_LONG).show();
        }
        SaveCart( productModelList);
    }
    public ArrayList<ProductModel> deleteProduct(ProductModel product){
        ArrayList<ProductModel> productModelList = getCartData();
        ArrayList<ProductModel> productModels = new ArrayList<>();
        if (productModelList == null ) {
            productModelList = new ArrayList<ProductModel>();
        }else{
            for (int i=0;i<productModelList.size();i++){
                if(!productModelList.get(i).getID().equals(product.getID())){
                    productModels.add(productModelList.get(i));
                }
            }

        }
        SaveCart( productModelList);
        return productModels;
    }



    public ArrayList<ProductModel> getCartData() {
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
