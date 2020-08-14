package com.example.moderndaypharmacy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String verificationId;
    EditText opt ;
    Button verification;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_page);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        verificationId = getIntent().getStringExtra("Auth");
        opt= findViewById(R.id.editTextNumber);
        verification= findViewById(R.id.OTP);
        verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String op = opt.getText().toString();
                if(op.isEmpty() || op.length() != 6){
                    Toast.makeText(getApplicationContext(), "Please enter the number of verification",Toast.LENGTH_LONG).show();
                }else{
                    verification.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, op);
                    signInWithPhoneAuthCredential(credential);
                }
            }
            });


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                           updateUI(user);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(), "3Please enter the number",Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(), "Please enter the number",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
    private void updateUI(@Nullable FirebaseUser user) {
        Intent done = new Intent(getApplicationContext(),HomePage.class);
if(user!=null) {
    done.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    done.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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