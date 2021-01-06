package com.example.moderndaypharmacy.User;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Queries extends Fragment {
    EditText newQuery;
    TextView text;
    Button send;
    RecyclerView queries;
    QueryModel queryModel;
    FirestoreRecyclerAdapter adapter;
    FirestoreRecyclerOptions <QueryModel> response;
     String queryId,queryText,reply,ID;
    FirebaseFirestore db;


    public Queries() {
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
        return inflater.inflate(R.layout.fragment_query, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newQuery=view.findViewById(R.id.newQuery);
        send=view.findViewById(R.id.send);
        text=view.findViewById(R.id.text);
        queries=view.findViewById(R.id.queries);
        db= FirebaseFirestore.getInstance();
        Query query = db.collection("Queries");
        response = new FirestoreRecyclerOptions.Builder<QueryModel>()
                .setQuery(query, QueryModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<QueryModel, QueriesHolder>(response) {
            @NonNull
            @Override
            public QueriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queries_layout,parent,false);
                return new QueriesHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull QueriesHolder holder, int position, @NonNull QueryModel model) {
                holder.query.setText(model.getQueryText());

            }
        };

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newQ = getArguments().getString("newQ");
                if(newQ.equals("new")){
                    ID= db.collection("Queries").document().getId();

                }
                queryModel = new QueryModel(""+ID,queryText,reply);
                String result = newQuery.getEditableText().toString();

                queryModel.setQueryId(ID);
                queryModel.setQueryText(result);


                db.collection("Queries").document(ID)
                        .set(queryModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Send Successfully , Wait for Reply", Toast.LENGTH_LONG).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getActivity(),""+ e, Toast.LENGTH_LONG).show();
                            }

                        });

                       newQuery.setText("");


            }


        });
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
    class QueriesHolder extends RecyclerView.ViewHolder{
        TextView query;
        Button replies;


        public QueriesHolder(@NonNull final View itemView) {
            super(itemView);
            query = itemView.findViewById(R.id.query);
            replies = itemView.findViewById(R.id.replies);

            replies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    QueryModel q=response.getSnapshots().get(getAdapterPosition());

                    showPopupWindow(view,q);


        }
        public void showPopupWindow(final View view, final QueryModel q){

            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.replies, null);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;

            boolean focusable = true;

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            final TextView text=popupView.findViewById(R.id.text);

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

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    //Close the window when clicked
                    popupWindow.dismiss();
                    return true;
                }
            });

        }

            });

        }
    }



}
