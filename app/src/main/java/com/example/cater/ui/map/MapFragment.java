package com.example.cater.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cater.R;
import com.example.cater.databinding.FragmentMapBinding;
import com.example.cater.profile.Profile;
import com.example.cater.profile.ProfileViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int mType; // 1-4 from normal map to terrain map
    private int mSampleSize = 5;
    ArrayList<Marker> mSampleMarker = new ArrayList<Marker>();
    private ProfileViewModel mProfileViewModel;
    private List<Profile> mProfiles;
    private FragmentMapBinding binding;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        binding.buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i < mSampleMarker.size(); i++)
                    mSampleMarker.get(i).remove();
                mSampleMarker.clear();

                setProfileMarker(mProfiles);
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

        // Set the initial position and zoom level
        float zoom = 18;
        LatLng hkust = new LatLng(22.33653, 114.26363);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hkust, zoom));
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        mProfileViewModel.getAllProfiles().observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(@Nullable final List<Profile> profiles) {
                setProfileMarker(profiles);
            }
        });

        setMapLongClick(mMap);
        setPoiClick(mMap);
        enableMyLocation();
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
            int profile_size = mProfiles.size();
            int sample_size = Math.min(mSampleSize, profile_size);
            ArrayList<Integer> list = new ArrayList<Integer>();

            while(list.size() < sample_size) {
                int sample = rand.nextInt(profile_size);
                boolean flag = true;
                for (int i=0; i < list.size(); i++) {
                    if (sample == list.get(i))
                        flag = false;
                }
                if (flag)
                    list.add(sample);
            }

            for (int j = 0; j < sample_size; j++) {
                Profile current = mProfiles.get(list.get(j));
                String current_name = current.getuName();
                LatLng current_latLng = new LatLng(current.getPosition()[0], current.getPosition()[1]);
                String current_snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}