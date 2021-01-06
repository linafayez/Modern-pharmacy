package com.example.moderndaypharmacy.User;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Admin.Ads;
import com.example.moderndaypharmacy.Models.AdsModel;
import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.littlemango.stacklayoutmanager.StackLayoutManager;
import com.squareup.picasso.Picasso;

public class HomePage extends Fragment {
    LinearLayout product,query;
    CardView search;
    RecyclerView ads;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<AdsModel> response;
    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        query=view.findViewById(R.id.query);

        final Bundle bundle = new Bundle();
        bundle.putString("newQ","new");
        View.OnClickListener q= Navigation.createNavigateOnClickListener(R.id.action_homePage_to_query,bundle);
        query.setOnClickListener(q);
        ads = view.findViewById(R.id.Ads);
        Query query = db.collection("Ads");
        response = new FirestoreRecyclerOptions.Builder<AdsModel>()
                .setQuery(query, AdsModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<AdsModel, AdsH>(response) {
            @Override
            protected void onBindViewHolder(@NonNull AdsH holder, int position, @NonNull AdsModel model) {
                Picasso.get().load(Uri.parse(model.getAdsImage())).into(holder.image);
            }

            @NonNull
            @Override
            public AdsH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads, parent, false);
                return new AdsH(view);
            }


        };
        StackLayoutManager manager = new StackLayoutManager();
        ads.setLayoutManager(manager);
        ads.setHasFixedSize(false);
        ads.setAdapter(adapter);
      product = view.findViewById(R.id.products);
      product.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Navigation.findNavController(getView()).navigate(R.id.action_homePage_to_products);
          }
      });
        search = view.findViewById(R.id.s);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_homePage_to_search2);
            }
        });

     /*
        query=view.findViewById(R.id.query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_homePage_to_query);
            }
        });
*/


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

    public class AdsH extends RecyclerView.ViewHolder {
        public ImageView image;

        public AdsH(@NonNull final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore.getInstance().collection("Products").document(response.getSnapshots().get(getAdapterPosition()).getProId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.toObject(ProductModel.class) != null)
                                Navigation.findNavController(getView()).navigate(HomePageDirections.actionHomePageToProductView(documentSnapshot.toObject(ProductModel.class)));
                        }
                    });

                }
            });
        }
    }}