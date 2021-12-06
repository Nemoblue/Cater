package com.example.cater.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cater.R;
import com.example.cater.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private RecyclerView mRvHome;
    private HomeAdapter homeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mRvHome = binding.mRvHome;
        initView();
        return root;
    }

    private void initView(){
        Random random = new Random();
        List<RestaurantBean> list = new ArrayList<>();
        list.add(new RestaurantBean("Can.teen II",R.mipmap.a1, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("lg7 Kitchen 1 Asia Pacific Catering",R.mipmap.a2, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("lg7 kitchen 2 Gold Rice Bowl Delicious Food",R.mipmap.a3, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("lg7 kitchen 3 TT Veggie",R.mipmap.a4, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("Oliver's Super Sanwitches",R.mipmap.a5, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("China Garden",R.mipmap.a6, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("McDonald's",R.mipmap.a7, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("McCafe",R.mipmap.a8, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("Passion",R.mipmap.a9, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("Hungry Korean",R.mipmap.a10, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("American Diner",R.mipmap.a11, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("Diners@LSKBB",R.mipmap.a12, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("Ebeneezer's",R.mipmap.a13, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("UniBistro",R.mipmap.a14, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("UniQue",R.mipmap.a15, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("Starbucks Coffee",R.mipmap.a16, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("Pacific Coffee",R.mipmap.a17, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("The UniBar",R.mipmap.a18, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("Subway",R.mipmap.a19, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("eafront Cafeteria",R.mipmap.a20, random.nextInt(9999)+"m"));
        list.add(new RestaurantBean("Food Truck",R.mipmap.a21, random.nextInt(9999)+"m"));

        homeAdapter = new HomeAdapter(getActivity(),list);
        mRvHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvHome.setAdapter(homeAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}