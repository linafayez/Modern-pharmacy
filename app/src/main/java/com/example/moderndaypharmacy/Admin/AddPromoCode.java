package com.example.moderndaypharmacy.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.moderndaypharmacy.Models.PromoCodeModel;
import com.example.moderndaypharmacy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddPromoCode extends Fragment {
    TextInputLayout PromoCode , Discount,EndAfter;
    Button Add;
    String id;
    ImageView delete;
    FirebaseFirestore db ;
    PromoCodeModel promoCodeModel;
    public AddPromoCode() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_promo_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        delete = view.findViewById(R.id.delete);
        PromoCode = view.findViewById(R.id.voucher);
        EndAfter = view.findViewById(R.id.EndDate);
        Discount= view.findViewById(R.id.Discount);
        Add = view.findViewById(R.id.add);
        promoCodeModel = AddPromoCodeArgs.fromBundle(getArguments()).getCode();
        if(promoCodeModel == null){
            id = db.collection("PromoCode").document().getId();
        }else {
            id = promoCodeModel.getId();
            delete.setVisibility(View.VISIBLE);
            PromoCode.getEditText().setText(promoCodeModel.getCode());
            EndAfter.getEditText().setText(promoCodeModel.getEndTime().toDate().getDay()-promoCodeModel.getTime().toDate().getDay()+"");
            Discount.getEditText().setText(promoCodeModel.getDiscount()+"");
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete PromoCode")
                        .setMessage("Are you sure to delete this PromoCode?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int l) {
                                db.collection("PromoCode").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(),"deleted",Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(getView()).navigateUp();
                                    }
                                });

                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                        .show();
            }
        });
        PromoCode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>=3){
                PromoCode.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PromoCode.getEditText().getText().equals(null) && !Discount.getEditText().getText().equals(null) && !EndAfter.getEditText().getText().equals(null)) {
                    if (PromoCode.getEditText().getText().length() < 3) {
                        PromoCode.setError("The voucher must be more than two char");
                    } else{

                Add.setVisibility(View.INVISIBLE);
                promoCodeModel = new PromoCodeModel(id, Timestamp.now(), new Timestamp(Timestamp.now().getSeconds() + Integer.parseInt(EndAfter.getEditText().getText().toString()) * 60 * 60 * 24, 0), PromoCode.getEditText().getText().toString(), Integer.parseInt(Discount.getEditText().getText().toString()));
                Upload(promoCodeModel);
            }  }else{

                }

            }
        });
    }

    private void Upload(PromoCodeModel promoCodeModel) {
        db.collection("PromoCode").document(id).set(promoCodeModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"Done",Toast.LENGTH_LONG).show();
                Navigation.findNavController(getView()).navigateUp();
            }
        });
    }
}