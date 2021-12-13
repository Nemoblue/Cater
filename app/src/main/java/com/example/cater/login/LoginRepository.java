package com.example.cater.login;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class LoginRepository {
    private LoginDao mLoginDao;
    private LiveData<Login> mLogin;

    public LoginRepository(@NonNull Application application) {
        LoginRoomDatabase db = LoginRoomDatabase.getDatabase(application);
        mLoginDao = db.loginDao();
        mLogin = new MutableLiveData<>();
    }

    public LiveData<Login> getLogin(String phone, String password) {
        if (mLoginDao.getLogin(phone, password) == null) {
            return new MutableLiveData<>();
        }
        mLogin = mLoginDao.getLogin(phone, password);
        return mLogin;
    }

    public void deleteAll() {
        new LoginRepository.deleteAllLoginsAsyncTask(mLoginDao).execute();
    }

    public void insert(Login login) {
        new LoginRepository.insertAsyncTask(mLoginDao).execute(login);
    }

    public void deleteLogin(Login login) {
        new LoginRepository.deleteLoginAsyncTask(mLoginDao).execute(login);
    }

    public Login[] getAnyLogin() { return mLoginDao.getAnyLogin(); }

    private static class insertAsyncTask extends AsyncTask<Login, Void, Void> {
        private LoginDao mAsyncTaskDao;

        insertAsyncTask(LoginDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Login... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllLoginsAsyncTask extends AsyncTask<Void, Void, Void> {
        private LoginDao mAsyncTaskDao;

        deleteAllLoginsAsyncTask(LoginDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteALL();
            return null;
        }
    }

    private static class deleteLoginAsyncTask extends AsyncTask<Login, Void, Void> {
        private LoginDao mAsyncTaskDao;

        deleteLoginAsyncTask(LoginDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Login... params) {
            mAsyncTaskDao.deleteLogin(params[0]);
            return null;
        }
    }
}
