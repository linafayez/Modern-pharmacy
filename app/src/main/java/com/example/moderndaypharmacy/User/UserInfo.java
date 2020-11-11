package com.example.moderndaypharmacy.User;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.example.moderndaypharmacy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfo extends Fragment {
    TextInputLayout name,email,phone , Gender , Address;
    Button submit;
    UserInfoModel user;
    FirebaseFirestore db;
    String UID;
    public UserInfo() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.name);
        Gender = view.findViewById(R.id.Gender);
        Address = view.findViewById(R.id.Address);
        db = FirebaseFirestore.getInstance();
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        submit= view.findViewById(R.id.Update);


        user = UserInfoArgs.fromBundle(getArguments()).getUser();
        if(user !=null) {
            UID = user.getId();
            name.getEditText().setText(user.getName());
            email.getEditText().setText(user.getEmail());
            phone.getEditText().setText(user.getPhoneNumber());
            Gender.getEditText().setText(user.getGender());
            Address.getEditText().setText(user.getAddress());
        }else {
            user = new UserInfoModel();
            UID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
            user.setId(UID);
            user.setType("User");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    user.setName(name.getEditText().getText().toString());
                    user.setPhoneNumber(phone.getEditText().getText().toString());
                    user.setEmail(email.getEditText().getText().toString());
                    user.setGender(Gender.getEditText().getText().toString());
                    user.setAddress(Address.getEditText().getText().toString());
                    db.collection("Users").document(UID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"Thank you",Toast.LENGTH_LONG).show();
                            Navigation.findNavController(getView()).navigateUp();
                        }
                    });
            }
        });


    }
}