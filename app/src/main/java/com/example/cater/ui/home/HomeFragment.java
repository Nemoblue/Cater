package com.example.cater.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cater.R;
import com.example.cater.databinding.FragmentHomeBinding;
import com.example.cater.profile.Profile;
import com.example.cater.profile.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class HomeFragment extends Fragment {

    private ProfileViewModel mProfileViewModel;
    private Profile mProfile;
    private double[] mCurrentPosition;
    private FragmentHomeBinding binding;
    private RecyclerView mRvHome;
    private HomeAdapter homeAdapter;
    List<RestaurantBean> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mRvHome = binding.mRvHome;
        initView();

        mProfileViewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        mProfileViewModel.getProfile().observe(requireActivity(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                mProfile = profile;
                if (mProfile != null) {
                    mCurrentPosition = mProfile.getPosition();
                    updateView(mCurrentPosition);
                }
            }
        });
        return root;
    }

    public void initView() {
        list.add(0, new RestaurantBean("Can.teen II", R.mipmap.a1, "--m",
                22.337339543105305, 114.26412914531693));
        list.add(1, new RestaurantBean("lg7 Kitchen 1 Asia Pacific Catering", R.mipmap.a2, "--m",
                22.33681707682137, 114.26350332092574));
        list.add(2, new RestaurantBean("lg7 kitchen 2 Gold Rice Bowl Delicious Food", R.mipmap.a3, "--m",
                22.33759992960767, 114.26411181860284));
        list.add(3, new RestaurantBean("lg7 kitchen 3 TT Veggie", R.mipmap.a4, "--m",
                22.33759992960767, 114.26411181860284));
        list.add(4, new RestaurantBean("Oliver's Super Sanwitches", R.mipmap.a5, "--m",
                22.33759992960767, 114.26411181860284));
        list.add(5, new RestaurantBean("China Garden", R.mipmap.a6, "--m",
                22.337227625399553, 114.26404302181928));
        list.add(6, new RestaurantBean("McDonald's", R.mipmap.a7, "--m",
                22.337559007508325, 114.26410575958978));
        list.add(7, new RestaurantBean("McCafe", R.mipmap.a8, "--m",
                22.337559007508325, 114.26410575958978));
        list.add(8, new RestaurantBean("Passion", R.mipmap.a9, "--m",
                22.33635396402903, 114.26390163568597));
        list.add(9, new RestaurantBean("Hungry Korean", R.mipmap.a10, "--m",
                22.33635396402903, 114.26390163568597));
        list.add(10, new RestaurantBean("American Diner", R.mipmap.a11, "--m",
                22.33635396402903, 114.26390163568597));
        list.add(11, new RestaurantBean("Diners@LSKBB", R.mipmap.a12, "--m",
                22.332993737594673, 114.26497357769017));
        list.add(12, new RestaurantBean("Ebeneezer's", R.mipmap.a13, "--m",
                22.333152545003255, 114.26486814163971));
        list.add(13, new RestaurantBean("UniBistro", R.mipmap.a14, "--m",
                22.335895026128227, 114.26514859541875));
        list.add(14, new RestaurantBean("UniQue", R.mipmap.a15, "--m",
                22.33241041061079, 114.26644389296638));
        list.add(15, new RestaurantBean("Starbucks Coffee", R.mipmap.a16, "--m",
                22.337843347141874, 114.26345203011249));
        list.add(16, new RestaurantBean("Pacific Coffee", R.mipmap.a17, "--m",
                22.336923749515933, 114.26347538665227));
        list.add(17, new RestaurantBean("The UniBar", R.mipmap.a18, "--m",
                22.335895026128227, 114.26514859541875));
        list.add(18, new RestaurantBean("Subway", R.mipmap.a19, "--m",
                22.334878712068953, 114.26362979123891));
        list.add(19, new RestaurantBean("Seafront Cafeteria", R.mipmap.a20, "--m",
                22.337368829442283, 114.26675549157589));
        list.add(20, new RestaurantBean("Food Truck", R.mipmap.a21, "--m",
                22.33691285590851, 114.26345117694139));

        homeAdapter = new HomeAdapter(getActivity(), list);
        mRvHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvHome.setAdapter(homeAdapter);
    }

    public void updateView(double[] mCurrentPosition) {
        double EARTH_RADIUS = 6378.137;

        for (int i = 0; i < list.size(); i++) {
            double[] mPosition = list.get(i).getLatLng();
            double radLat1 = rad(mPosition[0]);
            double radLat2 = rad(mCurrentPosition[0]);
            double a = radLat1 - radLat2;
            double b = rad(mPosition[1]) - rad(mCurrentPosition[1]);
            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                    Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
            s = s * EARTH_RADIUS * 1000;

            String distance;
            if (s < 100)
                distance = "<100m";
            else
                distance = String.format(Locale.getDefault(), "%.1fm", s);
            list.get(i).setDistance(distance);
        }
        homeAdapter = new HomeAdapter(getActivity(), list);
        mRvHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvHome.setAdapter(homeAdapter);
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}