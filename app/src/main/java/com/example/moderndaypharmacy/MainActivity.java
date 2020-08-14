package com.example.moderndaypharmacy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
         Thread thread = new Thread(){
             @Override
             public void run(){
                 try {
                     sleep(3000);
                   startActivity(new Intent(getApplicationContext(), LogIn.class));
                   finish();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }

         };
         thread.start();

    }
    private void updateUI(@Nullable FirebaseUser user) {
        Intent done = new Intent(getApplicationContext(),HomePage.class);
        if(user != null){
            startActivity(done);
            finish();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}