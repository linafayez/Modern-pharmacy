package com.example.moderndaypharmacy.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.Models.ScanModel;
import com.example.moderndaypharmacy.R;
import com.example.moderndaypharmacy.Util.TextViewUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Cart extends Fragment {
    RecyclerView recyclerView,ScanView;
    SharedPreference sharedPreference;
    ArrayList<ProductModel> data , data2;
    ArrayList<ScanModel> models;
Button checkout;
TextView text,label;

    RecyclerView.Adapter adapter,scanAdapter;
    static TextView total;
    static DecimalFormat df2 = new DecimalFormat("#.##");
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
text = view.findViewById(R.id.l);
label = view.findViewById(R.id.ll);
        models= new ArrayList<>();
        ScanView = view.findViewById(R.id.scan);
        total = view.findViewById(R.id.total);
        checkout = view.findViewById(R.id.checkout);
        recyclerView = view.findViewById(R.id.recyclerView);
        sharedPreference = new SharedPreference(getContext());
        data = new ArrayList<>();
        data = sharedPreference.getCartData();
        data2 = new ArrayList<>();
        double sum= 0.0;
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager manager2 = new LinearLayoutManager(getContext());
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {

//                if(!data.get(i).getID().equals(data2.get(i).getID())){
//                    sharedPreference.deleteProduct(data.get(i));
//                }
                ProductModel P = data.get(i);
                if (P.getItemNumberInCart() == 0) {
                    P.setItemNumberInCart(1);
                }
                double price = ((double) P.getPrice()) / 100;
                int number = P.getItemNumberInCart();
                sum += price * number;
            }
        }

        total.setText(df2.format(sum) + "JD");
        adapter = new CartAdapter(data);
        scanAdapter = new ScanAdapter(models, getContext(),"cart");
        ScanView.setLayoutManager(manager2);
        ScanView.setHasFixedSize(false);
        ScanView.setAdapter(scanAdapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_cart_to_checkout);
            }
        });
        models = sharedPreference.getCartScanData();
        if(models != null  || models.size()==0){
            text.setVisibility(View.INVISIBLE);
        }else {
            if(data != null && data.size() ==0){
                label.setVisibility(View.INVISIBLE);
            }
        }

        if(data!=null) {
            for (int i = 0; i < data.size(); i++) {
                final int finalI = i;
                FirebaseFirestore.getInstance().collection("Products").document(data.get(i).getID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //   ProductModel pro = documentSnapshot.toObject(ProductModel.class);
                        if (documentSnapshot != null) {
                            data2.add(documentSnapshot.toObject(ProductModel.class));
                        } else {
                            changed.total(-data.get(finalI).getItemNumberInCart() * ((double) data.get(finalI).getPrice()) / 100);
                            sharedPreference.deleteProduct(data.get(finalI));
                            adapter = new CartAdapter(data);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }


    }


    public static class changed {
        public static void total(double t) {
            double price = Double.parseDouble(total.getText().toString().split("JD")[0]);
            total.setText(df2.format(price + t )+ "JD");

        }



    }

}

