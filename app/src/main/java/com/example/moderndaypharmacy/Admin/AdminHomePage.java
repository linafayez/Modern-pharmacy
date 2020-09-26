package com.example.moderndaypharmacy.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.moderndaypharmacy.R;

import java.util.Objects;

public class AdminHomePage extends Fragment {
Button newProduct,newCategory, editCategory;
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
        newCategory= view.findViewById(R.id.newCategory);
        editCategory = view.findViewById(R.id.editCategory);
        final Bundle bundle = new Bundle();
        bundle.putString("type","new");
        View.OnClickListener s = Navigation.createNavigateOnClickListener(R.id.action_adminHomePage_to_addProduct,bundle);
        newProduct.setOnClickListener(s);
        View.OnClickListener c = Navigation.createNavigateOnClickListener(R.id.action_adminHomePage_to_addCategory,bundle);
        newCategory.setOnClickListener(c);
        final Bundle bundle2 = new Bundle();
        bundle2.putString("type","edit");
        View.OnClickListener edit = Navigation.createNavigateOnClickListener(R.id.action_adminHomePage_to_categories,bundle2);
        editCategory.setOnClickListener(edit);

    }
}