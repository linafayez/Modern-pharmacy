package com.example.moderndaypharmacy.User;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.R;
import com.squareup.picasso.Picasso;



public class ProductView extends Fragment {


    static ProductModel productModel;
    TextView name, price, desc,note;
    ImageView image;
    Button addToCart;
    RatingBar rate;
    SharedPreference sharedPreference;


    public ProductView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productModel = ProductViewArgs.fromBundle(getArguments()).getProduct();
        sharedPreference = new SharedPreference(view.getContext());
        name = view.findViewById(R.id.name);
        price = view.findViewById(R.id.price);
        image = view.findViewById(R.id.image);
        addToCart = view.findViewById(R.id.addToCart);
        desc = view.findViewById(R.id.desc);
        rate=view.findViewById(R.id.ratingBar);
        note=view.findViewById(R.id.note);
        //data = new ArrayList<>();
        desc.setMovementMethod(new ScrollingMovementMethod());

        if( productModel!=null) {

            name.setText(productModel.getName());
            price.setText((productModel.getPrice() / 100 + " " + "JD"));
            desc.setText(productModel.getDesc());
           // rate.setRating(productModel.getRating());
            if (productModel!=null && productModel.getPic() != null){
                Picasso.get().load(Uri.parse(productModel.getPic().get(0))).into(image);
            }

               addToCart.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       sharedPreference.addToCart(productModel);
                       Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_SHORT).show();
                   }

               });

        }


    }
}




