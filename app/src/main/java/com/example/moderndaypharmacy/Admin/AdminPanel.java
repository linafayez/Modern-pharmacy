package com.example.moderndaypharmacy.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.moderndaypharmacy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
   BottomNavigationView bottomNav = findViewById(R.id.bottomBar);
        NavController nav = Navigation.findNavController(this,R.id.nav_host);
        NavigationUI.setupWithNavController(bottomNav , nav);
    }
}