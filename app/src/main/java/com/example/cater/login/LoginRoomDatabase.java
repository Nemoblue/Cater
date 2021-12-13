package com.example.cater.login;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cater.profile.ProfileRoomDatabase;

@Database(entities = {Login.class}, version = 1, exportSchema = false)
public abstract class LoginRoomDatabase extends RoomDatabase {
    public abstract LoginDao loginDao();

    private static LoginRoomDatabase INSTANCE;

    public static LoginRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LoginRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LoginRoomDatabase.class, "login_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final LoginDao mDao;
        PopulateDbAsync(LoginRoomDatabase db) {mDao = db.loginDao();}
        @Override
        protected Void doInBackground(Void... params) {
            //mDao.deleteALL();
            if (mDao.getAnyLogin().length < 1) {
                Login login = new Login(0, "85253002711", "123456");
                mDao.insert(login);
            }
            return null;
        }
    }
}
