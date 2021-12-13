package com.example.cater.profile;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "profile_table")
public class Profile implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    private final int uid;
    @NonNull
    @ColumnInfo (name = "phone")
    protected String uPhone;

    @ColumnInfo(name = "name")
    protected String uName;
    @ColumnInfo(name = "age")
    protected int uAge;
    @ColumnInfo(name = "photo")
    protected String uPhoto;
    @ColumnInfo(name = "description")
    protected String uDescription;
    @ColumnInfo(name = "latitude")
    protected double uLatitude;
    @ColumnInfo(name = "longitude")
    protected double uLongitude;
    @ColumnInfo(name = "active")
    protected boolean uActive;

    public Profile(int uid) { this.uid = uid;
        uPhone = "Default Phone Number";
    }

    public static class Builder {
        private final int uid;
        private final String uPhone;
        private String uName = null;
        private int uAge;
        private String uPhoto = null;
        private String uDescription = null;
        private final double[] uPosition = {22.33653,114.26363};
        private boolean uActive = true;

        public Builder(int uid, @NonNull String phone) {
            this.uid = uid;
            this.uPhone = phone;
        }

        public Builder name(String name) {
            uName = name;
            return this;
        }

        public Builder age(int age) {
            uAge = age;
            return this;
        }

        public Builder photo(String photo) {
            uPhoto = photo;
            return this;
        }

        public Builder description(String description) {
            uDescription = description;
            return this;
        }

        public Builder position(double latitude, double longitude) {
            uPosition[0] = latitude;
            uPosition[1] = longitude;
            return this;
        }

        public Builder active(boolean active) {
            uActive = active;
            return this;
        }

        public Profile builder() {
            return new Profile(this);
        }
    }

    public Profile(Builder builder) {
        this.uid = builder.uid;
        this.uPhone = builder.uPhone;
        if (builder.uName == null)
            this.uName = "+" + builder.uPhone;
        else
            this.uName = builder.uName;
        this.uAge = builder.uAge;
        this.uPhoto = builder.uPhoto;
        this.uDescription = builder.uDescription;
        this.uLatitude = builder.uPosition[0];
        this.uLongitude = builder.uPosition[1];
        this.uActive = builder.uActive;
    }

    public int getUid() {return this.uid;}
    public String getuName() {return this.uName;}
    public int getAge() {return this.uAge;}
    public String getPhoto() {return this.uPhoto;}
    public String getDescription() {return this.uDescription;}
    public double[] getPosition() { return new double[]{this.uLatitude, this.uLongitude};}
    public boolean isuActive() {return this.uActive;}
    @NonNull
    public String getuPhone() {return this.uPhone;}

    public void setLocation(double latitude, double longitude) {
        this.uLatitude = latitude;
        this.uLongitude = longitude;
    }
    public void setActive(boolean state) {
        this.uActive = state;
    }
}
