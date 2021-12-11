package com.example.cater.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cater.MainActivity;
import com.example.cater.R;
import com.example.cater.profile.Profile;

import java.net.URLEncoder;
import java.util.Objects;

public class GuestFragment extends Fragment {
    private static final String PROFILE = "profile";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private Profile mProfile;
    private String mPhone;

    public GuestFragment() {
        // Required empty public constructor
    }

    public static GuestFragment newInstance(Profile profile) {
        GuestFragment fragment = new GuestFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(PROFILE, profile);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =
                inflater.inflate(R.layout.fragment_guest, container, false);
        final ImageButton imageButton = rootView.findViewById(R.id.guest_image_button);
        final Button chatButton = rootView.findViewById(R.id.map_guest_chat);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getParentFragment() != null;
                ((MapFragment) getParentFragment()).removeFragment();
            }
        });
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Hello! This is a greeting from Cater APP!";
                PackageManager packageManager = requireContext().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone=" + mPhone + "&text=" + URLEncoder.encode(message, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        requireContext().startActivity(i);
                    }
                } catch (Exception e) {
                    Toast.makeText(requireContext(),
                            "Open Whatsapp failed, please check whether the app is on your phone!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        assert getArguments() != null;
        if (getArguments().containsKey(PROFILE)) {
            mProfile = (Profile) getArguments().getSerializable(PROFILE);

            if (mProfile != null) {
                mPhone = mProfile.getuPhone();
                final ImageView guestPhoto = rootView.findViewById(R.id.map_guest_photo);
                final TextView guestName = rootView.findViewById(R.id.map_guest_name);
                final TextView guestDescription = rootView.findViewById(R.id.map_guest_description);

                if (mProfile.getPhoto() != null) {
                    String photoPath = mProfile.getPhoto();
                    if (photoPath.startsWith("default")) {
                        int index = Integer.parseInt(photoPath.substring(photoPath.length() - 1));
                        TypedArray profilePhotoResources =
                                getResources().obtainTypedArray(R.array.profile_photos);
                        Glide.with(getContext()).load(profilePhotoResources.getResourceId(index, 0)).into(guestPhoto);

                        profilePhotoResources.recycle();
                    }
                }
                if (mProfile.getuName() == null)
                    guestName.setText(R.string.default_name);
                else
                    guestName.setText(mProfile.getuName());

                if (mProfile.getDescription() == null)
                    guestDescription.setText(R.string.default_description);
                else
                    guestDescription.setText(mProfile.getDescription());
            }
        }

        return rootView;
    }
}