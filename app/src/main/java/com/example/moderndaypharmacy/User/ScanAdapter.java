package com.example.moderndaypharmacy.User;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.ScanModel;
import com.example.moderndaypharmacy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ScanAdapter extends  RecyclerView.Adapter<ScanAdapter.CartViewHolder> {
    ArrayList<ScanModel> models;
    Context context;
    String type;
    public ScanAdapter( ArrayList<ScanModel> models, Context context,String type){
        this.models =models;
        this.context = context;
        this.type=type;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_cart,parent,false);

        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Picasso.get().load(Uri.parse(models.get(position).getImage())).into(holder.image);
    }

    @Override
    public int getItemCount() {
       return models == null?0:models.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView image,delete;
        SharedPreference sharedPreference;
        public CartViewHolder(@NonNull final View itemView) {
            super(itemView);
            sharedPreference = new SharedPreference(itemView.getContext());
            image = itemView.findViewById(R.id.imageView13);
            delete = itemView.findViewById(R.id.imageView5);
            if(type.equals("ch")){
                delete.setVisibility(View.INVISIBLE);
            }
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(itemView.getContext(),"imageView5",Toast.LENGTH_LONG).show();
                    //SharedPreference.SaveScanCart(sharedPreference.deleteScan(models.get(getAdapterPosition())));
                    models.remove(models.get(getAdapterPosition()));
                    SharedPreference.SaveScanCart(models);
                    notifyDataSetChanged();

                }
            });
        }
    }
}
