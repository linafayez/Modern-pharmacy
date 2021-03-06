package com.example.moderndaypharmacy.User;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.OrderModel;
import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.Models.ScanModel;
import com.example.moderndaypharmacy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Orders extends Fragment {
    static OrderModel orderUser;
    TextView name, order, items, total, ItemNum, Total, state, orderState,notes,scanTitle;
    Button feedback;
    FirebaseFirestore db;
    RecyclerView productList,scan;
    RecyclerView.Adapter adapter,adapter2;
    ProductModel productModel;
    double price1, sum, amount;
    int number;
    String Id;
    ArrayList<ProductModel> data;
    ArrayList<ScanModel> scanModels;
    ScanModel scanModel;
    SharedPreference sharedPreference;
    ImageView imageView;

    public Orders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        productList = view.findViewById(R.id.products);
        sharedPreference = new SharedPreference(getContext());

        orderUser = OrdersArgs.fromBundle(getArguments()).getYouOrder();
        Id=orderUser.getId();
        name = view.findViewById(R.id.name);
        items = view.findViewById(R.id.items);
        total = view.findViewById(R.id.total);
        order = view.findViewById(R.id.order);
        state=view.findViewById(R.id.state);
        feedback=view.findViewById(R.id.feedback);
        notes=view.findViewById(R.id.note);
        orderState=view.findViewById(R.id.orderState);
        Total=view.findViewById(R.id.txt1);
        ItemNum=view.findViewById(R.id.itemNum);
        productList=view.findViewById(R.id.product);
        scan=view.findViewById(R.id.scan);
        scanTitle = view.findViewById(R.id.s);
        //scanModels = sharedPreference.getCartScanData();
        scanModel=new ScanModel();
        scanModels = orderUser.getScanModels();

        data=orderUser.getProductList();
        if (orderUser != null) {
            Total.setText(orderUser.getTotal()+ "JD");
            ItemNum.setText(String.valueOf(orderUser.getProductList().size()));
            state.setText(orderUser.getOrderState());

                RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            adapter= new ProductAdapter(orderUser.getProductList());
            productList.setLayoutManager(manager);
            productList.setHasFixedSize(false);
            productList.setAdapter(adapter);
        }
        if(scanModels!=null) {
            if (scanModels.size() == 0) {
                scanTitle.setVisibility(View.INVISIBLE);
            }
            RecyclerView.LayoutManager manager2 = new LinearLayoutManager(getContext());
            //adapter2 = new ScanAdapter(scanModels, getContext(), "cart");
            adapter2=new ScanRAdapter(orderUser.getScanModels(),getContext(),"cart");
            scan.setLayoutManager(manager2);
            scan.setHasFixedSize(false);
            scan.setAdapter(adapter2);

        }

    }

    public  class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
        ArrayList<ProductModel> productList;

        public ProductAdapter(ArrayList<ProductModel> order) {
            productList = order;
        }

        @NonNull
        @Override
        public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false);

            return new ProductAdapter.ProductViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
            productModel = productList.get(position);

            price1 = ((productModel.getPrice()));
            number = productModel.getItemNumberInCart();
            amount = price1 * number;
            holder.proName.setText(productModel.getName());
            holder.proPrice.setText("" + amount / 100);
            holder.itemNum.setText(String.valueOf(productModel.getItemNumberInCart()));
            Picasso.get().load(Uri.parse(productModel.getPic().get(0))).into(holder.image);
        }

        @Override
        public int getItemCount() {
            if (productList != null)
                return productList.size();
            else {
                return 0;
            }
        }

        class ProductViewHolder extends RecyclerView.ViewHolder {
            View view;
            TextView name, price, itemNum, proName, proPrice, items;
            ImageView image;
            Button cancel;

            public ProductViewHolder(@NonNull final View view) {
                super(view);
                this.view = view;
                name = view.findViewById(R.id.name);
                proName = view.findViewById(R.id.proName);
                proPrice = view.findViewById(R.id.proPrice);
                image = view.findViewById(R.id.proImage);
                price = view.findViewById(R.id.price);
                itemNum = view.findViewById(R.id.number);
                items = view.findViewById(R.id.items);
                cancel = view.findViewById(R.id.cancel);

                //if(orderUser.getOrderState().equals("Delivered")||orderUser.getOrderState().equals("Completed"))
                if (orderUser.getOrderState().equals("confirmed")) {
                    cancel.setVisibility(View.INVISIBLE);
                    feedback.setVisibility(View.VISIBLE);
                    notes.setVisibility(View.VISIBLE);

                    feedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Navigation.findNavController(getView()).navigate(OrdersDirections.actionOrdersToFeedback(orderUser));
                        }
                    });
                }

                if (orderUser.getOrderState().equals("Ordered")) {
                    //feedback.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.VISIBLE);

                }

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductModel productModel = productList.get(getAdapterPosition());
                        productList.remove(productModel);
                        ItemNum.setText(String.valueOf(orderUser.getProductList().size()));

                        if (data != null) {
                            for (int i = 0; i < data.size(); i++) {

                                ProductModel P = data.get(i);
                                double price = ((double) P.getPrice()) / 100;
                                int number = P.getItemNumberInCart();
                                sum += (price * number);
                            }
                        }
                        Total.setText(sum + "JD");
                        orderUser.setTotal(sum);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(view.getContext(), "Item removed", Toast.LENGTH_SHORT).show();
                        UploadFirebase();
                    }
                });


            }
        }


    }
    public void UploadFirebase(){

        orderUser = new OrderModel(Id, orderUser.getUserId(), orderUser.getProductList(), orderUser.getTime(),
                orderUser.getOrderState(),  orderUser.getTotal());

        db.collection("Orders").document(Id)
                .set(orderUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(),""+ e, Toast.LENGTH_LONG).show();
                    }

                });


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
                if(models.get(position).getPrice()== null) {
                    holder.done.setVisibility(View.INVISIBLE);
                    holder.text.setText("not processed");
                }
            }

            @Override
            public int getItemCount() {
                return models == null?0:models.size();
            }

            public class ScanViewHolder extends RecyclerView.ViewHolder {
                ImageView image,delete;
                Button done;
                TextView price;
                EditText text;
                public ScanViewHolder(@NonNull final View itemView) {
                    super(itemView);
                    image = itemView.findViewById(R.id.imageView13);
                    delete = itemView.findViewById(R.id.imageView5);
                    done=itemView.findViewById(R.id.ok);
                    price=itemView.findViewById(R.id.price);
                    text=itemView.findViewById(R.id.text);
                    text.setEnabled(false);


                    if(type.equals("cart")){
                        delete.setVisibility(View.VISIBLE);
                    }

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            models.remove(models.get(getAdapterPosition()));
                            FirebaseFirestore.getInstance().collection("Orders").document(orderUser.getId()).set(orderUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                            notifyDataSetChanged();

                        }
                    });

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<ScanModel> scan=orderUser.getScanModels();
                            final String p=scan.get(getAdapterPosition()).getPrice();
                            double t=Double.parseDouble(p)+orderUser.getTotal();
                            Total.setText(String.valueOf(t));
                            orderUser.setTotal(t);
                           // scan.get(getAdapterPosition()).setPrice(String.valueOf(t));
                            scanModel.setPrice(text.getText().toString());
                            scanModel.setImage(scan.get(getAdapterPosition()).getImage());
                            scanModel.setId(scan.get(getAdapterPosition()).getId());
                            orderUser.setScanModels(scan);
                            adapter2.notifyDataSetChanged();
                            FirebaseFirestore.getInstance().collection("Orders").document(orderUser.getId()).set(orderUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                            notifyDataSetChanged();

                        }
                    });
                }
            }
        }

}


