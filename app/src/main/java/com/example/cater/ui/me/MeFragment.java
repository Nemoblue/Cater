package com.example.cater.ui.me;


import static java.lang.Integer.parseInt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.cater.R;
import com.example.cater.databinding.FragmentMeBinding;
import com.example.cater.profile.Profile;
import com.example.cater.profile.ProfileViewModel;
import com.example.cater.ui.login.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;
import java.util.Objects;

public class MeFragment extends Fragment {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;

    private ProfileViewModel mProfileViewModel;
    private Profile mProfile;
    private Profile zero_profile;
    private FragmentMeBinding binding;
    private int icon_count = 0;

    private ImageView user_icon;
    private Button login;
    private Button set_button;
    private TextView user_name;
    private TextView user_id;
    private TextView user_age;
    private TextView user_description;

    public static final int PICK_IMAGE = 1;
    public static final int LOGIN_REQUEST = 2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user_id = root.findViewById(R.id.user_id);
        user_icon = root.findViewById(R.id.user_icon);
        user_icon.setEnabled(false);
        user_name = root.findViewById(R.id.user_name);
        user_description = root.findViewById(R.id.user_description);
        user_age = root.findViewById(R.id.user_age);
        login = root.findViewById(R.id.login_button);
        set_button = root.findViewById(R.id.setting_button);

        zero_profile = new Profile.Builder(-1, "00000000000")
                .name("Not Login")
                .description("Please login in or register to continue.")
                .age(18)
                .builder();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mProfileViewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        mProfileViewModel.getProfile().observe(requireActivity(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                mProfile = profile;
                setUI(mProfile);
            }
        });

        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icon_count == 7)
                    icon_count = 0;
                else
                    icon_count++;
                @SuppressLint("Recycle") TypedArray profilePhotoResources =
                        getResources().obtainTypedArray(R.array.profile_photos);
                Glide.with(requireContext()).load(profilePhotoResources.getResourceId(icon_count, 0)).into(user_icon);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login.getText().toString().equals(getString(R.string.button_login_register))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REQUEST);
                } else {
                    mProfile = zero_profile;
                    mProfileViewModel.logout();
                    setUI(mProfile);
                }
            }
        });

        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (set_button.getText().toString().equals(getString(R.string.setting))) {
                    set_button.setText(R.string.save);
                    user_icon.setEnabled(true);
                    user_name.setInputType(InputType.TYPE_CLASS_TEXT);
                    user_age.setInputType(InputType.TYPE_CLASS_NUMBER);
                    user_description.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    enableLocation();
                    set_button.setText(R.string.setting);
                    user_icon.setEnabled(false);
                    user_name.setInputType(InputType.TYPE_NULL);
                    user_age.setInputType(InputType.TYPE_NULL);
                    user_description.setInputType(InputType.TYPE_NULL);
                    Profile profile = new Profile.Builder(mProfile.getUid(), mProfile.getuPhone())
                            .age(parseInt(user_age.getText().toString()))
                            .name(user_name.getText().toString())
                            .description(user_description.getText().toString())
                            .photo(String.format(Locale.getDefault(),
                                    "default_%d", icon_count))
                            .builder();
                    if (mLastLocation != null) {
                        Log.d("map", "check point");
                        profile.setLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        profile.setActive(true);
                    }
                    mProfile = profile;
                    setUI(profile);
                    mProfileViewModel.insert(profile);
                }
            }
        });
        return root;
    }

    public void setUI(Profile profile) {
        user_name.setText(profile.getuName());
        user_description.setText(profile.getDescription());
        if (profile.getUid() != -1) {
            user_age.setText(String.valueOf(profile.getAge()));
            user_id.setText(String.valueOf(profile.getUid()));
            set_button.setVisibility(View.VISIBLE);
            login.setText(R.string.logout);
        } else {
            user_id.setText("");
            user_age.setText("");
            set_button.setVisibility(View.INVISIBLE);
            login.setText(R.string.button_login_register);
        }

        try {
            NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
            View header_layout = navigationView.getHeaderView(0);
            TextView header_name = header_layout.findViewById(R.id.nav_name);
            TextView header_description = header_layout.findViewById(R.id.nav_description);
            ImageView header_icon = header_layout.findViewById(R.id.nav_image_view);

            header_name.setText(profile.getuName());
            header_description.setText(profile.getDescription());

            if (profile.getPhoto() != null) {
                String photoPath = profile.getPhoto();
                if (photoPath.startsWith("default")) {
                    int index = parseInt(photoPath.substring(photoPath.length() - 1));
                    TypedArray profilePhotoResources =
                            getResources().obtainTypedArray(R.array.profile_photos);
                    Glide.with(requireContext()).load(profilePhotoResources.getResourceId(index, 0)).into(user_icon);
                    Glide.with(requireContext()).load(profilePhotoResources.getResourceId(index, 0)).into(header_icon);
                    icon_count = index;
                    profilePhotoResources.recycle();
                }
            } else {
                Glide.with(requireContext()).load(R.drawable.ic_menu_home).into(user_icon);
                Glide.with(requireContext()).load(R.mipmap.ic_launcher_round).into(header_icon);
            }
        } catch (Exception ignore) {}
    }

    private void enableLocation() {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                mLastLocation = location;
                            }
                        }
                );
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            } else {
                Toast.makeText(requireContext(), "Location Permission Denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            android.net.Uri uri = data.getData();
            user_icon.setImageURI(uri);
        }

        if (requestCode == LOGIN_REQUEST && resultCode == Activity.RESULT_OK) {
            int result = data.getIntExtra(LoginActivity.EXTRA_REPLY, 0);

            mProfileViewModel.getProfileByID(result).observe(requireActivity(), new Observer<Profile>() {
                @Override
                public void onChanged(Profile profile) {
                    if (profile != null) {
                        mProfile = profile;
                        setUI(mProfile);
                    }
                }
            });
            login.setText(R.string.logout);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}