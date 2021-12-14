package com.example.cater.appointment;

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

    @ColumnInfo(name = "user_name")
    protected String user_name;
    @ColumnInfo(name = "user_photo")
    protected String user_photo;
    @ColumnInfo(name = "user_phone")
    protected String user_phone;
    @NonNull
    @ColumnInfo(name = "appoint_date")
    protected String appoint_date;
    @NonNull
    @ColumnInfo(name = "target_time")
    protected String target_time;

    public Appointment () {
        this.appoint_id = 0;
        user_name = "";
        user_photo = "";
        user_phone = "";
        appoint_date = "";
        target_time = "";
    }

    public static class Builder {
        private final int aid;
        private final int cid;
        private final int uid;
        private String uName = null;
        private String uPhoto = null;
        private String uPhone = null;
        private final String appoint_Date;
        private final String target_time;

        public Builder (int aid, int cid, int uid,
                        @NonNull Date appoint_Date, @NonNull String target_time) {
            this.aid = aid;
            this.cid = cid;
            this.uid = uid;
            this.appoint_Date = appoint_Date.toString();
            this.target_time = target_time;
        }

        public Builder name(String name) {
            uName = name;
            return this;
        }

        public Builder photo(String photo) {
            uPhoto = photo;
            return this;
        }

        public Builder phone(String phone) {
            uPhone = phone;
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
        this.user_phone = builder.uPhone;
        this.appoint_date = builder.appoint_Date;
        this.target_time = builder.target_time;
    }


    public int getAppoint_id() {return this.appoint_id;}
    public int getCanteen_id() {return this.canteen_id;}
    public int getUser_id() {return this.user_id;}
    public String getUser_name() {return this.user_name;}
    public String getUser_photo() {return this.user_photo;}
    public String getUser_phone() {return this.user_phone;}
    @NonNull
    public Date getAppoint_date() {return new Date(this.appoint_date);}
    @NonNull
    public String getTarget_date() {return this.target_time;}
}
