package com.example.cater.profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
        mProfile = null;
    }

    public LiveData<List<Profile>> getAllProfiles() { return mAllProfiles; }
    public LiveData<List<Profile>> getActiveProfiles() {return mActiveProfiles;}
    public void insert(Profile profile) { mRepository.insert(profile); }
    public void deleteAll() {mRepository.deleteAll();}
    public void deleteProfile(Profile profile) {mRepository.deleteProfile(profile);}

    public LiveData<Profile> getProfileByID(int uid) {
        if (mRepository.getProfileByID(uid) == null) {
            return new MutableLiveData<>();
        }
        mProfile = mRepository.getProfileByID(uid);
        return mProfile;
    }
    public LiveData<Profile> getProfile() {
        if (mProfile == null) {
            return new MutableLiveData<>();
        }
        return mProfile;
    }

    public int getUidByLogin(String name, String password) { return mRepository.getUidByLogin(name, password); }
    public int getIdCount() { return mRepository.getIdCount(); }
    public int login(String username, String password) {
        int result = 0; //let jason to be default
        result = getUidByLogin(username, password);

        return result; //todo implement register situation
    }
}
