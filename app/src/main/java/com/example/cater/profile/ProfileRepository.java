package com.example.cater.profile;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ProfileRepository {
    private final ProfileDao mProfileDao;
    private final LiveData<List<Profile>> mActiveProfiles;

    ProfileRepository(Application application) {
        ProfileRoomDatabase db = ProfileRoomDatabase.getDatabase(application);
        mProfileDao = db.profileDao();
        mActiveProfiles = mProfileDao.getActiveProfiles();
    }

    LiveData<List<Profile>> getActiveProfiles() {return mActiveProfiles;}

    LiveData<Profile> getProfileByID(int uid) { return mProfileDao.getProfileByID(uid); }

    int getTotalCount() { return mProfileDao.getTotalCount(); }

    public void deleteAll() {
        new deleteAllProfilesAsyncTask(mProfileDao).execute();
    }

    public void insert(Profile profile) {
        new insertAsyncTask(mProfileDao).execute(profile);
    }

    public void deleteProfile(Profile profile) {
        new deleteProfileAsyncTask(mProfileDao).execute(profile);
    }

    private static class insertAsyncTask extends AsyncTask<Profile, Void, Void> {
        private final ProfileDao mAsyncTaskDao;

        insertAsyncTask(ProfileDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Profile... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllProfilesAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ProfileDao mAsyncTaskDao;

        deleteAllProfilesAsyncTask(ProfileDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
        private final ProfileDao mAsyncTaskDao;

        deleteProfileAsyncTask(ProfileDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Profile... params) {
            mAsyncTaskDao.deleteProfile(params[0]);
            return null;
        }
    }
}
