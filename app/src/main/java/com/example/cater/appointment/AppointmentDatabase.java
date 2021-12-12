package com.example.cater.appointment;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.Time;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cater.R;

import java.util.Date;

@Database(entities = {Appointment.class}, version = 1, exportSchema = false)
public abstract class AppointmentDatabase extends RoomDatabase {
    public abstract AppointmentDao appointmentDao();

    private static AppointmentDatabase INSTANCE;

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

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppointmentDao mDao;
        PopulateDbAsync(AppointmentDatabase db) { mDao = db.appointmentDao();}

        @Override
        protected Void doInBackground(final Void... params) {
            //mDao.deleteAll();
            if (mDao.getAnyAppointment().length < 1) {
                // Populate the database
                Date date = new Date(System.currentTimeMillis());
                Appointment appointment1 = new Appointment(0, R.mipmap.a2, 0, date, date);
                Appointment appointment2 = new Appointment(1, R.mipmap.a1, 0, date, date);
                Appointment appointment3 = new Appointment(2, R.mipmap.a1, 0, date, date);
                mDao.insert(appointment1);
                mDao.insert(appointment2);
                mDao.insert(appointment3);
            }
            return null;
        }
    }
}