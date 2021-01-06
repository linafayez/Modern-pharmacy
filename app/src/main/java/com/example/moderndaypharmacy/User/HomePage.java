package com.example.moderndaypharmacy.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.moderndaypharmacy.R;

public class HomePage extends Fragment {
    LinearLayout product,query;
    CardView search;
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
        query=view.findViewById(R.id.query);

        final Bundle bundle = new Bundle();
        bundle.putString("newQ","new");
        View.OnClickListener q= Navigation.createNavigateOnClickListener(R.id.action_homePage_to_query,bundle);
        query.setOnClickListener(q);
     /*
        query=view.findViewById(R.id.query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_homePage_to_query);
            }
        });
*/


    }
}