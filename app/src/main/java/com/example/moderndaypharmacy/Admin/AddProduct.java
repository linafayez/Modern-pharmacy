package com.example.moderndaypharmacy.Admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddProduct extends Fragment {
    EditText name, price, itemNumber, desc;
    TextView textView;
    ImageView delete;
    Button addImage, UploadProduct, UploadImage;
    ProgressBar progressBar;
    Uri ImageUri;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    private ArrayList<String> listImage= new ArrayList<String>();
    String uniqueID;
    int uploadCont = 0;
    HorizontalScrollView switchImage;
    LinearLayout imageGallery;
    String category_id;
    private StorageReference mStorageRef;

    FirebaseFirestore db ;
    ProgressDialog pd;
    ProductModel product;
    public AddProduct() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         db = FirebaseFirestore.getInstance();
        switchImage = view.findViewById(R.id.switchImage);
        imageGallery = (LinearLayout) view.findViewById(R.id.imageGallery);
        name = view.findViewById(R.id.name);
        addImage = view.findViewById(R.id.AddImage);
        UploadImage = view.findViewById(R.id.UploadImage);
        progressBar = view.findViewById(R.id.progressBar);
        UploadProduct = view.findViewById(R.id.UploadProduct);
        textView = view.findViewById(R.id.textView);
        mStorageRef = FirebaseStorage.getInstance().getReference("Products");
        price = view.findViewById(R.id.price);
        itemNumber = view.findViewById(R.id.itemNumber);
        delete = view.findViewById(R.id.delete);
        desc = view.findViewById(R.id.desc);

        pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading....");
        String type = getArguments().getString("type");
        if (type.equals("new")) {
            category_id =getArguments().getString("category");
            delete.setVisibility(View.INVISIBLE);
            uniqueID = db.collection("Products").document().getId();
        } else {

            Gson gson = new Gson();
           product = gson.fromJson(type,ProductModel.class);
            category_id = product.getCategory_id();
            uniqueID = product.getID();
            textView.setText("Edit or delete product");
            addImage.setText("Edit Image");
            UploadProduct.setText("Update Data");
            name.setText(product.getName());
            price.setText(""+product.getPrice()*100);
            desc.setText(product.getDesc());
            itemNumber.setText(product.getItemNumber()+"");
            listImage = product.getPic();
            itemNumber.setText("" + product.getItemNumber());
            for (int i = 0; i < product.getPic().size(); i++) {
                mArrayUri.add(Uri.parse(product.getPic().get(i)));
            }
            setImage(mArrayUri);
            UploadProduct.setVisibility(View.VISIBLE);


        }
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);


            }
        });
        UploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadProduct.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                UploadProduct();
            }
        });
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
                UploadProduct.setVisibility(View.VISIBLE);
            }
        });
        switchImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    float currentPosition = switchImage.getScrollX();
                    float pagesCount = switchImage.getChildCount();
                    float pageLengthInPx = switchImage.getMeasuredWidth()/pagesCount;

                    int page = (int) (Math.floor((currentPosition - pageLengthInPx / 2) / pageLengthInPx) + 1);
                    switchImage.scrollTo((int) (page * pageLengthInPx), 0);
                }
                return false;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            imageGallery.removeAllViews();
            mArrayUri.clear();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            imagesEncodedList = new ArrayList<String>();
            if (data.getData() != null) {

                ImageUri = data.getData();
                mArrayUri.add(ImageUri);
                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(ImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageEncoded = cursor.getString(columnIndex);
                cursor.close();

            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        // Get the cursor
                        Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        imagesEncodedList.add(imageEncoded);
                        cursor.close();

                    }

                }
            }
            UploadImage.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "selected image :" + mArrayUri.size(), Toast.LENGTH_LONG).show();
            setImage(mArrayUri);
        } else {
            Toast.makeText(getActivity(), "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }
    }
    private View getImageView(Uri image) {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 10, 0);
        imageView.setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
        imageView.setMinimumHeight(300);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(lp);
        //imageView.setImageResource(image);
        Picasso.get().load(image).into(imageView);
        return imageView;
    }
    public void setImage(ArrayList<Uri> image){

        for (int i=0;i<image.size();i++){

            imageGallery.addView(getImageView(image.get(i)));

            //  Picasso.get().load(images[i]).into(image);

        }


    }
    public void UploadImage(){
        for( uploadCont = 0;uploadCont<mArrayUri.size();++uploadCont){
            Uri inImage = mArrayUri.get(uploadCont);

            final StorageReference childRef = mStorageRef.child(uniqueID+uploadCont);
            childRef.putFile(inImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //    Toast.makeText(UploadProduct.this,"Done Upload Image",Toast.LENGTH_LONG).show();
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url= String.valueOf(uri);
                            listImage.add(url);

                        }
                    });
                }
            });
        }
        Toast.makeText(getContext(),"Done Upload Image",Toast.LENGTH_LONG).show();
    }
    public void UploadProduct(){
        int P =0;
        pd.show();
        if(price.getText()!=null) {
             P = (int) (Double.parseDouble(price.getText().toString()) * 100);
        }
   //     Toast.makeText(getActivity(),""+P,Toast.LENGTH_SHORT).show();
        product = new ProductModel(uniqueID,name.getText().toString(),P,desc.getText().toString(),Integer.parseInt(itemNumber.getText().toString()),listImage,category_id);
        db.collection("Products").document(uniqueID)
                .set(product)
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
    }



