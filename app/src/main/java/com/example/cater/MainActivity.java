package com.example.cater;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.cater.databinding.ActivityMainBinding;
import com.example.cater.profile.Profile;
import com.example.cater.profile.ProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ProfileViewModel mProfileViewModel;

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

        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        mProfileViewModel.getProfile().observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                if (profile != null) {
                    NavigationView navigationView = binding.navView;
                    View header_layout = navigationView.getHeaderView(0);
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