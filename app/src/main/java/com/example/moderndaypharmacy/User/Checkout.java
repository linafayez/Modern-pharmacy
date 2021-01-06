package com.example.moderndaypharmacy.User;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.moderndaypharmacy.Models.CamModel;
import com.example.moderndaypharmacy.Models.OrderModel;
import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.R;
import com.example.moderndaypharmacy.User.SharedPreference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class Checkout extends Fragment {
    RecyclerView items;
    OrderModel orderModel;

    ArrayList<ProductModel> data;
    SharedPreference sharedPreference;
    EditText voucher , note;

    Button addAdders, Order, ApplyVoucher;
    Date date;
    double latitude;
    double longitude;
    static TextView total, subTotal, shipping, address;

    public Checkout() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      Order = view.findViewById(R.id.Order);
    //  tickerView = view.findViewById(R.id.tickerView);
      note = view.findViewById(R.id.note);
      total = view.findViewById(R.id.total);
    //  tickerView.setCharacterLists(TickerUtils.provideNumberList());
      ApplyVoucher = view.findViewById(R.id.button5);
      voucher = view.findViewById(R.id.voucher);
      items = view.findViewById(R.id.items);
      subTotal = view.findViewById(R.id.subtotal);
      addAdders = view.findViewById(R.id.addAdders);
      shipping = view.findViewById(R.id.shipping);
      address = view.findViewById(R.id.address);
      sharedPreference = new SharedPreference(getContext());
      data = new ArrayList <>();
    data = sharedPreference.getCartData();
    double sum=0.0;
    for(int i=0;i< data.size();i++){
        sum+=data.get(i).getPrice()/100.0 * data.get(i).getItemNumberInCart();

    }
      total.setText(sum + 3 + "JD");
      subTotal.setText(sum+ "JD");
      //total.setText();
      shipping.setText("3JD");
      RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
//      MyCart.changed.setTotal(data);
////      adapter = new ItemsAdapter(getContext(), data);
//      items.setLayoutManager(manager);
//      items.setHasFixedSize(false);
//      items.setAdapter(adapter);
//      ApplyVoucher.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          getPromoCode();
//        }
//      });
      Order.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          date = new Date();
          Timestamp timestamp = new Timestamp(date);
          String id= FirebaseFirestore.getInstance().collection("Orders").document().getId();
            orderModel = new OrderModel(id, FirebaseAuth.getInstance().getCurrentUser().getUid(),data,timestamp,"con",10.0);

            UploadOrder(orderModel);

          // orderModel.setTime(timestamp);
          //UploadOrder(orderModel);
        }
      });
      addAdders.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

       Navigation.findNavController(getView()).navigate(R.id.action_checkout_to_mapFragment);
        }
      });
      addAdders.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Navigation.findNavController(getView()).navigate(R.id.action_checkout_to_camera2);

          }
      });

    }

  private void UploadOrder(OrderModel orderModel) {

    FirebaseFirestore.getInstance().collection("Orders").document(orderModel.getId()).set(orderModel).addOnSuccessListener(new OnSuccessListener <Void>() {
        @Override
        public void onSuccess(Void aVoid) {

            sharedPreference.SaveCart(new ArrayList<ProductModel>());
           Navigation.findNavController(getView()).navigateUp();
           sharedPreference.SaveCamera(new ArrayList <CamModel>());
           Navigation.findNavController(getView()).navigateUp();


        }
    });

  }
}






