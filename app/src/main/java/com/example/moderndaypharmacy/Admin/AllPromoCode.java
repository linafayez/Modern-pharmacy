package com.example.moderndaypharmacy.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moderndaypharmacy.Models.PromoCodeModel;
import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class AllPromoCode extends Fragment {
    RecyclerView PromoCode;
    FirebaseFirestore db ;
    ImageView add;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<PromoCodeModel> response;
    public AllPromoCode() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_promo_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PromoCode = view.findViewById(R.id.all);
        add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(AllPromoCodeDirections.actionAllPromoCodeToAddPromoCode(null));
            }
        });
        db= FirebaseFirestore.getInstance();
        Query query = db.collection("PromoCode");
        response = new FirestoreRecyclerOptions.Builder<PromoCodeModel>()
                .setQuery(query, PromoCodeModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<PromoCodeModel, PromoCodeModelHolder>(response) {
            @NonNull
            @Override
            public PromoCodeModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promo_code,parent,false);
                return new PromoCodeModelHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PromoCodeModelHolder holder, int position, @NonNull PromoCodeModel model) {
                holder.Discount.setText(model.getDiscount()+"%");
                holder.code.setText(model.getCode());
               holder.create.setText(model.getTime().toDate()+"");
               holder.end.setText(model.getEndTime().toDate()+"");
            }
        };
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        PromoCode.setLayoutManager(layoutManager);
        PromoCode.setHasFixedSize(false);
        PromoCode.setAdapter(adapter);

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

    private class PromoCodeModelHolder extends RecyclerView.ViewHolder {
        TextView Discount, code , create , end;
        public PromoCodeModelHolder(@NonNull final View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.code);
            Discount = itemView.findViewById(R.id.Discount);
            create = itemView.findViewById(R.id.create);
            end = itemView.findViewById(R.id.end);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(getView()).navigate(AllPromoCodeDirections.actionAllPromoCodeToAddPromoCode(response.getSnapshots().get(getAdapterPosition())));
                }
            });
        }
    }
}