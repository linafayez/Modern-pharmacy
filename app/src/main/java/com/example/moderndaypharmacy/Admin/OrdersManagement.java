package com.example.moderndaypharmacy.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moderndaypharmacy.Models.OrderModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OrdersManagement extends Fragment {
    RecyclerView Orders;
    FirebaseFirestore db ;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<OrderModel> response;
    public OrdersManagement() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
       Orders = view.findViewById(R.id.orders);
        db= FirebaseFirestore.getInstance();
        Query query = db.collection("Orders");
        response = new FirestoreRecyclerOptions.Builder<OrderModel>()
                .setQuery(query, OrderModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<OrderModel, OrderHolder>(response) {
            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order,parent,false);
                return new OrderHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull OrderModel model) {
                   }
        };
        Orders.setLayoutManager( new LinearLayoutManager(getContext()));
        Orders.setHasFixedSize(false);
        Orders.setAdapter(adapter);

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
    class OrderHolder extends RecyclerView.ViewHolder{

        public OrderHolder(@NonNull View itemView) {
            super(itemView);


        }

    }
}