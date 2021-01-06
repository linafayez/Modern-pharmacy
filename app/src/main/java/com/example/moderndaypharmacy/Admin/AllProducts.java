package com.example.moderndaypharmacy.Admin;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class AllProducts extends Fragment {

    RecyclerView allProducts;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<ProductModel> response;

    public AllProducts() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_products, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allProducts = view.findViewById(R.id.products);
        db= FirebaseFirestore.getInstance();
        Query query = db.collection("Products").orderBy("name");
        response = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<ProductModel, ProductModelHolder>(response) {
            @NonNull
            @Override
            public ProductModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);
                return new ProductModelHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductModelHolder holder, int position, @NonNull ProductModel model) {
                holder.name.setText(model.getName());
                holder.price.setText(""+model.getPrice()/100);
                if (model.getPic()!=null&&model.getPic().get(0)!=null)
                    Picasso.get().load(Uri.parse(model.getPic().get(0))).into(holder.image);

            }
        };
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        allProducts.setLayoutManager(layoutManager);
        allProducts.setHasFixedSize(false);
        allProducts.setAdapter(adapter);

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

    private class ProductModelHolder extends RecyclerView.ViewHolder {
        ImageView image ;
        TextView price, name ;
        public ProductModelHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            name = itemView.findViewById(R.id.name);
            image=itemView.findViewById(R.id.proImage);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Bundle bundle = new Bundle();

                    if(getArguments() != null &&getArguments().getString("type").equals("newAds")){
                        bundle.putString("type", "new");
                        bundle.putString("product",response.getSnapshots().get(getAdapterPosition()).getID());
                        Navigation.createNavigateOnClickListener(R.id.action_allProducts_to_addAds, bundle).onClick(getView());
                    }
                    if(getArguments()!=null&&getArguments().getString("type").equals("editAds")){
                        bundle.putString("type", "editAds");
                        Navigation.createNavigateOnClickListener(R.id.action_allProducts_to_addAds, bundle).onClick(getView());
                    }



                }
            });


        }
    }
}