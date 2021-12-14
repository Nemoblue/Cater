/*
 * Copyright (c) 2021.   # COMP 4521 #
 * # SHEN, Ye #	 20583137	yshenat@connect.ust.hk
 * # ZHOU, Ji #	 20583761	jzhoubl@connect.ust.hk
 * # WU, Sik Chit #	 20564571	scwuaa@connect.ust.hk
 */

package com.example.cater.appointment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppointmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Appointment appointment);

    @Query("DELETE FROM appointment_table")
    void deleteAll();

    @Delete
    void deleteAppointment(Appointment appointment);

    @Query("SELECT * FROM appointment_table LIMIT 1")
    Appointment[] getAnyAppointment();

    @Query("SELECT * FROM appointment_table " +
            "WHERE canteen_id = :canteen_id " +
            "ORDER BY appoint_id ASC")
    LiveData<List<Appointment>> getAppointmentByCanteen(int canteen_id);

    @Query("SELECT * FROM appointment_table " +
            "WHERE canteen_id = :canteen_id AND appoint_date <= :time " +
            "ORDER BY appoint_id ASC")
    LiveData<List<Appointment>> getAppointmentByTime(int canteen_id, String time);

    @Query("SELECT * FROM appointment_table WHERE appoint_id = :appoint_id")
    LiveData<Appointment> getAppointmentByID(int appoint_id);
}
