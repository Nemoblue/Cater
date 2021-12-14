/*
 * Copyright (c) 2021.   # COMP 4521 #
 * # SHEN, Ye #	 20583137	yshenat@connect.ust.hk
 * # ZHOU, Ji #	 20583761	jzhoubl@connect.ust.hk
 * # WU, Sik Chit #	 20564571	scwuaa@connect.ust.hk
 */

package com.example.cater.profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    private final ProfileRepository mRepository;
    private final LiveData<List<Profile>> mActiveProfiles;
    private LiveData<Profile> mProfile;

    public ProfileViewModel(Application application) {
        super(application);
        mRepository = new ProfileRepository(application);
        mActiveProfiles = mRepository.getActiveProfiles();
        mProfile = new MutableLiveData<>();
    }

    public LiveData<List<Profile>> getActiveProfiles() {
        return mActiveProfiles;
    }

    public int getTotalCount() {
        return mRepository.getTotalCount();
    }

    public void insert(Profile profile) {
        mRepository.insert(profile);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteProfile(Profile profile) {
        mRepository.deleteProfile(profile);
    }

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

    public LiveData<Profile> logout() {
        mProfile = new MutableLiveData<>();
        return mProfile;
    }
}
