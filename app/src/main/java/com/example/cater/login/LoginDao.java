/*
 * Copyright (c) 2021.   # COMP 4521 #
 * # SHEN, Ye #	 20583137	yshenat@connect.ust.hk
 * # ZHOU, Ji #	 20583761	jzhoubl@connect.ust.hk
 * # WU, Sik Chit #	 20564571	scwuaa@connect.ust.hk
 */

package com.example.cater.login;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface LoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Login login);

    @Query("DELETE FROM login_table")
    void deleteALL();

    @Delete
    void deleteLogin(Login login);

    @Query("SELECT * FROM login_table WHERE phone = :phone AND password = :password LIMIT 1")
    LiveData<Login> getLogin(String phone, String password);

    @Query("SELECT * FROM login_table LIMIT 1")
    Login[] getAnyLogin();
}
