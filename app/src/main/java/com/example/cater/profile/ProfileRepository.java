package com.example.cater.profile;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.RoomDatabase;

import java.lang.ref.WeakReference;
import java.lang.reflect.Parameter;
import java.util.List;

public class ProfileRepository {
    private ProfileDao mProfileDao;
    private LiveData<List<Profile>> mAllProfiles;
    private LiveData<List<Profile>> mActiveProfiles;
    private LiveData<Profile> mProfile;

    ProfileRepository(Application application) {
        ProfileRoomDatabase db = ProfileRoomDatabase.getDatabase(application);
        mProfileDao = db.profileDao();
        mAllProfiles = mProfileDao.getAllProfiles();
        mActiveProfiles = mProfileDao.getActiveProfiles();
    }

    LiveData<List<Profile>> getAllProfiles() {
        return mAllProfiles;
    }

    LiveData<List<Profile>> getActiveProfiles() {return mActiveProfiles;}

    LiveData<Profile> getProfileByID(int uid) {
        getProfileByIDAsyncTask task =
                new getProfileByIDAsyncTask(mProfileDao);
        task.setProfileListener(new getProfileByIDAsyncTask.ProfileListener() {
            @Override
            public void getProfileSuccess(LiveData<Profile> profile) {
                setmProfile(profile);
            }

            @Override
            public void getProfileFailed() {
                mProfile = null;
            }
        });
        task.execute(uid);
        return mProfile;
    }

    private void setmProfile(LiveData<Profile> profile) {
        this.mProfile = profile;
    }

    public void deleteAll() {
        new deleteAllProfilesAsyncTask(mProfileDao).execute();
    }

    public void insert(Profile profile) {
        new insertAsyncTask(mProfileDao).execute(profile);
    }

    public void deleteProfile(Profile profile) {
        new deleteProfileAsyncTask(mProfileDao).execute(profile);
    }

    private static class getProfileByIDAsyncTask extends AsyncTask<Integer, Void, LiveData<Profile>> {
        private ProfileDao mAsyncTaskDao;
        ProfileListener listener;

        public void setProfileListener(ProfileListener profileListener) {
            this.listener = profileListener;
        }

        public static interface ProfileListener {
            void getProfileSuccess(LiveData<Profile> profile);

            void getProfileFailed();
        }

        getProfileByIDAsyncTask(ProfileDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LiveData<Profile> doInBackground(Integer... integers) {
            return mAsyncTaskDao.getProfileByID(integers[0]);
        }

        @Override
        protected void onPostExecute(LiveData<Profile> result) {
            if (result != null)
                listener.getProfileSuccess(result);
            else
                listener.getProfileFailed();
        }
    }

    private static class insertAsyncTask extends AsyncTask<Profile, Void, Void> {
        private ProfileDao mAsyncTaskDao;

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
        private ProfileDao mAsyncTaskDao;

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
        private ProfileDao mAsyncTaskDao;

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
