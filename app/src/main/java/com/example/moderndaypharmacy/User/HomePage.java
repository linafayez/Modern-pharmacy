package com.example.moderndaypharmacy.User;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Admin.Ads;
import com.example.moderndaypharmacy.Models.AdsModel;
import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.Models.ScanModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.littlemango.stacklayoutmanager.StackLayoutManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomePage extends Fragment {
    LinearLayout product,query, scan;
    CardView search;
    RecyclerView ads;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<AdsModel> response;
    private static final int CAMERA_REQUEST = 1888;
    ImageView image ;
    String id;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
SharedPreference sharedPreference;
    private Uri ImageUri;


    public HomePage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        query=view.findViewById(R.id.query);
        sharedPreference = new SharedPreference(getContext());
        scan = view.findViewById(R.id.scan);
        image = view.findViewById(R.id.i);
        final Bundle bundle = new Bundle();
        bundle.putString("newQ","new");
        View.OnClickListener q= Navigation.createNavigateOnClickListener(R.id.action_homePage_to_query,bundle);
        query.setOnClickListener(q);
        ads = view.findViewById(R.id.Ads);
        Query query = db.collection("Ads");
        response = new FirestoreRecyclerOptions.Builder<AdsModel>()
                .setQuery(query, AdsModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<AdsModel, AdsH>(response) {
            @Override
            protected void onBindViewHolder(@NonNull AdsH holder, int position, @NonNull AdsModel model) {
                Picasso.get().load(Uri.parse(model.getAdsImage())).into(holder.image);
            }

            @NonNull
            @Override
            public AdsH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads, parent, false);
                return new AdsH(view);
            }


        };
        StackLayoutManager manager = new StackLayoutManager();
        ads.setLayoutManager(manager);
        ads.setHasFixedSize(false);
        ads.setAdapter(adapter);
      product = view.findViewById(R.id.products);
      product.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Navigation.findNavController(getView()).navigate(R.id.action_homePage_to_products);
          }
      });
        search = view.findViewById(R.id.s);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_homePage_to_search2);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraHardware(getContext());
            }
        });


    }



    /** Check if this device has a camera */
    private void checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
            else
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        } else {
            // no camera on this device

        }
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    /** A safe way to get an instance of the Camera object. */
//    public static Camera getCameraInstance(){
//        Camera c = null;
//        try {
//            c = Camera.open(); // attempt to get a Camera instance
//        }
//        catch (Exception e){
//            // Camera is not available (in use or does not exist)
//        }
//        return c; // returns null if camera is unavailable
//    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class AdsH extends RecyclerView.ViewHolder {
        public ImageView image;

        public AdsH(@NonNull final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore.getInstance().collection("Products").document(response.getSnapshots().get(getAdapterPosition()).getProId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.toObject(ProductModel.class) != null)
                                Navigation.findNavController(getView()).navigate(HomePageDirections.actionHomePageToProductView(documentSnapshot.toObject(ProductModel.class)));
                        }
                    });

                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
                startActivityForResult(intent, CAMERA_REQUEST);



            }
            else
            {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {

           Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            id= db.collection("Scan").document().getId();
            Uri u= bitmapToUriConverter(bitmap);
            Toast.makeText(getContext(),u.toString(),Toast.LENGTH_LONG).show();
            ScanModel scanModel = new ScanModel();
            scanModel.setId(id);
            scanModel.setImage(u.toString());
            sharedPreference.addToScanCart(scanModel);


        }
        }
    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            //options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);
            File file = new File(getActivity().getFilesDir(), "Image"
                    + id+ ".jpeg");
            FileOutputStream out = getActivity().openFileOutput(file.getName(),
                    Context.MODE_PRIVATE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
       // Toast.makeText(getContext(),(uri ==null)+"",Toast.LENGTH_LONG).show();

        return uri;
    }

}
