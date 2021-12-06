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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cater.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultVH> {
    Context context;

    public ResultAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ResultVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultVH(LayoutInflater.from(context).inflate(R.layout.item_result,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class ResultVH extends RecyclerView.ViewHolder{
        RoundedImageView mIvHead;
        TextView mTvName;
        TextView mTvDesc;
        TextView mTvInvitation;

        public ResultVH(@NonNull View itemView) {
            super(itemView);
            mIvHead = itemView.findViewById(R.id.mIvHead);
            mTvName = itemView.findViewById(R.id.mTvName);
            mTvDesc = itemView.findViewById(R.id.mTvDesc);
            mTvInvitation = itemView.findViewById(R.id.mTvInvitation);
        }
    }

}
