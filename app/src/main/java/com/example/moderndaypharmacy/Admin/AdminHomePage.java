package com.example.moderndaypharmacy.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.moderndaypharmacy.Models.PromoCodeModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdminHomePage extends Fragment {
Button newProduct,newCategory, editCategory , newPromoCode , editProduct,newAds,manageQueries,editAds,reviewFeedback;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<PromoCodeModel> response;
    public AdminHomePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newProduct = view.findViewById(R.id.newProduct);
        editProduct= view.findViewById(R.id.editProduct);
        reviewFeedback = view.findViewById(R.id.feedBack);
        newPromoCode = view.findViewById(R.id.promoCode);
        newCategory= view.findViewById(R.id.newCategory);
        editCategory = view.findViewById(R.id.editCategory);
        newAds=view.findViewById(R.id.newAds);
        manageQueries=view.findViewById(R.id.manageQueries);
        editAds=view.findViewById(R.id.editAds);
         reviewFeedback.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Navigation.findNavController(getView()).navigate(R.id.action_adminHomePage_to_reviewFeedback);
             }
         });
        final Bundle bundle4 = new Bundle();
        newAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle4.putString("type","newAds");
                Navigation.findNavController(getView()).navigate(R.id.action_adminHomePage_to_allProducts,bundle4);
            }
        });
        final Bundle bundle1 = new Bundle();
        editAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle1.putString("type","editAds");
                Navigation.findNavController(getView()).navigate(R.id.action_adminHomePage_to_ads,bundle1);
            }
        });

        manageQueries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_adminHomePage_to_manageQueries);

            }
        });


        final Bundle bundle = new Bundle();
        newProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","newProduct");
                Navigation.findNavController(getView()).navigate(R.id.action_adminHomePage_to_categories,bundle);
            }
        });
        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("type","editProduct");
                Navigation.findNavController(getView()).navigate(R.id.action_adminHomePage_to_categories,bundle);

            }
        });
//        bundle.putString("type","newProduct");
//        View.OnClickListener s = Navigation.createNavigateOnClickListener(R.id.action_adminHomePage_to_categories,bundle);
//        newProduct.setOnClickListener(s);
        final Bundle bundle3 = new Bundle();
        bundle3.putString("category","new");
        View.OnClickListener c = Navigation.createNavigateOnClickListener(R.id.action_adminHomePage_to_addCategory,bundle3);
        newCategory.setOnClickListener(c);
        final Bundle bundle2 = new Bundle();
        bundle2.putString("type","editCategory");
        View.OnClickListener edit = Navigation.createNavigateOnClickListener(R.id.action_adminHomePage_to_categories,bundle2);
        editCategory.setOnClickListener(edit);
        newPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_adminHomePage_to_allPromoCode);
            }
        });


    }
}