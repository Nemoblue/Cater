/*
 * Copyright (c) 2021.   # COMP 4521 #
 * # SHEN, Ye #	 20583137	yshenat@connect.ust.hk
 * # ZHOU, Ji #	 20583761	jzhoubl@connect.ust.hk
 * # WU, Sik Chit #	 20564571	scwuaa@connect.ust.hk
 */

package com.example.cater.profile;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Locale;

@Database(entities = {Profile.class}, version = 15, exportSchema = false)
public abstract class ProfileRoomDatabase extends RoomDatabase {
    private static ProfileRoomDatabase INSTANCE;
    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    public static ProfileRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProfileRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProfileRoomDatabase.class, "profile_database")
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

    public abstract ProfileDao profileDao();

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ProfileDao mDao;
        String[] names = {"Jason", "Monica", "Cindy", "David", "Eric", "Frank"};


        PopulateDbAsync(ProfileRoomDatabase db) {
            mDao = db.profileDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // IF need to delete all the database, use the following line.
            //mDao.deleteAll();
            // If we have no profiles, then create the initial list of profiles
            if (mDao.getAnyProfile().length < 1) {
                for (int i = 0; i < names.length; i++) {
                    String description = String.format(Locale.getDefault(),
                            "This is %s. Nice to see you!", names[i]);
                    String photo = String.format(Locale.getDefault(),
                            "default_%d", i + 1);
                    Profile profile = new Profile
                            .Builder(i, "85253002711")
                            .name(names[i])
                            .photo(photo)
                            .position((22.33653 + (Math.random() * 2 - 1) * 0.001)
                                    , (114.26363 + (Math.random() * 2 - 1) * 0.001))
                            .description(description)
                            .builder();
                    mDao.insert(profile);
                }
            }
            return null;
        }
    }
}
