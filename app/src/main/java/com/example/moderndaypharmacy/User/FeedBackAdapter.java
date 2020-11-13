package com.example.moderndaypharmacy.User;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.feedBackViewHolder>  {

    @NonNull
    @Override
    public feedBackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull feedBackViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class feedBackViewHolder extends RecyclerView.ViewHolder {

        public feedBackViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
