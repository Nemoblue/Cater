package com.example.cater.ui.home;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cater.R;
import com.example.cater.appointment.Appointment;

import java.net.URLEncoder;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultVH> {
    Context context;
    private List<Appointment> appointments;
    private final OnInvitationCallback callback;

    public ResultAdapter(Context context, OnInvitationCallback callback) {
        this.context = context;
        this.callback = callback;
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
            holder.mTvDesc.setText(current.getTarget_date().toString());

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
            holder.mTvInvitation.setOnClickListener(v-> {
                String message = "Hello! This is a greeting from Cater APP!";
                PackageManager packageManager = context.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone=" + current.getUser_phone() + "&text=" + URLEncoder.encode(message, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        context.startActivity(i);
                    }
                } catch (Exception e) {
                    Toast.makeText(context,
                            "Open Whatsapp failed, please check whether the app is on your phone!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                if (callback != null) callback.callback(holder.mTvInvitation, current);
            });
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

    @SuppressLint("NotifyDataSetChanged")
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

    public interface OnInvitationCallback {
        void callback(View view, Appointment appointment);
    }

}
