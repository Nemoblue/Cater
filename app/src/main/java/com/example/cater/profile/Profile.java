package com.example.cater.profile;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "profile_table")
public class Profile implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uid")
    private int uid;

    @ColumnInfo(name = "name")
    protected String uName;
    @ColumnInfo(name = "age")
    protected int uAge;
    @ColumnInfo(name = "photo")
    protected String uPhoto;
    @ColumnInfo(name = "description")
    protected String uDescription;
    @ColumnInfo(name = "tag")
    protected String uTag;
    @ColumnInfo(name = "latitude")
    protected double uLatitude;
    @ColumnInfo(name = "longitude")
    protected double uLongitude;
    @ColumnInfo(name = "active")
    protected boolean uActive;
    //to be upgraded
    @ColumnInfo(name = "password")
    protected String uPassword;

    public Profile(@NonNull int uid) { this.uid = uid; }

    public static class Builder {
        private int uid;
        private String uName;
        private int uAge;
        private String uPhoto = null;
        private String uDescription = null;
        private String uTag = null;
        private double[] uPosition = {-1, -1};
        private boolean uActive = true;
        private String uPassword = "aaa"; //todo please remember to modify the constructor

        public Builder(@NonNull int uid, @NonNull String name) {
            this.uid = uid;
            this.uName = name;
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

        public Builder tag(String tag) {
            uTag = tag;
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
        public Builder password(String password){
            uPassword = password;
            return this;
        }

        public Profile builder() {
            return new Profile(this);
        }
    }

    public Profile(Builder builder) {
        this.uid = builder.uid;
        this.uName = builder.uName;
        this.uAge = builder.uAge;
        this.uPhoto = builder.uPhoto;
        this.uDescription = builder.uDescription;
        this.uTag = builder.uTag;
        this.uLatitude = builder.uPosition[0];
        this.uLongitude = builder.uPosition[1];
        this.uActive = builder.uActive;
        this.uPassword = builder.uPassword;
    }

    public int getUid() {return this.uid;}
    public String getuName() {return this.uName;}
    public int getAge() {return this.uAge;}
    public String getPhoto() {return this.uPhoto;}
    public String getDescription() {return this.uDescription;}
    public String getTag() {return this.uTag;}
    public double[] getPosition() { return new double[]{this.uLatitude, this.uLongitude};}
    public boolean isuActive() {return this.uActive;}
    public String getPassword() {return this.uPassword;}
}
