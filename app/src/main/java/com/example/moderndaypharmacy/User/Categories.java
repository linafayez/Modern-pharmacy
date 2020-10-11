package com.example.moderndaypharmacy.User;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class Categories extends Fragment {
    private static final String TAG = "Categories";
RecyclerView recyclerView;
    FirebaseFirestore db ;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<CategoryModel> response;
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
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.category);
        Query query = db.collection("Category");
        response = new FirestoreRecyclerOptions.Builder<CategoryModel>()
                .setQuery(query, CategoryModel.class)
                .build();
         adapter = new FirestoreRecyclerAdapter<CategoryModel, CategoryHolder>(response) {
            @NonNull
            @Override
            public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category,parent,false);
                return new CategoryHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CategoryHolder holder, int position, @NonNull CategoryModel model) {
//                holder.name.setText(model.getName());
                Picasso.get().load(Uri.parse(model.getImage())).into(holder.image);


            }
        };
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

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
    public class CategoryHolder extends RecyclerView.ViewHolder{
//        TextView name;
        ImageView image;
        public CategoryHolder(@NonNull final View itemView) {
            super(itemView);
//            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Bundle bundle = new Bundle();
                    if(getArguments().getString("type").equals("editCategory")) {
                        Gson gson = new Gson();
                        String category = gson.toJson(response.getSnapshots().get(getAdapterPosition()));
                        bundle.putString("category", category);
                        Navigation.createNavigateOnClickListener(R.id.action_categories_to_addCategory, bundle).onClick(getView());
                    }
                    if(getArguments().getString("type").equals("newProduct")) {
                        bundle.putString("type", "new");
                        bundle.putString("category",response.getSnapshots().get(getAdapterPosition()).getId());
                        Navigation.createNavigateOnClickListener(R.id.action_categories_to_addProduct, bundle).onClick(getView());
                        Navigation.findNavController(getView()).navigateUp();
                    }
                }
            });
        }
    }
}