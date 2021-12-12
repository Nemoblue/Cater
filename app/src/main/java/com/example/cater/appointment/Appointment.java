package com.example.cater.appointment;

import android.text.format.Time;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "appointment_table")
public class Appointment implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "appoint_id")
    protected int appoint_id;
    @ColumnInfo(name = "canteen_id")
    protected int canteen_id;
    @ColumnInfo(name = "user_id")
    protected int user_id;

    @NonNull
    @ColumnInfo(name = "appoint_date")
    protected String appoint_date;
    @NonNull
    @ColumnInfo(name = "target_date")
    protected String target_date;

    public Appointment () {
        this.appoint_id = 0;
        appoint_date = null;
        target_date = null;
    }
    public Appointment(int aid, int cid, int uid,
                       @NonNull Date appoint_date,
                       @NonNull Date target_date) {
        this.appoint_id = aid;
        this.canteen_id = cid;
        this.user_id = uid;
        this.appoint_date = appoint_date.toString();
        this.target_date = target_date.toString();
    }

    public int getAppoint_id() {return this.appoint_id;}
    public int getCanteen_id() {return this.canteen_id;}
    public int getUser_id() {return this.user_id;}
    @NonNull
    public Date getAppoint_date() {return new Date(this.appoint_date);}
    @NonNull
    public Date getTarget_date() {return new Date(this.target_date);}
}
