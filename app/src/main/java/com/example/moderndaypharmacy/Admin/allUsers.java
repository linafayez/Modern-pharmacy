package com.example.moderndaypharmacy.Admin;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.example.moderndaypharmacy.R;
import com.example.moderndaypharmacy.User.Products;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class allUsers extends Fragment {
    RecyclerView Users;
    FirebaseFirestore db ;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<UserInfoModel> response;
    public allUsers() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Users = view.findViewById(R.id.users);
        db= FirebaseFirestore.getInstance();
        Query query = db.collection("Users");
        response = new FirestoreRecyclerOptions.Builder<UserInfoModel>()
                .setQuery(query, UserInfoModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<UserInfoModel, UserInfoModelHolder>(response) {
            @NonNull
            @Override
            public UserInfoModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user,parent,false);
                return new UserInfoModelHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserInfoModelHolder holder, int position, @NonNull UserInfoModel model) {
               holder.name.setText(model.getName());
               holder.phone.setText(model.getPhoneNumber());
               holder.email.setText(model.getEmail());
             //   Picasso.get().load(Uri.parse(model.get)).into(holder.image);
            }
        };
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        Users.setLayoutManager(layoutManager);
        Users.setHasFixedSize(false);
        Users.setAdapter(adapter);

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

    private class UserInfoModelHolder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        TextView phone, name , email;
        public UserInfoModelHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.phone);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);

        }
    }
}