package com.example.moderndaypharmacy.Admin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.OrderModel;
import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.Models.ScanModel;
import com.example.moderndaypharmacy.R;
import com.example.moderndaypharmacy.User.SharedPreference;
import com.example.moderndaypharmacy.User.feedbackArgs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class OrderProcess extends Fragment {
    TextView itemsList,orderId,UserId,OrderState,time,Date,total,OId,UId,txt;
    EditText Total,State;
    Button update;
    OrderModel orderModel,ordersModel;
    RecyclerView products,scan;
    RecyclerView.Adapter adapter;
    ScanRAdapter adapter2;
    Spinner spinner;
    String newTotal="";
    RatingBar rate;
    ArrayList<ScanModel> scanModels;


    public OrderProcess() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_process, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderModel = OrderProcessArgs.fromBundle(getArguments()).getOrder();
        ordersModel= feedbackArgs.fromBundle(getArguments()).getOrder();
        orderId = view.findViewById(R.id.id);
        UserId = view.findViewById(R.id.userId);
        OrderState = view.findViewById(R.id.txt1);
        time = view.findViewById(R.id.time);
        Date = view.findViewById(R.id.date);
        total = view.findViewById(R.id.textView11);
        itemsList=view.findViewById(R.id.itemList);
        OId=view.findViewById(R.id.orderId);
        UId=view.findViewById(R.id.UserId);
        Total=view.findViewById(R.id.total);
        rate=view.findViewById(R.id.ratingBar);
        update=view.findViewById(R.id.button);
        products=view.findViewById(R.id.productList);
        spinner=view.findViewById(R.id.spinner);
        scan=view.findViewById(R.id.scan);
        txt=view.findViewById(R.id.s);


        ArrayAdapter<String> spinadapter = new ArrayAdapter (this.getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.orderState));
        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinadapter);
        if (orderModel != null) {
            itemsList.setText(itemsList.getText());
            Total.setText(orderModel.getTotal()+"");
            OId.setText(orderModel.getId());
            UId.setText(orderModel.getUserId());
            scanModels = orderModel.getScanModels();
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            adapter= new ProductAdapter(orderModel.getProductList());
            products.setLayoutManager(manager);
            products.setHasFixedSize(false);
            products.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   orderModel.setOrderState(adapterView.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Total.getText().equals(orderModel.getTotal())&&State.getText().equals(orderModel.getOrderState()))
                    {
                        Total.setText(orderModel.getTotal()+"");
                    }
                    else
                    {
                        newTotal=Total.getText().toString();
                        Total.setText(newTotal);
                        orderModel.setTotal(Double.parseDouble(String.valueOf(newTotal)));

                    }
                    Toast.makeText(view.getContext(), "Data updated", Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("Orders").document(orderModel.getId()).set(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            adapter.notifyDataSetChanged();
                        }
                    });

                }
               });

            if(scanModels!=null) {
                if (scanModels.size() == 0) {
                    txt.setVisibility(View.INVISIBLE);
                }
                RecyclerView.LayoutManager manager2 = new LinearLayoutManager(getContext());
                //adapter2 = new ScanAdapter(scanModels, getContext(), "cart");
                adapter2=new ScanRAdapter(orderModel.getScanModels(),getContext(),"cart");
                scan.setLayoutManager(manager2);
                scan.setHasFixedSize(false);
                scan.setAdapter(adapter2);

            }
        }

    }

    public  class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
        ArrayList<ProductModel> productList;

        public ProductAdapter(ArrayList<ProductModel> product) {productList = product;
        }
        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list,parent,false);

            return new ProductViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
            ProductModel P = productList.get(position);

            holder.name.setText(P.getName());
            holder.price.setText(P.getPrice()/100+" "+"JD");
            Picasso.get().load(Uri.parse(P.getPic().get(0))).into(holder.image);
           /* if(orderModel.getOrderState().equals("Delivered")||orderModel.getOrderState().equals("Ordered")) {
                if (orderModel.getFeedbackModel().getModels()!=null&&orderModel.getFeedbackModel().getModels().get(position).getRating() != null&&orderModel.getFeedbackModel().getModels().get(position).getNote() != null)
              //  {
                    update.setVisibility(View.INVISIBLE);
                    holder.note.setVisibility(View.VISIBLE);
                    holder.rate.setVisibility(View.VISIBLE);
                    holder.rate.setRating(Float.parseFloat(orderModel.getFeedbackModel().getModels().get(position).getRating()));
                    holder.note.setText(orderModel.getFeedbackModel().getModels().get(position).getNote());
               // }
            }


            if(orderModel.getOrderState().equals("Confirmed"))
            {
                update.setVisibility(View.INVISIBLE);
                holder.note.setVisibility(View.INVISIBLE);
                holder.rate.setVisibility(View.INVISIBLE);

            }*/
        }

        @Override
        public int getItemCount() {
            if(productList!= null)
                return productList.size();
            else {
                return 0;
            }
        }

         class ProductViewHolder extends RecyclerView.ViewHolder{
            View view;
            TextView name,price,note;
            ImageView image ;
            RatingBar rate;

            public ProductViewHolder(@NonNull View view) {
                super(view);
                this.view=view;
                name = view.findViewById(R.id.name);
                image = view.findViewById(R.id.proImage);
                price=view.findViewById(R.id.price);
                rate=view.findViewById(R.id.ratingBar);
                note=view.findViewById(R.id.note);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProductModel productModel =productList.get(getAdapterPosition());
                        String id=productModel.getID();
                        if(productModel.getID().equals(id)) {
                            Toast.makeText(view.getContext(), "Item is found", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(view.getContext(), "Item is not found", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        }
    }

    public class ScanRAdapter extends  RecyclerView.Adapter<ScanRAdapter.ScanViewHolder> {
        ArrayList<ScanModel> models;
        Context context;
        String type;
        public ScanRAdapter( ArrayList<ScanModel> models, Context context,String type){
            this.models =models;
            this.context = context;
            this.type=type;
        }
        @NonNull
        @Override
        public ScanRAdapter.ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan,parent,false);

            return new ScanRAdapter.ScanViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ScanRAdapter.ScanViewHolder holder, int position) {
            Picasso.get().load(Uri.parse(models.get(position).getImage())).into(holder.image);
            holder.text.setText(models.get(position).getPrice());
        }

        @Override
        public int getItemCount() {
            return models == null?0:models.size();
        }

        public class ScanViewHolder extends RecyclerView.ViewHolder {
            ImageView image,delete;
            SharedPreference sharedPreference;
            Button done;
            TextView price;
            EditText text;
            public ScanViewHolder(@NonNull final View itemView) {
                super(itemView);
                sharedPreference = new SharedPreference(itemView.getContext());
                image = itemView.findViewById(R.id.imageView13);
                delete = itemView.findViewById(R.id.imageView5);
                done=itemView.findViewById(R.id.ok);
                price=itemView.findViewById(R.id.price);
                text=itemView.findViewById(R.id.text);

                if(type.equals("cart")){
                    delete.setVisibility(View.INVISIBLE);
                }

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String p= String.valueOf(text.getEditableText());
                        orderModel.getScanModels().get(getAdapterPosition()).setPrice(p);
                        FirebaseFirestore.getInstance().collection("Orders").document(orderModel.getId()).set(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });


                    }
                });
            }
        }
    }



}


