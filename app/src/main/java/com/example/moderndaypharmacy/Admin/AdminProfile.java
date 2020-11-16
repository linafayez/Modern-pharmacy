package com.example.moderndaypharmacy.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.moderndaypharmacy.BuildConfig;
import com.example.moderndaypharmacy.MainActivity;
import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.example.moderndaypharmacy.R;
import com.example.moderndaypharmacy.User.SharedPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class AdminProfile extends Fragment {
    LinearLayout adminInfo , LogOut , sendNotification , changePassword,allUser , shareApp;
    UserInfoModel user;
    ImageView image;
    public AdminProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SharedPreference sharedPreference = new SharedPreference(getContext());
        adminInfo = view.findViewById(R.id.adminInfo);
        LogOut = view.findViewById(R.id.LogOut);
        allUser = view.findViewById(R.id.allUser);
        sendNotification = view.findViewById(R.id.sendNotification);
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_adminProfile_to_sendNotification2);
            }
        });
        shareApp = view.findViewById(R.id.Invite);
        allUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_adminProfile_to_allUsers);
            }
        });
        user = sharedPreference.getUser();
        adminInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(AdminProfileDirections.actionAdminProfileToUserInfo5(user));
            }
        });
        image = view.findViewById(R.id.imageView6);
        if(user.getImage() != null ){
            Picasso.get().load(Uri.parse(user.getImage())).into(image);

        }
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
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Log out")
                        .setMessage("Are you sure to sign out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
        });

    }
}