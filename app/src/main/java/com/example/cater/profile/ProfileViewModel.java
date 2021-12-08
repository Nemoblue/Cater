package com.example.cater.profile;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    private ProfileRepository mRepository;
    private LiveData<List<Profile>> mAllProfiles;
    private LiveData<List<Profile>> mActiveProfiles;
    private LiveData<Profile> mProfile;

    public ProfileViewModel (Application application) {
        super(application);
        mRepository = new ProfileRepository(application);
        mAllProfiles = mRepository.getAllProfiles();
        mActiveProfiles = mRepository.getActiveProfiles();
    }

    public LiveData<List<Profile>> getAllProfiles() { return mAllProfiles; }
    public LiveData<List<Profile>> getActiveProfiles() {return mActiveProfiles;}
    public void insert(Profile profile) { mRepository.insert(profile); }
    public void deleteAll() {mRepository.deleteAll();}
    public void deleteProfile(Profile profile) {mRepository.deleteProfile(profile);}

    public LiveData<Profile> getProfileByID(int uid) {
        return mRepository.getProfileByID(uid);
    }
}
