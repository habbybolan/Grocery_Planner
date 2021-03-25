package com.habbybolan.groceryplanner.loginpage.login;

import androidx.databinding.ObservableField;

import org.json.JSONObject;

public interface LoginInteractor {

    /**
     * Send the information to the web service to attempt to login.
     * @param username              Username of the new user
     * @param password              Password of the new user
     * @param loginResponse         set as the JSON response from the web service
     */
    void login(String username, String password, ObservableField<JSONObject> loginResponse);

    /**
     * Saves the web service JWT token to SharedPreferences
     * @param token     JWT String token to save
     */
    void saveTokenToPreferences(String token);
}
