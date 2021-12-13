package com.example.cater.ui.home;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cater.R;
import com.example.cater.appointment.Appointment;
import com.example.cater.appointment.AppointmentViewModel;
import com.example.cater.profile.Profile;
import com.example.cater.profile.ProfileRepository;
import com.example.cater.profile.ProfileViewModel;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultVH> {
    Context context;
    private List<Appointment> appointments;

    public ResultAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ResultVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultVH(LayoutInflater.from(context).inflate(R.layout.item_result, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultVH holder, int position) {
        if (appointments != null) {
            Appointment current = appointments.get(position);
            holder.mTvDesc.setText(current.getAppoint_date().toString().substring(0, 19));

            if (current.getUser_name() != null)
                holder.mTvName.setText(current.getUser_name());
            else
                holder.mTvName.setText("Anonymous User");

            if (current.getUser_photo() != null) {
                String photoPath = current.getUser_photo();
                if (photoPath.startsWith("default")) {
                    int index = Integer.parseInt(photoPath.substring(photoPath.length() - 1));
                    TypedArray profilePhotoResources =
                            context.getResources().obtainTypedArray(R.array.profile_photos);
                    Glide.with(context).load(profilePhotoResources.getResourceId(index, 0)).into(holder.mIvHead);

                    profilePhotoResources.recycle();
                }
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.mTvName.setText("No Appointment");
        }
    }

    @Override
    public int getItemCount() {
        if (appointments != null)
            return appointments.size();
        else return 0;
    }

    void setAppointments(List<Appointment> appoints) {
        appointments = appoints;
        notifyDataSetChanged();
    }

    public static class ResultVH extends RecyclerView.ViewHolder {
        ImageView mIvHead;
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
