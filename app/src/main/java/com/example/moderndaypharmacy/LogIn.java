package com.example.moderndaypharmacy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LogIn extends AppCompatActivity {
Button in ;
EditText phone;
 private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ProgressBar progressBar;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        phone = findViewById(R.id.editTextPhone);
        in = findViewById(R.id.OTP);
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = phone.getText().toString();
                if(number.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter the number",Toast.LENGTH_LONG).show();
                }else if(number.length() != 13){
                    Toast.makeText(getApplicationContext(), "Please enter the number again",Toast.LENGTH_LONG).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    in.setVisibility(View.INVISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            number,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            LogIn.this,
                            mCallbacks
                    );


                }

            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Intent opt = new Intent(getApplicationContext(), VerifyPage.class);
                opt.putExtra("Auth", s);
                startActivity(opt);
                finish();
            }
        };


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