package com.example.moderndaypharmacy.User;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    ArrayList<ProductModel> cartList;

    public CartAdapter(ArrayList<ProductModel> productModels  ){
        cartList = productModels;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart,parent,false);

        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductModel P = cartList.get(position);
        if(P.getItemNumberInCart() == 0){
            P.setItemNumberInCart(1);
        }
        holder.ItemsCount.setText(P.getItemNumberInCart()+"");
        double   price = ((double) P.getPrice())/100;
        int number = P.getItemNumberInCart();

        holder.price.setText(price*number+"JD");
        holder.name.setText(P.getName());
        Picasso.get().load(Uri.parse(P.getPic().get(0))).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(cartList!= null)
            return cartList.size();
        else {
            return 0;
        }
    }

     class CartViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView price , ItemsCount ;
        TextView name;
        ImageView image , del;
        Button dec, inc;

        SharedPreference sharedPreference ;
        public CartViewHolder(@NonNull View view) {
            super(view);
            this.view=view;
            sharedPreference = new SharedPreference(view.getContext());
            price = (TextView) view.findViewById(R.id.price);
            name =(TextView) view.findViewById(R.id.name);
            image = (ImageView) view.findViewById(R.id.productImage);
            del = view.findViewById(R.id.imageView5);
            ItemsCount = view.findViewById(R.id.number);
            dec = view.findViewById(R.id.dec);
            inc = view.findViewById(R.id.inc);
            final Cart.changed c = new Cart.changed();
            inc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int a =Integer.parseInt(ItemsCount.getText().toString());
                    if(a<20){
                        ItemsCount.setText(++a +"");
                        ProductModel productModel =cartList.get(getAdapterPosition());
                        productModel.setItemNumberInCart(a);
                        cartList.set(getAdapterPosition(),productModel);
                        price.setText((((double) productModel.getPrice())/100 *a)+"JD");
                        sharedPreference.SaveCart(cartList);

                        c.total(((double) productModel.getPrice())/100);
                    }
                }
            });
            dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int a =Integer.parseInt(ItemsCount.getText().toString());
                    if(a>1){
                        ItemsCount.setText(--a +"");
                        ProductModel productModel =cartList.get(getAdapterPosition());
                        productModel.setItemNumberInCart(a);
                        cartList.set(getAdapterPosition(),productModel);
                        price.setText((double) productModel.getPrice()/100 *a+"JD");
                        sharedPreference.SaveCart(cartList);
                        c.total(-(double) productModel.getPrice()/100);

                    }
                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductModel productModel =cartList.get(getAdapterPosition());
                    int a =Integer.parseInt(ItemsCount.getText().toString());
                    double   pr = Double.parseDouble(price.getText().toString().split("JD")[0]);
                    c.total(-pr);
                    cartList.remove(productModel);
                    sharedPreference.SaveCart(cartList);
                    notifyDataSetChanged();
                    Toast.makeText(view.getContext(),"Item removed", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}

