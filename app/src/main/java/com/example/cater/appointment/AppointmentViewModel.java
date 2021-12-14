/*
 * Copyright (c) 2021.   # COMP 4521 #
 * # SHEN, Ye #	 20583137	yshenat@connect.ust.hk
 * # ZHOU, Ji #	 20583761	jzhoubl@connect.ust.hk
 * # WU, Sik Chit #	 20564571	scwuaa@connect.ust.hk
 */

package com.example.cater.appointment;

import android.app.Application;
import android.text.format.Time;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {
    private final AppointmentRepository mRepository;
    private LiveData<List<Appointment>> mCurrentAppointments;
    private LiveData<Appointment> mAppointment;

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppointmentRepository(application);
        mCurrentAppointments = new MutableLiveData<>();
        mAppointment = new MutableLiveData<>();
    }

    public void insert(Appointment appointment) {
        mRepository.insert(appointment);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteAppointment(Appointment appointment) {
        mRepository.deleteAppointment(appointment);
    }

    public LiveData<List<Appointment>> getAppointmentByCanteen(int canteen_id) {
        if (mRepository.getAppointmentByCanteen(canteen_id) == null)
            return new MutableLiveData<>();
        mCurrentAppointments = mRepository.getAppointmentByCanteen(canteen_id);
        return mCurrentAppointments;
    }

    public int getSize() {
        return mRepository.getAll().length;
    }

    public LiveData<List<Appointment>> getAppointmentByTime(int canteen_id, Time time) {
        if (mRepository.getAppointmentByTime(canteen_id, time) == null)
            return new MutableLiveData<>();
        mCurrentAppointments = mRepository.getAppointmentByTime(canteen_id, time);
        return mCurrentAppointments;
    }

    public LiveData<Appointment> getAppointmentByID(int appoint_id) {
        if (mRepository.getAppointmentByID(appoint_id) == null)
            return new MutableLiveData<>();
        mAppointment = mRepository.getAppointmentByID(appoint_id);
        return mAppointment;
    }
}
