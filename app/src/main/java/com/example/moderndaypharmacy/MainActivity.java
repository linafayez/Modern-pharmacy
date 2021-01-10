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
    public void update(@Nullable FirebaseUser user) {
        if(user != null){
            final SharedPreference sharedPreference = new SharedPreference(MainActivity.this);
           UserInfoModel userInfo = sharedPreference.getUser();
                    if(userInfo != null){
                      //  sharedPreference.addUser(userInfo);
                        Intent done ;
                        if(userInfo.getType()!= null && userInfo.getType().equals("User")){
                            done = new Intent(MainActivity.this, MainPage.class);
                        }else {
                            done = new Intent(MainActivity.this, AdminPanel.class);
                        }
                        startActivity(done);
                    }else{
                        UserInfo userInfo1 = new UserInfo();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", null );
                        userInfo1.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .add(android.R.id.content, userInfo1).commit();}

        }
                }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: " + user.toString());
               updateUI(user);

                // ...
            } else {
                Log.d(TAG, "onActivityResult: " + "error");
                auth();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    private void updateUI(@Nullable FirebaseUser user) {
        final SharedPreference sharedPreference = new SharedPreference(getApplicationContext());
        if(user != null){

            Intent done;
           UserInfoModel userInfoModel= sharedPreference.getUser();
           if(userInfoModel==null)
            Toast.makeText(getApplicationContext(),"userInfoModel.getName()",Toast.LENGTH_LONG).show();
           if(userInfoModel != null && userInfoModel.getType().equals("User")){
              done = new Intent(getApplicationContext(), MainPage.class);

           }else {
                done = new Intent(getApplicationContext(), AdminPanel.class);

           }
            startActivity(done);
//        Intent done = new Intent(getApplicationContext(), MainPage.class);
//
//            startActivity(done);
        }else{
           auth();
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        update(currentUser);
    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        update(currentUser);
    }
}