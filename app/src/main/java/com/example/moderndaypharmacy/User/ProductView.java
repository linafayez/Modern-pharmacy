package com.example.moderndaypharmacy.User;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
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
            addReadMore(productModel.getDesc(),desc);
            if (productModel!=null && productModel.getPic() != null){
                Picasso.get().load(Uri.parse(productModel.getPic().get(0))).into(image);
            }

               addToCart.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(productModel.getItemNumberInCart() == 0){
                           productModel.setItemNumberInCart(1);
                       }
                       sharedPreference.addToCart(productModel);
                       Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_SHORT).show();
                   }

               });

        }

    }
    private void addReadMore(final String text, final TextView textView) {
        SpannableString ss = new SpannableString(text.substring(0, 100) + "... read more");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadLess(text, textView);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 10, ss.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void addReadLess(final String text, final TextView textView) {
        SpannableString ss = new SpannableString(text + " read less");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadMore(text, textView);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    ds.setColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 10, ss.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}

