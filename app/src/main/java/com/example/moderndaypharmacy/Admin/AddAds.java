package com.example.moderndaypharmacy.Admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.moderndaypharmacy.Models.AdsModel;
import com.example.moderndaypharmacy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;


public class AddAds extends Fragment {

    AdsModel adsModel;
    ImageView image,delete;
    TextView textView,id,productId;
    Button Upload;
    String ID, ImageLink,proId;
    Uri ImageUri;
    ProgressDialog pd;
    ProgressBar progressBar;
    private StorageReference mStorageRef;
    FirebaseFirestore db;


    public AddAds() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ads, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image = view.findViewById(R.id.imageView3);
        Upload = view.findViewById(R.id.upload);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textView5);
        delete = view.findViewById(R.id.delete);
        id=view.findViewById(R.id.id);
        productId=view.findViewById(R.id.proId);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading....");
        db = FirebaseFirestore.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference("AdsImage");
        String ads = getArguments().getString("type");

        if(ads.equals("new"))
        {
            proId=getArguments().getString("product");
            id.setText(getArguments().getString("product"));
            ID= db.collection("Ads").document().getId();

        }
        else
        {
            Gson gson = new Gson();
            adsModel=gson.fromJson(ads,AdsModel.class);
            textView.setText("Change Ads Data");
            ID= adsModel.getAdsId();
            Picasso.get().load(Uri.parse(adsModel.getAdsImage())).into(image);
            proId=adsModel.getProId();
            id.setText(adsModel.getProId());
            productId.setText("Product Id");
            id.setVisibility(View.VISIBLE);
            productId.setVisibility(View.VISIBLE);
            Upload.setText("Update Data");
            delete.setVisibility(View.VISIBLE);
            Upload.setVisibility(View.VISIBLE);


        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAlert();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(EXTRA_ALLOW_MULTIPLE, false);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });


        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Upload.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                UploadImage();
            }
        });

     }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && null != data)
        {
            ImageUri=data.getData();
            image.setImageURI(ImageUri);
            Upload.setVisibility(View.VISIBLE);
            id.setVisibility(View.VISIBLE);
            productId.setVisibility(View.VISIBLE);

        }
    }
    public void UploadImage(){
         pd.show();
        final StorageReference childRef = mStorageRef.child(ID);

        childRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageLink = String.valueOf(uri);
                        UploadFirebase();
                    }
                });
            }
        });
    }
    public void UploadFirebase(){
        adsModel = new AdsModel(""+ID,ImageLink,proId);

        db.collection("Ads").document(ID)
                .set(adsModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getActivity(),"Upload Done",Toast.LENGTH_SHORT).show();
                         pd.dismiss();
                        Navigation.findNavController(getView()).navigateUp();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(),""+ e, Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                });


    }
    public void displayAlert(){

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Ads")
                .setMessage("Are you sure to delete this Ads?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.collection("Ads").document(ID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getActivity(),"done deleted all ads information ",Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getView()).navigateUp();
                        } });
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        })
                .show();

    }


}

