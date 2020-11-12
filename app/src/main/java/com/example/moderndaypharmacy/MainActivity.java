package com.example.moderndaypharmacy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moderndaypharmacy.Admin.AdminHomePage;
import com.example.moderndaypharmacy.Admin.AdminPanel;
import com.example.moderndaypharmacy.Models.UserInfoModel;
import com.example.moderndaypharmacy.User.MainPage;
import com.example.moderndaypharmacy.User.SharedPreference;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
      //      auth();
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
    public void updateUI(@Nullable FirebaseUser user) {
        if(user != null){
            final SharedPreference sharedPreference = new SharedPreference(MainActivity.this);
            FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserInfoModel userInfo = documentSnapshot.toObject(UserInfoModel.class);
                    if(userInfo == null){
                        //   Navigation.findNavController(getView()).navigate(SplashScreenDirections.actionSplashScreenToUserInfo3(null));
                    }else{
                        sharedPreference.addUser(userInfo);
                        Toast.makeText(MainActivity.this,userInfo.getType(),Toast.LENGTH_LONG).show();
                        Intent done ;
                        if(userInfo.getType().equals("User")){
                            done = new Intent(MainActivity.this, MainPage.class);
                        }else {
                            Toast.makeText(MainActivity.this,userInfo.getType(),Toast.LENGTH_LONG).show();
                            done = new Intent(MainActivity.this, AdminPanel.class);
                        }
                        startActivity(done);
                    }
                }
            });


        }else{
            //  auth();
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: ");
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            if (resultCode == RESULT_OK) {
//                // Successfully signed in
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                Log.d(TAG, "onActivityResult: " + user.toString());
//               updateUI(user);
//
//                // ...
//            } else {
//                // Sign in failed. If response is null the user canceled the
//                // sign-in flow using the back button. Otherwise check
//                // response.getError().getErrorCode() and handle the error.
//                // ...
//            }
//        }
//    }
//    private void updateUI(@Nullable FirebaseUser user) {
//        final SharedPreference sharedPreference = new SharedPreference(getApplicationContext());
//        if(user != null){
//
//            Intent done;
//           UserInfoModel userInfoModel= sharedPreference.getUser();
//           if(userInfoModel==null)
//            Toast.makeText(getApplicationContext(),"userInfoModel.getName()",Toast.LENGTH_LONG).show();
//           if(userInfoModel != null && userInfoModel.getType().equals("User")){
//              done = new Intent(getApplicationContext(), MainPage.class);
//
//           }else {
//                done = new Intent(getApplicationContext(), AdminPanel.class);
//
//           }
//            startActivity(done);
////        Intent done = new Intent(getApplicationContext(), MainPage.class);
////
////            startActivity(done);
//        }else{
//          //  auth();
//        }
 //   }
    @Override
    protected void onRestart() {
        super.onRestart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(currentUser);
    }
}