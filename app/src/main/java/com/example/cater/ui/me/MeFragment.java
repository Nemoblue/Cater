package com.example.cater.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cater.R;
import com.example.cater.databinding.FragmentMeBinding;
import com.example.cater.ui.map.MapFragment;

import java.io.InputStream;
import java.net.URI;

public class MeFragment extends Fragment {

    private MeViewModel meViewModel;
    private FragmentMeBinding binding;
    private ImageView userIcon;
    public static final int PICK_IMAGE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        meViewModel =
                new ViewModelProvider(this).get(MeViewModel.class);

        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textMe;
        meViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        userIcon = root.findViewById(R.id.user_icon);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        final TextView userName = root.findViewById(R.id.user_name);
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName.setText("TEST");
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            android.net.Uri uri = data.getData();
            userIcon.setImageURI(uri);
        }
    }
}