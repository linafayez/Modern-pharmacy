package com.example.moderndaypharmacy.User;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderndaypharmacy.BuildConfig;
import com.example.moderndaypharmacy.MainActivity;
import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.example.moderndaypharmacy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;

public class Profile extends Fragment {
    LinearLayout userInfo , LogOut , shareApp ;
    UserInfoModel user;
    ImageView image;
    TextView VirtualBalance , point , orders;
    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SharedPreference sharedPreference = new SharedPreference(getContext());
        VirtualBalance = view.findViewById(R.id.VirtualBalance);
        point= view.findViewById(R.id.point);
        orders = view.findViewById(R.id.AllOrder);
        LogOut = view.findViewById(R.id.LogOut);
        shareApp = view.findViewById(R.id.Invite);
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlert();
            }
        });
        userInfo = view.findViewById(R.id.userInfo);
        user = sharedPreference.getUser();
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(ProfileDirections.actionProfile2ToUserInfo2(user));



            }
        });
        FirebaseFirestore.getInstance().collection("Orders").whereEqualTo("userId",user.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
             orders.setText(""+queryDocumentSnapshots.size());
            }
        });
        point.setText(user.getPoints()+"");
        VirtualBalance.setText(""+user.getVirtualBalance());
        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Modern Pharmacy");
                    String shareMessage= "\nInstall this cool application:\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        image = view.findViewById(R.id.imageView6);
        if(user.getImage() != null){
            Picasso.get().load(Uri.parse(user.getImage())).into(image);

        }
    }
    public void displayAlert(){

        new AlertDialog.Builder(getContext())
                .setTitle("Log out")
                .setMessage("Are you sure to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreference sharedPreference = new SharedPreference(getContext());
                        sharedPreference.addUser(null);
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getActivity(),
                                MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        })
                .show();
    }
}