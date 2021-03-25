package com.habbybolan.groceryplanner.http.requests;

import androidx.databinding.ObservableField;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public interface HttpLoginSignUp {

    /**
     * Sends the user information to the web service to add to database.
     * @param username              Username input by user
     * @param password              Password input by user
     * @param email                 Email input by user
     * @param signUpResponse        Sets value to the response from the web service after trying to sign up
     */
    void signUpUser(String username, String password, String email, ObservableField<JSONObject> signUpResponse) throws ExecutionException, InterruptedException;

    /**
     * Sends the user information to the web service to try and login.
     * @param username      Username input by user
     * @param password      Password input by user
     * @param loginResponse set as the JSON response from the web service
     */
    void loginUser(String username, String password, ObservableField<JSONObject> loginResponse) throws ExecutionException, InterruptedException;
}
