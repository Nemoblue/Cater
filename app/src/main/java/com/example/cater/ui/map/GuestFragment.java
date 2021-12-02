package com.example.cater.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cater.R;
import com.example.cater.profile.Profile;

public class GuestFragment extends Fragment {
    private static final String PROFILE = "profile";
    private Profile mProfile;

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
                Toast.makeText(requireContext(), "Not yet complemented.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        assert getArguments() != null;
        if (getArguments().containsKey(PROFILE)) {
            mProfile = (Profile) getArguments().getSerializable(PROFILE);

            if (mProfile != null) {
                final ImageView imageView = rootView.findViewById(R.id.map_guest_photo);
                final TextView guestName = rootView.findViewById(R.id.map_guest_name);
                final TextView guestDescription = rootView.findViewById(R.id.map_guest_description);

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