package com.example.cater.ui.home;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * create by liubit on 2021/12/4
 */
public class RestaurantBean implements Serializable {
    String name;
    int resId;
    String distance;
    double[] position;

    public RestaurantBean(String name, int resId, String distance, double latitude, double longitude) {
        this.name = name;
        this.resId = resId;
        this.distance = distance;
        this.position = new double[]{latitude, longitude};
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public double[] getLatLng() {
        return position;
    }
}
