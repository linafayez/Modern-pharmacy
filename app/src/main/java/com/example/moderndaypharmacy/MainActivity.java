package com.example.moderndaypharmacy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderndaypharmacy.Admin.AdminHomePage;
import com.example.moderndaypharmacy.Admin.AdminPanel;
import com.example.moderndaypharmacy.Models.ProductModel;
import com.example.moderndaypharmacy.Models.ScanModel;
import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.example.moderndaypharmacy.User.MainPage;
import com.example.moderndaypharmacy.User.SharedPreference;
import com.example.moderndaypharmacy.User.UserInfo;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}