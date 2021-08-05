package com.habbybolan.groceryplanner.models.primarymodels;

import android.content.Context;

/**
 * A user, created on login for global use.
 * Stores necessary information to identify the user and communicate with API.
 * An default User object with isLoggedOn = false is created if user continues through offline mode.
 */
public class User {

    // sets if the user is logged on
    private boolean isLoggedOn;
    private long onlineId;
    private String username;
    private String email;
    private Context context;

    public User(Context context) {
        this.context = context;
        isLoggedOn = false;
    }

    public void storeLoggedInUserInfo(long onlineId, String username, String email) {
        this.onlineId = onlineId;
        this.username = username;
        this.email = email;
    }

    public long getId() {
        return onlineId;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }

    public boolean isUserLoggedOn() {
        return isLoggedOn;
    }
}
