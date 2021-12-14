package com.example.cater.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cater.R;
import com.example.cater.appointment.Appointment;
import com.example.cater.appointment.AppointmentViewModel;
import com.example.cater.profile.Profile;
import com.example.cater.tools.BasisTimesUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * create by liubit on 2021/12/4
 */
public class BookingActivity extends AppCompatActivity {
    private RestaurantBean restaurantBean;
    private Profile mProfile;
    private String target_date;
    private String target_time;
    private TextView mBtnDate;
    private TextView mBtnFromTime;
    private ResultAdapter resultAdapter;

    private AppointmentViewModel appointmentViewModel;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        restaurantBean = (RestaurantBean) getIntent().getSerializableExtra("restaurant");
        mProfile = (Profile) getIntent().getSerializableExtra("profile");
        initView();
    }

    private void initView() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        target_date = null;
        mBtnDate = findViewById(R.id.mBtnDate);
        mBtnFromTime = findViewById(R.id.mBtnFromTime);
        RecyclerView mRvResult = findViewById(R.id.mRvResult);

        mBtnDate.setOnClickListener(view -> showSelectDate(0));
        mBtnFromTime.setOnClickListener(view -> showSelectTime("请选择开始时间", 0));

        mRvResult.setLayoutManager(new LinearLayoutManager(this));
        resultAdapter = new ResultAdapter(this, (view, appointment) ->
                Toast.makeText(BookingActivity.this, "Successful invitation", Toast.LENGTH_SHORT).show());
        mRvResult.setAdapter(resultAdapter);

        appointmentViewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);
        appointmentViewModel.getAppointmentByCanteen(restaurantBean.getResId()).observe(this, appointments -> {
            ArrayList<Appointment> list = new ArrayList<>(appointments);
            List<Appointment> refuseList = new ArrayList<>();
            boolean refuse = false;
            StringBuilder nameSb = new StringBuilder();
            for (Appointment appointment : appointments) {
                if (System.currentTimeMillis() - appointment.getAppoint_date().getTime() > 1000 * 60 * 3) {
                    list.remove(appointment);
                    refuseList.add(appointment);
                    nameSb.append(appointment.getUser_name()).append(",");
                    refuse = true;
                }
            }
            // Update the cached copy of the appointments in the adapter.
            resultAdapter.setAppointments(list);
            processRefuseAppointments(refuse, nameSb, refuseList);
        });

        findViewById(R.id.layout_reserve).setOnClickListener(v -> {
            if (mProfile == null) {
                Toast.makeText(BookingActivity.this, "Please login in to continue!", Toast.LENGTH_SHORT).show();
            } else {
                int uid = mProfile.getUid();
                String name = mProfile.getuName();
                String photo = mProfile.getPhoto();
                String phone = mProfile.getuPhone();
                Date appoint_date = new Date(System.currentTimeMillis());
                String target = target_date + " " + target_time;
                if (target_date == null || target_time == null) {
                    if (target_date == null && target_time == null)
                        target = "Current";
                    else if (target_time== null) {
                        String[] strs_time = BasisTimesUtils.getNowTime().split(":");
                        target = target_date + " " + strs_time[0] + ":" + strs_time[1];
                    } else {
                        String[] strs_date = BasisTimesUtils.getNowDate().split("-");//yyyy-MM-dd
                        target = strs_date[0] + "-" + strs_date[1] + "-" + strs_date[2]
                                + " " + target_time;
                    }
                }
                int id = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(5));
                Appointment appointment = new Appointment.Builder(id, restaurantBean.getResId(), uid, appoint_date, target)
                        .name(name)
                        .photo(photo)
                        .phone(phone)
                        .builder();
                appointmentViewModel.insert(appointment);
                handler.post(() ->
                        Toast.makeText(BookingActivity.this, "Reserve successfully!", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void processRefuseAppointments(boolean refuse, StringBuilder nameSb, List<Appointment> refuseList) {
        if (refuse) {
            for (Appointment appointment : refuseList) {
                appointmentViewModel.deleteAppointment(appointment);
            }
            String names = nameSb.substring(0, nameSb.toString().length() - 1);
            new AlertDialog.Builder(BookingActivity.this)
                    .setTitle("Expire")
                    .setMessage(String.format("%s's appointments has been expired.", names))
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }

    private void showSelectDate(int type) {
        String[] strs = BasisTimesUtils.getNowDate().split("-");//yyyy-MM-dd
        BasisTimesUtils.showDatePickerDialog(this, true, "请选择日期", Integer.parseInt(strs[0]), Integer.parseInt(strs[1]), Integer.parseInt(strs[2]), new BasisTimesUtils.OnDatePickerListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                String mm = month > 9 ? "" + month : "0" + month;
                String dd = dayOfMonth > 9 ? "" + dayOfMonth : "0" + dayOfMonth;
                if (type == 0) {
                    target_date = year + "-" + mm + "-" + dd;
                    mBtnDate.setText(year + "-" + mm + "-" + dd);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void showSelectTime(String tip, int type) {
        String[] strs = BasisTimesUtils.getNowTime().split(":");//yyyy-MM-dd
        BasisTimesUtils.showTimerPickerDialog(this, true, tip, Integer.parseInt(strs[0]), Integer.parseInt(strs[1]), true, new BasisTimesUtils.OnTimerPickerListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onConfirm(int hourOfDay, int minute) {
                String hour = hourOfDay > 9 ? "" + hourOfDay : "0" + hourOfDay;
                String mm = minute > 9 ? "" + minute : "0" + minute;
                if (type == 0) {
                    target_time = hour + ":" + mm;
                    mBtnFromTime.setText(hour + ":" + mm);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
