package com.example.cater.ui.home;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cater.R;
import com.example.cater.appointment.Appointment;
import com.example.cater.appointment.AppointmentViewModel;
import com.example.cater.tools.BasisTimesUtils;

import java.util.List;

/**
 * create by liubit on 2021/12/4
 */
public class BookingActivity extends AppCompatActivity {
    RestaurantBean restaurantBean;
    TextView mBtnDate;
    TextView mBtnFromTime;
    RecyclerView mRvResult;
    ResultAdapter resultAdapter;
    private AppointmentViewModel appointmentViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        restaurantBean = (RestaurantBean) getIntent().getSerializableExtra("restaurant");

        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("Booking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBtnDate = findViewById(R.id.mBtnDate);
        mBtnFromTime = findViewById(R.id.mBtnFromTime);
        mRvResult = findViewById(R.id.mRvResult);

        mBtnDate.setOnClickListener(view -> showSelectDate(0));
        mBtnFromTime.setOnClickListener(view -> showSelectTime("请选择开始时间", 0));

        mRvResult.setLayoutManager(new LinearLayoutManager(this));
        resultAdapter = new ResultAdapter(this);
        mRvResult.setAdapter(resultAdapter);

        appointmentViewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);
        appointmentViewModel.getAppointmentByCanteen(restaurantBean.getResId()).observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable final List<Appointment> appointments) {
                // Update the cached copy of the appointments in the adapter.
                resultAdapter.setAppointments(appointments);
            }
        });

    }

    private void showSelectDate(int type) {
        String[] strs = BasisTimesUtils.getNowDate().split("-");//yyyy-MM-dd
        BasisTimesUtils.showDatePickerDialog(this, true, "请选择日期", Integer.parseInt(strs[0]), Integer.parseInt(strs[1]), Integer.parseInt(strs[2]), new BasisTimesUtils.OnDatePickerListener() {
            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                String mm = month > 9 ? "" + month : "0" + month;
                String dd = dayOfMonth > 9 ? "" + dayOfMonth : "0" + dayOfMonth;
                if (type == 0) {
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
            @Override
            public void onConfirm(int hourOfDay, int minute) {
                String hour = hourOfDay > 9 ? "" + hourOfDay : "0" + hourOfDay;
                String mm = minute > 9 ? "" + minute : "0" + minute;
                if (type == 0) {
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
