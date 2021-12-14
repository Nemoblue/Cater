/*
 * Copyright (c) 2021.   # COMP 4521 #
 * # SHEN, Ye #	 20583137	yshenat@connect.ust.hk
 * # ZHOU, Ji #	 20583761	jzhoubl@connect.ust.hk
 * # WU, Sik Chit #	 20564571	scwuaa@connect.ust.hk
 */

package com.example.cater.profile;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Profile profile);

    @Query("DELETE FROM profile_table")
    void deleteAll();

    @Delete
    void deleteProfile(Profile profile);

    @Query("SELECT * FROM profile_table ORDER BY uid ASC")
    LiveData<List<Profile>> getAllProfiles();

    @Query("SELECT * FROM profile_table WHERE active = 1 ORDER BY RANDOM() LIMIT 20")
    LiveData<List<Profile>> getActiveProfiles();

    @Query("SELECT * FROM profile_table WHERE uid = :uid")
    LiveData<Profile> getProfileByID(int uid);

    @Query("SELECT * FROM profile_table LIMIT 1")
    Profile[] getAnyProfile();

    @Query("SELECT COUNT(uid) FROM profile_table")
    int getTotalCount();
}
