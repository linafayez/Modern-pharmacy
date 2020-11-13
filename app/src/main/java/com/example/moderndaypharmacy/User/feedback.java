package com.example.moderndaypharmacy.User;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.moderndaypharmacy.Models.FeedbackModel;
import com.example.moderndaypharmacy.Models.OrderModel;
import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class feedback extends Fragment {
    static String UID ;
    RecyclerView products;
    FeedBackAdapter adapter;
    static ArrayList<ProductModel> data;
    static OrderModel orderModel;
    static FeedbackModel feedbackModel;
    static ArrayList<ProductModel> models , pro;
    FloatingActionButton done;
    ProgressBar progressBar;
    public feedback() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar2);
        done = view.findViewById(R.id.done);
        products = view.findViewById(R.id.products);
    }
}