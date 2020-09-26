package com.example.moderndaypharmacy.User;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderndaypharmacy.CategoryModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class Categories extends Fragment {
    FirestoreRecyclerOptions<CategoryModel> options;
    FirestoreRecyclerAdapter adapter;
    public Categories() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.category);
        FirebaseFirestore fire = FirebaseFirestore.getInstance();
        Query query = fire.collection("Category");
        options = new FirestoreRecyclerOptions.Builder<CategoryModel>()
                .setQuery(query,CategoryModel.class)
                .build();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
         adapter = new FirestoreRecyclerAdapter<CategoryModel, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i, @NonNull CategoryModel categoryModel) {
                categoryViewHolder.name.setText(categoryModel.getName());
                Picasso.get().load(Uri.parse(categoryModel.getImage())).into(categoryViewHolder.image);


            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
                return new CategoryViewHolder(view);
            }
             @Override
             public void onError(FirebaseFirestoreException e) {

                 Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
             }


        };

//    RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),3);
//        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }

static class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    ImageView image;
    public CategoryViewHolder(@NonNull View view) {
        super(view);
        name = view.findViewById(R.id.name);
        image = view.findViewById(R.id.image);
    }
}
}