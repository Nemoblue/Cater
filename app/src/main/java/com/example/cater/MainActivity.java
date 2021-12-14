/*
 * Copyright (c) 2021.   # COMP 4521 #
 * # SHEN, Ye #	 20583137	yshenat@connect.ust.hk
 * # ZHOU, Ji #	 20583761	jzhoubl@connect.ust.hk
 * # WU, Sik Chit #	 20564571	scwuaa@connect.ust.hk
 */

package com.example.cater;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.cater.databinding.ActivityMainBinding;
import com.example.cater.profile.ProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_map, R.id.nav_me)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        ProfileViewModel mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        mProfileViewModel.getProfile().observe(this, profile -> {
            if (profile != null) {
                NavigationView navigationView1 = binding.navView;
                View header_layout = navigationView1.getHeaderView(0);
                TextView header_name = header_layout.findViewById(R.id.nav_name);
                TextView header_description = header_layout.findViewById(R.id.nav_description);
                ImageView header_icon = header_layout.findViewById(R.id.nav_image_view);

                header_name.setText(profile.getuName());
                header_description.setText(profile.getDescription());
                if (profile.getPhoto() != null) {
                    String photoPath = profile.getPhoto();
                    if (photoPath.startsWith("default")) {
                        int index = Integer.parseInt(photoPath.substring(photoPath.length() - 1));
                        TypedArray profilePhotoResources =
                                getResources().obtainTypedArray(R.array.profile_photos);
                        Glide.with(getBaseContext()).load(profilePhotoResources.getResourceId(index, 0)).into(header_icon);

                        profilePhotoResources.recycle();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}