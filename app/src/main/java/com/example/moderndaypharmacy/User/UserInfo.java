package com.example.moderndaypharmacy.User;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.example.moderndaypharmacy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;

public class UserInfo extends Fragment {
    TextInputLayout name,email,phone , Gender , Address;
    Button submit;
    UserInfoModel user;
    FirebaseFirestore db;
    String UID;
    StorageReference mStorageRef;
    ImageView imageView;
     Uri ImageUri;

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
imageView = view.findViewById(R.id.imageView6);
imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        }
        submit.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(EXTRA_ALLOW_MULTIPLE, false);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }
});
final SharedPreference sharedPreference = new SharedPreference(getContext());
        mStorageRef = FirebaseStorage.getInstance().getReference("Users");
        user = UserInfoArgs.fromBundle(getArguments()).getUser();
        if(user !=null) {
            UID = user.getId();
            name.getEditText().setText(user.getName());
            email.getEditText().setText(user.getEmail());
            phone.getEditText().setText(user.getPhoneNumber());
            Gender.getEditText().setText(user.getGender());
            Address.getEditText().setText(user.getAddress());
            if(user.getImage() != null){
                Picasso.get().load(Uri.parse(user.getImage())).into(imageView);

            }
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
                            sharedPreference.addUser(user);
                            Navigation.findNavController(getView()).navigateUp();
                        }
                    });
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && null != data){
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
          //  user.setImage(ImageUri+"");
            final StorageReference childRef = mStorageRef.child(user.getId());
            childRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //    Toast.makeText(UploadProduct.this,"Done Upload Image",Toast.LENGTH_LONG).show();
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            user.setImage(uri+"");
                            submit.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        }
    }
}