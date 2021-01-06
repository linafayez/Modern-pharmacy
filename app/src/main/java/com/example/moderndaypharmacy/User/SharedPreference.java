package com.example.moderndaypharmacy.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.moderndaypharmacy.Models.CamModel;
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
        user = context.getSharedPreferences("User", Context.MODE_PRIVATE);
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
        }
        else{
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

    public static void SaveCamera(List<CamModel> came) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("Camera",
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String cam = gson.toJson(came);
        editor.putString("Camera", cam);
        editor.apply();
    }
    public CamModel deleteCamera(CamModel cam){
        ArrayList <ProductModel> camModelArrayList = getCartData();
        ArrayList <ProductModel> camModels = new ArrayList <>();
        if (camModels == null ) {
            camModels = new ArrayList <>();
        }else{
            for (int i=0;i<camModelArrayList.size();i++){
                if(!camModelArrayList.get(i).getID().equals(cam.getImage())){
                    camModels.add(camModelArrayList.get(i));
                }
            }

        }
        List <CamModel> camModelList = null;
        SaveCamera(camModelList);
        return cam;

    }
    public void addToCamera(CamModel cam) {
        ArrayList <ProductModel> camModelArrayList = getCartData();
        boolean have = false;
        if (camModelArrayList == null ) {
            camModelArrayList = new ArrayList<ProductModel>();
        }
        else{
            for (int i=0;i<camModelArrayList.size();i++){
                if(camModelArrayList.get(i).getID().equals(cam.getImage())){
                    have=true;
                    break;
                }
            }
        }
        if(have == false)
            camModelArrayList.add(cam);
        else{
            Toast.makeText(context,"from past",Toast.LENGTH_LONG).show();
        }
        SaveCamera( camModelArrayList);
    }

    private void SaveCamera(ArrayList<ProductModel> camModelArrayList) {
    }

    public ArrayList<CamModel> getCamData() {
        SharedPreferences settings;
        List<CamModel> camModels;

        settings = context.getSharedPreferences("Camera",
                Context.MODE_PRIVATE);

        if (settings.contains("Camera")) {
            String jsonFavorites = settings.getString("Camera", null);
            Gson gson = new Gson();
            CamModel[] camModels1 = gson.fromJson(jsonFavorites,
                    CamModel[].class);

            camModels= Arrays.asList(camModels1);
            camModels = new ArrayList<CamModel>(camModels);
        } else
            return null;

        return (ArrayList<CamModel>) camModels;
    }
}
