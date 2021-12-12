package com.example.cater.appointment;

import android.text.format.Time;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cater.profile.Profile;

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

    @ColumnInfo(name = "user_name")
    protected String user_name;
    @ColumnInfo(name = "user_photo")
    protected String user_photo;
    @NonNull
    @ColumnInfo(name = "appoint_date")
    protected String appoint_date;
    @NonNull
    @ColumnInfo(name = "target_date")
    protected String target_date;

    public Appointment () {
        this.appoint_id = 0;
        user_name = "";
        user_photo = "";
        appoint_date = "";
        target_date = "";
    }

    public static class Builder {
        private int aid;
        private int cid;
        private int uid;
        private String uName = null;
        private String uPhoto = null;
        private String appoint_Date;
        private String target_date;

        public Builder (int aid, int cid, int uid,
                        @NonNull Date appoint_Date, @NonNull Date target_date) {
            this.aid = aid;
            this.cid = cid;
            this.uid = uid;
            this.appoint_Date = appoint_Date.toString();
            this.target_date = target_date.toString();
        }

        public Builder name(String name) {
            uName = name;
            return this;
        }

        public Builder photo(String photo) {
            uPhoto = photo;
            return this;
        }

        public Appointment builder() { return new Appointment(this); }
    }

    public Appointment(Builder builder) {
        this.appoint_id = builder.aid;
        this.canteen_id = builder.cid;
        this.user_id = builder.uid;
        this.user_name = builder.uName;
        this.user_photo = builder.uPhoto;
        this.appoint_date = builder.appoint_Date;
        this.target_date = builder.target_date;
    }


    public int getAppoint_id() {return this.appoint_id;}
    public int getCanteen_id() {return this.canteen_id;}
    public int getUser_id() {return this.user_id;}
    public String getUser_name() {return this.user_name;}
    public String getUser_photo() {return this.user_photo;}
    @NonNull
    public Date getAppoint_date() {return new Date(this.appoint_date);}
    @NonNull
    public Date getTarget_date() {return new Date(this.target_date);}
}
