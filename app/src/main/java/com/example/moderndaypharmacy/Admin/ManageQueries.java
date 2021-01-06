package com.example.moderndaypharmacy.Admin;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moderndaypharmacy.Models.QueryModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ManageQueries extends Fragment {

    RecyclerView queries;
    FirebaseFirestore db;
    String id;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions<QueryModel> response;

    public ManageQueries() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_queries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queries = view.findViewById(R.id.queries);
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("Queries");
        response = new FirestoreRecyclerOptions.Builder<QueryModel>()
                .setQuery(query, QueryModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<QueryModel, QueriesHolder>(response) {
            @NonNull
            @Override
            public QueriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_query, parent, false);
                return new QueriesHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull QueriesHolder holder, int position, @NonNull QueryModel model) {
                holder.query.setText(model.getQueryText());

            }
        };
        queries.setLayoutManager(new LinearLayoutManager(getContext()));
        queries.setHasFixedSize(false);
        queries.setAdapter(adapter);

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

    class QueriesHolder extends RecyclerView.ViewHolder {
        EditText newQuery;
        TextView query;
        Button replies, send;



        public QueriesHolder(@NonNull final View itemView) {
            super(itemView);
            query = itemView.findViewById(R.id.query);
            replies = itemView.findViewById(R.id.replies);
            replies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QueryModel q=response.getSnapshots().get(getAdapterPosition());
                    showPopupWindow(v,q);

                }
            });

        }
    }

    public void showPopupWindow(final View view, final QueryModel q) {

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.queries, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        final TextView text = popupView.findViewById(R.id.text);
        final ImageView post = popupView.findViewById(R.id.post);
        final EditText reply = popupView.findViewById(R.id.editText);

        String data = "";

        if(q!=null) {
            if (q.getReply() != null) {
                String documentReply=q.getReply();
                data += documentReply;
                text.setText(data);

            } else {
                text.setText("Not Answered");
            }
        }


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "";
                if(q!=null) {
                    id =q.getQueryId();

                        String documentReply=String.valueOf(reply.getEditableText());
                        data += documentReply;
                        text.setText(data);
                    db.collection("Queries").document(id).update("reply",documentReply);
                    Toast.makeText(getContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();


                }
                reply.setText("");
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }



}
