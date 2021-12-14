package com.example.cater.appointment;

import android.app.Application;
import android.os.AsyncTask;
import android.text.format.Time;

import androidx.lifecycle.LiveData;

import java.util.List;


public class AppointmentRepository {
    private final AppointmentDao mAppointmentDao;

    AppointmentRepository(Application application) {
        AppointmentDatabase db = AppointmentDatabase.getDatabase(application);
        mAppointmentDao = db.appointmentDao();
    }

    LiveData<List<Appointment>> getAppointmentByCanteen(int canteen_id) {
        return mAppointmentDao.getAppointmentByCanteen(canteen_id);
    }

    Appointment[] getAll() {
        return mAppointmentDao.getAnyAppointment();
    }

    LiveData<List<Appointment>> getAppointmentByTime(int canteen_id, Time time) {
        return mAppointmentDao.getAppointmentByTime(canteen_id, time.toString());
    }
    LiveData<Appointment> getAppointmentByID(int appoint_id) {
        return mAppointmentDao.getAppointmentByID(appoint_id);
    }


    public void deleteAll() {
        new deleteAllAppointmentsAsyncTask(mAppointmentDao).execute();
    }

    public void insert(Appointment appointment) {
        new AppointmentRepository.insertAsyncTask(mAppointmentDao).execute(appointment);
    }

    public void deleteAppointment(Appointment appointment) {
        new AppointmentRepository.deleteAppointmentAsyncTask(mAppointmentDao).execute(appointment);
    }

    private static class insertAsyncTask extends AsyncTask<Appointment, Void, Void> {
        private final AppointmentDao mAsyncTaskDao;

        insertAsyncTask(AppointmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Appointment... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllAppointmentsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final AppointmentDao mAsyncTaskDao;

        deleteAllAppointmentsAsyncTask(AppointmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteAppointmentAsyncTask extends AsyncTask<Appointment, Void, Void> {
        private final AppointmentDao mAsyncTaskDao;

        deleteAppointmentAsyncTask(AppointmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Appointment... params) {
            mAsyncTaskDao.deleteAppointment(params[0]);
            return null;
        }
    }
}
