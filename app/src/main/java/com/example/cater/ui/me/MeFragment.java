package com.example.cater.ui.me;


import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.cater.R;
import com.example.cater.databinding.FragmentMeBinding;
import com.example.cater.profile.Profile;
import com.example.cater.profile.ProfileViewModel;
import com.example.cater.ui.login.LoginActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MeFragment extends Fragment {

    private MeViewModel meViewModel;
    private ProfileViewModel profileViewModel;
    private FragmentMeBinding binding;

    private ImageView user_icon;
    private Button login;
    private TextView user_name;
    private TextView user_id;
    private TextView user_age;
    private TextView user_tag;
    private TextView user_description;

    public static final int PICK_IMAGE = 1;
    public static final int LOGIN_REQUEST = 2;
    static final String STATE_FRAGMENT = "state_of_fragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        meViewModel =
                new ViewModelProvider(this).get(MeViewModel.class);
        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);

        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user_id = root.findViewById(R.id.user_id);
        user_icon = root.findViewById(R.id.user_icon);
        user_name = root.findViewById(R.id.user_name);
        user_tag = root.findViewById(R.id.user_tag);
        user_description = root.findViewById(R.id.user_description);
        user_age = root.findViewById(R.id.user_age);
        login = root.findViewById(R.id.button);
        //final TextView textView = binding.textMe;
        meViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        if(profileViewModel.getProfile() != null) {
            profileViewModel.getProfile().observe(getViewLifecycleOwner(), new Observer<Profile>() {
                @Override
                public void onChanged(Profile profile) {
                    setUI(profile);
                }
            });
        }
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST);
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
            user_icon.setImageURI(uri);
        }

        if(requestCode == LOGIN_REQUEST && resultCode == Activity.RESULT_OK) {
            int result = data.getIntExtra(LoginActivity.EXTRA_REPLY, 0);
            profileViewModel.saveProfile(result);
            new updateUiAsyncTask(profileViewModel).execute(result);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        ArrayList<String> texts = new ArrayList<>();
        texts.add(user_name.getText().toString());
        texts.add(user_age.getText().toString());
        texts.add(user_id.getText().toString());
        texts.add(user_tag.getText().toString());
        texts.add(user_description.getText().toString());
        savedInstanceState.putStringArrayList(STATE_FRAGMENT, texts);
    }

    public void setUI(Profile profile) {
        user_name.setText(profile.getuName());
        user_age.setText(String.valueOf(profile.getAge()));
        user_id.setText(String.valueOf(profile.getUid()));
        //user_tag.setText(profile.getTag());
        user_description.setText(profile.getDescription());

        if (profile.getPhoto() != null) {
            String photoPath = profile.getPhoto();
            if (photoPath.startsWith("default")) {
                int index = Integer.parseInt(photoPath.substring(photoPath.length() - 1));
                TypedArray profilePhotoResources =
                        getResources().obtainTypedArray(R.array.profile_photos);
                Glide.with(getContext()).load(profilePhotoResources.getResourceId(index, 0)).into(user_icon);

                profilePhotoResources.recycle();
            }
        }
    }

    private  class updateUiAsyncTask extends AsyncTask<Integer, Void, LiveData<Profile>> {

        private ProfileViewModel mAsyncTaskViewModel;
        updateUiAsyncTask(ProfileViewModel model) { mAsyncTaskViewModel = model; }

        @Override
        protected LiveData<Profile> doInBackground(Integer...Integers) {
            return mAsyncTaskViewModel.getProfileByID(Integers[0]);
        }

        protected void onPostExecute(LiveData<Profile> result) {
            result.observe(getViewLifecycleOwner(), new Observer<Profile>() {
                @Override
                public void onChanged(Profile profile) {
                    setUI(profile);
                }
            });
        }
    }

    //todo setting things should be update to database, register not implemented, login failure not implemented....kill programs need to save profile...
}