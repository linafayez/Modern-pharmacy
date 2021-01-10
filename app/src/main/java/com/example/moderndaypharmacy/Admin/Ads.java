package com.example.moderndaypharmacy.Admin;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.AdsModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class Ads extends Fragment {
    RecyclerView ads;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;
    static FirestoreRecyclerOptions<AdsModel> response;

    public Ads() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        ads = view.findViewById(R.id.ads);
        Query query = db.collection("Ads");
        response = new FirestoreRecyclerOptions.Builder<AdsModel>()
                .setQuery(query, AdsModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<AdsModel, AdsHolder>(response) {
            @NonNull
            @Override
            public AdsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads, parent, false);
                return new AdsHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AdsHolder holder, int position, @NonNull AdsModel model) {
                Picasso.get().load(Uri.parse(model.getAdsImage())).into(holder.image);

            }
        };
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        ads.setLayoutManager(layoutManager);
        ads.setHasFixedSize(false);
        ads.setAdapter(adapter);

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

    public static class AdsHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public AdsHolder(@NonNull final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        bundle.putString("type",gson.toJson(response.getSnapshots().get(getAdapterPosition())));
                        Navigation.createNavigateOnClickListener(R.id.action_ads_to_addAds,bundle).onClick(itemView);

                }
        });
    }
}}
