package com.example.cater.ui.home;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cater.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeVH> {
    Context context;
    List<RestaurantBean> list;

    public HomeAdapter(Context context, List<RestaurantBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeVH(LayoutInflater.from(context).inflate(R.layout.item_home,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeVH holder, int position) {
        holder.mTvName.setText(list.get(position).getName());
        holder.mTvDis.setText(list.get(position).getDistance());
        Glide.with(context).load(list.get(position).getResId()).into(holder.mImageView);
        holder.mItem.setOnClickListener(view -> {
            Intent intent = new Intent(context, BookingActivity.class);
            intent.putExtra("restaurant",list.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeVH extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mTvName;
        TextView mTvDis;
        View mItem;

        public HomeVH(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.mImageView);
            mTvName = itemView.findViewById(R.id.mTvName);
            mTvDis = itemView.findViewById(R.id.mTvDis);
            mItem = itemView.findViewById(R.id.mItem);
        }
    }

}
