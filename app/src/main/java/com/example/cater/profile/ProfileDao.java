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

    @Query("SELECT * from profile_table LIMIT 3")
    Profile[] getThreeProfile();

    @Query("SELECT * from profile_table LIMIT 1")
    Profile[] getAnyProfile();
}
