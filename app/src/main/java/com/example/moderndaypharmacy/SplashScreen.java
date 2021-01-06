package com.example.moderndaypharmacy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.moderndaypharmacy.Admin.AdminPanel;
import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.example.moderndaypharmacy.User.MainPage;
import com.example.moderndaypharmacy.User.SharedPreference;
import com.example.moderndaypharmacy.User.UserInfo;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class SplashScreen extends Fragment {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "SignIn";

    public SplashScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            auth();
        }

    }
    public void auth(){
        Thread thread = new Thread() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                try {
                    sleep(3000);
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.PhoneBuilder().build()
                            // new AuthUI.IdpConfig.GoogleBuilder().build(),
                            // new AuthUI.IdpConfig.FacebookBuilder().build(),
                            // new AuthUI.IdpConfig.TwitterBuilder().build()
                    );
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setTheme(R.style.AppTheme)// Set theme
                                    .setLogo(R.drawable.in)
                                    .build(),
                            RC_SIGN_IN);
                    //  startActivity(new Intent(getApplicationContext(), LogIn.class));
                    //      Toast.makeText(getApplicationContext(), "kkjh", Toast.LENGTH_LONG).show();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        thread.start();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: "+data.getDataString());
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: " + user.toString());
                updateUI(user);

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    public void updateUI(@Nullable FirebaseUser user) {
        if(user != null){
            final SharedPreference sharedPreference = new SharedPreference(getContext());
        FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfoModel userInfo = documentSnapshot.toObject(UserInfoModel.class);
                if(userInfo == null){
                   Navigation.findNavController(getView()).navigate(SplashScreenDirections.actionSplashScreenToUserInfo3(null));
                }else{
                    sharedPreference.addUser(userInfo);
                    Intent done ;
                    if(userInfo.getType().equals("User")){
                         done = new Intent(getContext(), MainPage.class);
                    }else {

                         done = new Intent(getContext(), AdminPanel.class);
                    }
                    startActivity(done);
                }
            }
        });


        }else{
            //  auth();
        }
    }
}