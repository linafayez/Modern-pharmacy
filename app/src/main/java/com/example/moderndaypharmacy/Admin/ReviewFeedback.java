package com.example.moderndaypharmacy.Admin;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moderndaypharmacy.Models.AdsModel;
import com.example.moderndaypharmacy.Models.FeedbackModel;
import com.example.moderndaypharmacy.R;
import com.example.moderndaypharmacy.User.HomePage;
import com.example.moderndaypharmacy.Util.TextViewUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class ReviewFeedback extends Fragment {
RecyclerView feedback;
    FirebaseFirestore db;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<FeedbackModel> response;
    public ReviewFeedback() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        feedback = view.findViewById(R.id.feedback);
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("Feedback");
        response = new FirestoreRecyclerOptions.Builder<FeedbackModel>()
                .setQuery(query, FeedbackModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<FeedbackModel, FeedbackViewHolder>(response) {
            @Override
            protected void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position, @NonNull FeedbackModel model) {
                if(model.getUser()!= null){
                    if(model.getUser().getImage()!= null)
                        Picasso.get().load(Uri.parse(model.getUser().getImage())).into(holder.imageView);
                    holder.name.setText(model.getUser().getName());

                }
                holder.time.setText(model.getTimestamp().toDate().toString());
                holder.feedback.setText(TextViewUtil.feedbackDara(model));
                holder.orderId.setText("Order Id: "+model.getOrderId());

            }

            @NonNull
            @Override
            public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_admin, parent, false);
                return new FeedbackViewHolder(view);
            }


        };
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        feedback.setLayoutManager(manager);
        feedback.setHasFixedSize(false);
        feedback.setAdapter(adapter);

    }
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

    public class FeedbackViewHolder extends RecyclerView.ViewHolder{
     ImageView imageView;
     TextView name, time, feedback,orderId;
        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
             name = itemView.findViewById(R.id.name);
             time = itemView.findViewById(R.id.time);
             feedback = itemView.findViewById(R.id.textView10);
             orderId = itemView.findViewById(R.id.ll);
        }
    }
}