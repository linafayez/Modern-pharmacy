package com.example.moderndaypharmacy.User;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class Search extends Fragment {
    SearchView searchView;
    EditText se;
    RecyclerView result;
    FirestoreRecyclerAdapter firebaseRecyclerAdapter;
    FirestoreRecyclerOptions<ProductModel> options;
    public Search() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.search);
        //  searchView.setFocusable(View.FOCUSABLE);
        se = view.findViewById(R.id.se);

        result = view.findViewById(R.id.searchResult);

        se.isFocusableInTouchMode();
        se.setFocusable(true);
        se.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imgr.showSoftInput(se,InputMethodManager.SHOW_IMPLICIT);
        se.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firebaseUserSearch(s.toString());

                firebaseRecyclerAdapter.startListening();
                if(s.length()<1){
                    firebaseRecyclerAdapter.stopListening();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                firebaseUserSearch(s.toString());
                firebaseRecyclerAdapter.startListening();
                if(s.length()<1){
                    firebaseRecyclerAdapter.stopListening();
                }
            }
        });

    }

    private void firebaseUserSearch(String newText) {

        //  Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = FirebaseFirestore.getInstance().collection("Products").orderBy("name").startAt(newText).endAt(newText + "\uf8ff");
        options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(firebaseSearchQuery, ProductModel.class)
                .build();

        firebaseRecyclerAdapter = new FirestoreRecyclerAdapter<ProductModel, SearchViewHolder>(options) {
            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
                return new SearchViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull ProductModel model) {
                holder.name.setText(model.getName());
                Picasso.get().load(Uri.parse(model.getPic().get(0))).into(holder.image);
            }
        };


        result.setLayoutManager(new LinearLayoutManager(getContext()));
        result.setHasFixedSize(false);
        result.setAdapter(firebaseRecyclerAdapter);


    }

    private class SearchViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView11);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(SearchDirections.actionSearch2ToProductView(options.getSnapshots().get(getAdapterPosition())));
                }
            });
        }
    }
}