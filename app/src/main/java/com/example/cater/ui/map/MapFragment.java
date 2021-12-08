package com.example.cater.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cater.R;
import com.example.cater.databinding.FragmentMapBinding;
import com.example.cater.profile.Profile;
import com.example.cater.profile.ProfileViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    // Variables for the Map fragment
    private GoogleMap mMap;
    private int mType; // 1-4 from normal map to terrain map
    // Set the initial position and zoom level
    float zoom = 18;
    LatLng hkust = new LatLng(22.33653, 114.26363);
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    // Variables for the sample fragment
    private final int mSampleSize = 5;
    private List<Profile> mProfiles;
    private final List<Marker> mSampleMarker = new ArrayList<>();
    private List<Integer> mSampleList = new ArrayList<>();

    // Variables for the guest fragment
    private boolean isFragmentDisplayed = false;
    private FragmentMapBinding binding;
    static final String STATE_FRAGMENT = "state_of_fragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isFragmentDisplayed =
                    savedInstanceState.getBoolean(STATE_FRAGMENT);
        }

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        binding.buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mSampleMarker.size(); i++)
                    mSampleMarker.get(i).remove();
                mSampleMarker.clear();

                setProfileMarker(mProfiles);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hkust, zoom));
                Toast.makeText(requireContext(), R.string.marker_refresh,
                        Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                Toast.makeText(requireContext(), R.string.marker_clear,
                        Toast.LENGTH_SHORT).show();
            }
        });

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment).commit();
        mapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mType = 1;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hkust, zoom));

        assert getParentFragment() != null;
        ProfileViewModel mProfileViewModel = ViewModelProviders.of(getParentFragment()).get(ProfileViewModel.class);
        mProfileViewModel.getActiveProfiles().observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(@Nullable final List<Profile> profiles) {
                for (int i = 0; i < mSampleMarker.size(); i++)
                    mSampleMarker.get(i).remove();
                mSampleMarker.clear();
                setProfileMarker(profiles);
            }
        });

        setMapLongClick(mMap);
        setPoiClick(mMap);
        enableMyLocation();
        setInfoWindowClickToProfile(mMap);
    }

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.dropped_pin))
                        .snippet(snippet));
            }
        });
    }

    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(@NonNull PointOfInterest poi) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions()
                        .position(poi.latLng)
                        .title(poi.name));
                assert poiMarker != null;
                poiMarker.showInfoWindow();
            }
        });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    private void setProfileMarker(List<Profile> profiles) {
        mProfiles = profiles;
        if (mProfiles != null) {
            Random rand = new Random();
            List<Integer> sampleList = new ArrayList<>();
            int profile_size = mProfiles.size();
            int sample_size = Math.min(mSampleSize, profile_size);


            while (sampleList.size() < sample_size) {
                int sample = rand.nextInt(profile_size);
                boolean flag = true;
                for (int i = 0; i < sampleList.size(); i++) {
                    if (sample == sampleList.get(i))
                        flag = false;
                }
                if (flag)
                    sampleList.add(sample);
            }
            mSampleList = sampleList;

            for (int j = 0; j < sample_size; j++) {
                Profile current = mProfiles.get(sampleList.get(j));
                String current_name = String.format(Locale.getDefault(), "%s(%d)",
                        current.getuName(), j);
                LatLng current_latLng = new LatLng(current.getPosition()[0], current.getPosition()[1]);
                String current_snippet = String.format(Locale.getDefault(), "Lat: %1$.5f, Long: %2$.5f",
                        current_latLng.latitude,
                        current_latLng.longitude);
                Marker marker = Objects.requireNonNull(mMap.addMarker(new MarkerOptions()
                        .position(current_latLng)
                        .title(current_name)
                        .snippet(current_snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_CYAN))
                ));
                marker.setTag("Profile");
                marker.showInfoWindow();
                mSampleMarker.add(marker);
            }
        }
    }

    private void setInfoWindowClickToProfile(GoogleMap map) {
        map.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(@NonNull Marker marker) {
                        if (marker.getTag() == "Profile") {
                            displayFragment(marker.getTitle());
                        }
                    }
                }
        );
    }

    public void displayFragment(String title) {
        if (isFragmentDisplayed)
            removeFragment();

        int id = 0;
        Pattern pattern = Pattern.compile(getString(R.string.map_regular_pattern));
        Matcher matcher = pattern.matcher(title);
        while (matcher.find())
            id = Integer.parseInt(matcher.group());
        Profile profile = mProfiles.get(mSampleList.get(id));

        GuestFragment guestFragment = GuestFragment.newInstance(profile);
        // Get the FragmentManager and start a transaction.
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.fragment_guest_container,
                guestFragment).addToBackStack(null).commit();
        // Set boolean flag to indicate fragment is open.
        isFragmentDisplayed = true;
    }

    public void removeFragment() {
        // Get the FragmentManager.
        FragmentManager fragmentManager = getChildFragmentManager();
        // Check to see if the fragment is already showing.
        GuestFragment guestFragment = (GuestFragment) fragmentManager
                .findFragmentById(R.id.fragment_guest_container);
        if (guestFragment != null) {
            // Create and commit the transaction to remove the fragment.
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.remove(guestFragment).commit();
        }
        isFragmentDisplayed = false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        inflater.inflate(R.menu.map_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                if (mType == 1) {
                    Toast.makeText(requireContext(), R.string.map_type_notify,
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mType = 1;
                return true;
            case R.id.hybrid_map:
                if (mType == 2) {
                    Toast.makeText(requireContext(), R.string.map_type_notify,
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mType = 2;
                return true;
            case R.id.satellite_map:
                if (mType == 3) {
                    Toast.makeText(requireContext(), R.string.map_type_notify,
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mType = 3;
                return true;
            case R.id.terrain_map:
                if (mType == 4) {
                    Toast.makeText(requireContext(), R.string.map_type_notify,
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mType = 4;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            }
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_FRAGMENT, isFragmentDisplayed);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}