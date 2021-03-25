package com.habbybolan.groceryplanner.loginpage.signup;

import androidx.databinding.ObservableField;

import org.json.JSONObject;

public interface SignUpInteractor {

    /**
     * Sends the information the database to complete the sign-up. Checks if the username, password, and email are all valid.
     * @param username              Username of the new user
     * @param password              Password of the new user
     * @param email                 Email of the new user
     * @param signUpResponse        Sets value to the response from the web service after trying to sign up
     */
    void signUp(String username, String password, String email, ObservableField<JSONObject> signUpResponse);


    /**
     * Saves the web service JWT token to SharedPreferences
     * @param token     JWT String token to save
     */
    void saveTokenToPreferences(String token);
}
