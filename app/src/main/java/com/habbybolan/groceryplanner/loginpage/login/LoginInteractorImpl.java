package com.habbybolan.groceryplanner.loginpage.login;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.http.requests.HttpLoginSignUp;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class LoginInteractorImpl implements LoginInteractor {

    private HttpLoginSignUp httpRequest;
    private Context context;

    @Inject
    public LoginInteractorImpl(HttpLoginSignUp httpRequest, Context context) {
        this.httpRequest = httpRequest;
        this.context = context;
    }

    @Override
    public void login(String username, String password, ObservableField<JSONObject> loginResponse) {
        try {
            httpRequest.loginUser(username, password, loginResponse);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTokenToPreferences(String token) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.sharedPref_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getResources().getString(R.string.saved_token), token);
        editor.apply();
    }
}
