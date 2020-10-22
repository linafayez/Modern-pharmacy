package com.example.moderndaypharmacy.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.moderndaypharmacy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //fragment2
        BottomNavigationView bottomNav = findViewById(R.id.bottomBar);
        NavController nav = Navigation.findNavController(this,R.id.fragment2);
        NavigationUI.setupWithNavController(bottomNav , nav);
    }
}