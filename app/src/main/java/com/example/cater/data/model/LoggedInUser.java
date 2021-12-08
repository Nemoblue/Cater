package com.example.cater.data.model;

import static java.lang.Integer.parseInt;

import com.example.cater.profile.Profile;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private Profile profile;

    public LoggedInUser(String userId, String displayName, String password) {
        this.userId = userId;
        this.displayName = displayName;
        this.profile = new Profile.Builder(parseInt(userId), displayName).password(password).builder();
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}