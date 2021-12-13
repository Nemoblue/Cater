package com.example.cater.login;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "login_table")
public class Login {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "phone")
    private final String phone;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "uid")
    private final int uid;

    public Login(int uid, @NonNull String phone, @NonNull String password) {
        this.uid = uid;
        this.phone = phone;
        this.password = password;
    }

    @NonNull
    public String getPhone() {return this.phone;}
    @NonNull
    public String getPassword() {return this.password;}
    public int getUid() {return this.uid;}
}
