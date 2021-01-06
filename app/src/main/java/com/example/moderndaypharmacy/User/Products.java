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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class Products extends Fragment {
   RecyclerView Products;
    FirebaseFirestore db ;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions <ProductModel> response;
   public Products() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Products = view.findViewById(R.id.products);
        db= FirebaseFirestore.getInstance();
        Query query = db.collection("Products");
        response = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<ProductModel, ProductHolder>(response) {
            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products,parent,false);
                return new ProductHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull ProductModel model) {
                     holder.name.setText(model.getName());
                    holder.price.setText(""+model.getPrice()/100);
               // if(model.getProductList() != null && model.getProductList().size()!= 0 && model.getProductList().get(0).getPic().get(0)!= null )
                    if (model !=null&&model.getPic()!=null)
                    Picasso.get().load(Uri.parse(model.getPic().get(0))).into(holder.image);
            }
        };
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        Products.setLayoutManager(layoutManager);
        Products.setHasFixedSize(false);
        Products.setAdapter(adapter);

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
    class ProductHolder extends RecyclerView.ViewHolder{
       SharedPreference sharedPreference ;
TextView name , price;
ImageView image;
Button addToCart;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            sharedPreference = new SharedPreference(itemView.getContext());
            addToCart =itemView.findViewById(R.id.addToCart);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.proImage);
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductModel model = response.getSnapshots().get(getAdapterPosition());
                    if(model.getItemNumberInCart() == 0){
                        model.setItemNumberInCart(1);
                    }
                    sharedPreference.addToCart(model);
                }
            });
            final Bundle bundle = new Bundle();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!getArguments().equals(null) && getArguments().get("type").equals("editProduct")){
                        Gson gson = new Gson();
                        bundle.putString("type", gson.toJson(response.getSnapshots().get(getAdapterPosition())));
                        Navigation.findNavController(getView()).navigate(R.id.action_products2_to_addProduct,bundle);
                    }
                }
            });
            image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigation.findNavController(getView()).navigate(ProductsDirections.actionProductsToProductView(response.getSnapshots().get(getAdapterPosition())));
                    }
                });


        }

    }
}