package com.example.moderndaypharmacy.Admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderndaypharmacy.Models.CategoryModel;
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


public class AddCategory extends Fragment {
    EditText name,desc;
    ImageView image , delete;
    Button Upload;
    TextView textView;
    ProgressBar progressBar;
    Uri ImageUri;
    String uniqueID, ImageLink;
    private StorageReference mStorageRef;
    FirebaseFirestore db ;
    ProgressDialog pd;
    CategoryModel categoryModel;

    public AddCategory() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance().getReference("Category");
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(getContext());
        textView = view.findViewById(R.id.textView2);
        pd.setMessage("Uploading....");
        image = view.findViewById(R.id.imageView3);
        desc = view.findViewById(R.id.desc);
        delete = view.findViewById(R.id.delete);
        name = view.findViewById(R.id.name);
        Upload = view.findViewById(R.id.UploadCategory);
        progressBar = view.findViewById(R.id.progressBar);
        String category = getArguments().getString("category");
        if(category.equals("new")){
            uniqueID = db.collection("Category").document().getId();
        }else {
            textView.setText("Change Category Data");
            Gson gson = new Gson();
            categoryModel = gson.fromJson(category, CategoryModel.class);
            uniqueID = categoryModel.getId();
            name.setText(categoryModel.getName());
            desc.setText(categoryModel.getDesc());
            Picasso.get().load(Uri.parse(categoryModel.getImage())).into(image);
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
            public void onClick(View view) {
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
            public void onClick(View view) {
                Upload.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                UploadImage();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && null != data){
            ImageUri = data.getData();
            image.setImageURI(ImageUri);
           Upload.setVisibility(View.VISIBLE);
        }
    }
    public void UploadImage(){
        pd.show();
            final StorageReference childRef = mStorageRef.child(uniqueID);
            childRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //    Toast.makeText(UploadProduct.this,"Done Upload Image",Toast.LENGTH_LONG).show();
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
        categoryModel = new CategoryModel(""+uniqueID,name.getText().toString(),desc.getText().toString(),ImageLink);
        db.collection("Category").document(uniqueID)
                .set(categoryModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getActivity(),"done",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        Navigation.findNavController(getView()).navigateUp();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(),""+ e,Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }

                });
    }
    public void displayAlert(){

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Category")
                .setMessage("Are you sure to delete this Category?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.collection("Category").document(uniqueID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(),"deleted",Toast.LENGTH_SHORT).show();
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

}