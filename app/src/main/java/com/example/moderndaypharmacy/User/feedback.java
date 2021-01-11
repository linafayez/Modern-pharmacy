package com.example.moderndaypharmacy.User;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.FeedbackModel;
import com.example.moderndaypharmacy.Models.OrderModel;
import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.example.moderndaypharmacy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class feedback extends Fragment {
    static String UID ;
    RecyclerView products;
    FeedBackAdapter adapter;
    static ArrayList<ProductModel> data;
    Button Submit;
    static OrderModel orderModel;
    static FeedbackModel feedbackModel;
    static ArrayList<ProductModel> models , pro;
    FloatingActionButton done;
    static UserInfoModel user;
    ProgressBar progressBar;
    static ArrayList<FeedbackModel.feed> feeds;
    SharedPreference sharedPreference;
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
        models = new ArrayList<>();
        feeds = new ArrayList<>();
        sharedPreference= new SharedPreference(getContext());
        pro = new ArrayList<>();
      //  user = Change.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user = sharedPreference.getUser();
        orderModel = feedbackArgs.fromBundle(getArguments()).getOrder();
        products = view.findViewById(R.id.products);
        data = feedbackArgs.fromBundle(getArguments()).getOrder().getProductList();
        feedbackModel = new FeedbackModel();
        // UID = FirebaseFirestore.getInstance().collection("Feedback").document().getId();

        if(orderModel.getFeedbackModel() != null) {
            if( orderModel.getFeedbackModel().getId() != null ) {
                UID = orderModel.getFeedbackModel().getId();
                feedbackModel = Change.getFeedback(UID);
            }
        }else {
            UID = FirebaseFirestore.getInstance().collection("Feedback").document().getId();

        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        adapter = new FeedBackAdapter(getContext(),orderModel  , feedbackModel );
        products.setLayoutManager(manager);
        products.setHasFixedSize(false);
        products.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);
        pro= Change.getProducts(data);
        done = view.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                done.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                CountDownTimer timer = null;
                if (timer != null) {
                    timer.cancel();
                }

                timer = new CountDownTimer(1500, 10000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {

                        //do what you wish
                        Change.uploadFeedBack(feedbackModel,getView());

                    }

                }.start();


            }
        });
    }

    public static class Change{
        public static void uploadFeedBack(final FeedbackModel feedbackModel, final View view){
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date);
            feedbackModel.setOrderId(orderModel.getId());
            feedbackModel.setId(UID);
            feedbackModel.setTimestamp(timestamp);
            feedbackModel.setUser(user);

           feedbackModel.setProductsModels(models);
           //  feedbackModel.setProductsModels(pro);

            orderModel.setFeedbackModel(feedbackModel);
            // feedbackModel.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            //feedbackModel.
            // orderModel.set
            for (int i=0;i<models.size();i++) {
                final int finalI = i;
                FirebaseFirestore.getInstance().collection("Products").document(models.get(i).getID()).set(models.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
            FirebaseFirestore.getInstance().collection("Orders").document(orderModel.getId()).set(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Toast.makeText(view.getContext(), "thank you",Toast.LENGTH_LONG).show();
                    //  Navigation.findNavController(view).navigateUp();
                }
            });
            FirebaseFirestore.getInstance().collection("Feedback").document(UID).set(feedbackModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(view.getContext(), "thank you",Toast.LENGTH_LONG).show();
                    Navigation.findNavController(view).navigateUp();
                }
            });
        }

        public static void updateProducts(ProductModel product, Float rating, String note, int position) {
            int a=0;

            FeedbackModel.feed feed = new FeedbackModel.feed(product, note,rating+"");
            feeds.add(feed);
            System.out.println(""+feeds.size());
            feedbackModel.setModels(feeds);

            System.out.println(""+feeds.get(0).getNote());
            if(models.size()>0){
                // product.setRating((product.getRating()+rating)/2);
               // product.setRating(product.getRating());
                for (int i = 0; i < models.size(); i++) {
                    if (product.getID().equals(models.get(i).getID())){
                        models.set(i,product);
                        a=1;
                        break;
                    }
                }


            }

            if(a==0){
                models.add(product);
                System.out.println("done1");

            }




            // feedbackModel.setModels();

        }
        public static ArrayList<ProductModel> getProducts(final ArrayList<ProductModel> products) {
            final ArrayList<ProductModel> models= new ArrayList<>();
            for(int i= 0 ;i<products.size();i++){
                final int finalI = i;
                FirebaseFirestore.getInstance().collection("Products").document(products.get(i).getID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        models.add(documentSnapshot.toObject(ProductModel.class));


                    }
                });
            }

            return models;
        }
        private static FeedbackModel getFeedback(String uid) {

            FirebaseFirestore.getInstance().collection("Feedback").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot!= null){
                        feedbackModel = documentSnapshot.toObject(FeedbackModel.class);
                    }
                }
            });
            return feedbackModel;
        }


        public static UserInfoModel getUser(String uid) {
            FirebaseFirestore.getInstance().collection("User").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot!= null){
                        user = documentSnapshot.toObject(UserInfoModel.class);
                    }
                }
            });
            return user;
        }
    }

}