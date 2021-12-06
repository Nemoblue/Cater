package com.example.cater.ui.home;

import java.io.Serializable;

/**
 * create by liubit on 2021/12/4
 */
public class RestaurantBean implements Serializable {
    String name;
    int resId;
    String distance;

    public RestaurantBean(String name, int resId, String distance) {
        this.name = name;
        this.resId = resId;
        this.distance = distance;
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
}
