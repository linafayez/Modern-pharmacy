package com.example.moderndaypharmacy.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.OrderModel;
import com.example.moderndaypharmacy.R;
import com.example.moderndaypharmacy.Util.TextViewUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OrdersManagement extends Fragment {
    RecyclerView order;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<OrderModel> options;
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
       order = view.findViewById(R.id.orders);
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("Orders").orderBy("time", Query.Direction.DESCENDING);

        options = new FirestoreRecyclerOptions.Builder<OrderModel>()
                .setQuery(query,OrderModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<OrderModel, OrderHolder>(options){

            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order, parent, false);
                return new OrderHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull OrderModel model) {
                holder.OrderState.setText(model.getOrderState());
                if(model.getProductList()!=null&&model.getProductList().size()>0)
                holder.Items.setText(TextViewUtil.ItemsName(model.getProductList()));
                holder.total.setText(model.getTotal()+"JD");
                holder.orderId.setText("Order ID: "+model.getId().subSequence(0,10));
                holder.UserId.setText("User ID: "+model.getUserId().subSequence(0,10));
            }
        };

        //  RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        order.setLayoutManager( new LinearLayoutManager(getContext()));
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
    class OrderHolder extends RecyclerView.ViewHolder{
        TextView Date, Items , total,time, orderId,UserId , OrderState;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.id);
            UserId = itemView.findViewById(R.id.userId);
            OrderState = itemView.findViewById(R.id.OrderState);
            time = itemView.findViewById(R.id.time);
            Items = itemView.findViewById(R.id.productList);
            Date = itemView.findViewById(R.id.date);
            total = itemView.findViewById(R.id.total);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(getView()).navigate(OrdersManagementDirections.actionOrdersManagementToOrderProcess(options.getSnapshots().get(getAdapterPosition())));

                }
            });


        }

    }
}