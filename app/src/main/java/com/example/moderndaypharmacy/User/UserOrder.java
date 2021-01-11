package com.example.moderndaypharmacy.User;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.OrderModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserOrder extends Fragment {
RecyclerView order;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<OrderModel> options;
    public UserOrder() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        order = view.findViewById(R.id.orders);
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("Orders").whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        options = new FirestoreRecyclerOptions.Builder<OrderModel>()
                .setQuery(query,OrderModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<OrderModel, OrderViewHolder >(options){

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_orders_cart, parent, false);
                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull OrderModel model) {
                holder.total.setText(model.getTotal()+"JD");
               holder.state.setText(model.getOrderState());
               if(model.getProductList() != null && model.getProductList().size()!= 0 && model.getProductList().get(0).getPic().get(0)!= null )
               Picasso.get().load(Uri.parse(model.getProductList().get(0).getPic().get(0))).into(holder.image);
               holder.id.setText("#OrderId:"+" "+model.getId().substring(0,7));
             //   holder.Date.setText(model.getTime().toDate()+"");
                Date date=model.getTime().toDate();
               // DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
                holder.Date.setText(DateFormat.getDateInstance().format(date));
                holder.time.setText(DateFormat.getTimeInstance().format(date));
                //   holder.Items.setText(TextViewUtil.ItemsName(model.getProductsModels()));



            }
        };
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        order.setLayoutManager(manager);
        order.setHasFixedSize(false);
        order.setAdapter(adapter);


    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView Date, Items , total , state,id,time;
        ImageView image;
        ArrayList products;
        Button userOrder;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
           Date = itemView.findViewById(R.id.date);
           time=itemView.findViewById(R.id.time);
            //    Items = itemView.findViewById(R.id.items);
            total = itemView.findViewById(R.id.textView12);
            state= itemView.findViewById(R.id.OrderState);
            image= itemView.findViewById(R.id.imageView5);
            userOrder=itemView.findViewById(R.id.order);
            id= itemView.findViewById(R.id.Id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(getView()).navigate(UserOrderDirections.actionUserOrderToFeedback(options.getSnapshots().get(getAdapterPosition())));
                }
            });
            userOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(getView()).navigate(UserOrderDirections.actionUserOrderToOrders(options.getSnapshots().get(getAdapterPosition())));
                }
            });

        }
    }


}