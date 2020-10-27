package com.example.moderndaypharmacy.User;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moderndaypharmacy.ProductModel;
import com.example.moderndaypharmacy.R;

import java.util.ArrayList;

public class Cart extends Fragment {
    RecyclerView recyclerView;
    SharedPreference sharedPreference;
    ArrayList<ProductModel> data;

    RecyclerView.Adapter adapter;
    static TextView total;

    public Cart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        total = view.findViewById(R.id.total);
        recyclerView = view.findViewById(R.id.recyclerView);
        sharedPreference = new SharedPreference(getContext());
        double sum= 0.0;
        data = new ArrayList<>();
        data = sharedPreference.getCartData();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                ProductModel P = data.get(i);
                if (P.getItemNumberInCart() == 0) {
                    P.setItemNumberInCart(1);
                }
                double price = ((double) P.getPrice()) / 100;
                int number = P.getItemNumberInCart();
                sum += price * number;
            }
        }
        total.setText(sum + "JD");
        adapter = new CartAdapter(data);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);


    }

    public static class changed {
        public void total(double t) {
            double price = Double.parseDouble(total.getText().toString().split("JD")[0]);
            total.setText(price + t + "JD");
        }
    }
}