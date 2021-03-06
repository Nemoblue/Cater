/*
 * Copyright (c) 2021.   # COMP 4521 #
 * # SHEN, Ye #	 20583137	yshenat@connect.ust.hk
 * # ZHOU, Ji #	 20583761	jzhoubl@connect.ust.hk
 * # WU, Sik Chit #	 20564571	scwuaa@connect.ust.hk
 */

package com.example.cater.appointment;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cater.R;

import java.util.Date;

@Database(entities = {Appointment.class}, version = 7, exportSchema = false)
public abstract class AppointmentDatabase extends RoomDatabase {
    private static AppointmentDatabase INSTANCE;
    private static final Callback sRoomDatabaseCallback =
            new Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    public static AppointmentDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppointmentDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppointmentDatabase.class, "appointment_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract AppointmentDao appointmentDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppointmentDao mDao;

        PopulateDbAsync(AppointmentDatabase db) {
            mDao = db.appointmentDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //mDao.deleteAll();
            if (mDao.getAnyAppointment().length < 1) {
                // Populate the database
                Date date = new Date(System.currentTimeMillis());
                Appointment appointment1 = new Appointment.Builder(0, R.mipmap.a2, 0, date, date.toString().substring(0, 19))
                        .name("Jason").photo("default_1").builder();
                Appointment appointment2 = new Appointment.Builder(1, R.mipmap.a1, 1, date, date.toString().substring(0, 19))
                        .name("Monica").photo("default_2").builder();
                Appointment appointment3 = new Appointment.Builder(2, R.mipmap.a1, 3, date, date.toString().substring(0, 19))
                        .name("David").photo("default_4").builder();
                mDao.insert(appointment1);
                mDao.insert(appointment2);
                mDao.insert(appointment3);
            }
            return null;
        }
    }
}
