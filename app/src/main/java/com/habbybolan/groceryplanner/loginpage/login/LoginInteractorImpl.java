package com.habbybolan.groceryplanner.loginpage.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.primarymodels.User;
import com.habbybolan.groceryplanner.models.webmodels.Login;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInteractorImpl implements LoginContract.LoginInteractor {

    private RestWebService restWebService;
    private Context context;
    private User user;

    @Inject
    public LoginInteractorImpl(RestWebService restWebService, User user, Context context) {
        this.restWebService = restWebService;
        this.context = context;
        this.user = user;
    }

    @Override
    public void login(String username, String password, LoginContract.LoginWebServiceCallback callback) {
        if (username.isEmpty() || password.isEmpty()) {
            callback.onFailure(500, "empty username or password");
            return;
        }
        Call<User> call = restWebService.login(new Login(username, password));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // if successful login, then save token and signal login successful
                if (response.isSuccessful()) {
                    User userResponse = response.body();
                    // save the token in preferences
                    saveTokenToPreferences(userResponse.getToken());
                    // save the retrieved user into the globally available User object
                    user.copyUser(userResponse);
                    callback.onResponse();
                    // otherwise, signal failed login
                } else {
                    callback.onFailure(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onFailure(404, "");
            }
        });
    }

    @Override
    public void saveTokenToPreferences(String token) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.sharedPref_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getResources().getString(R.string.saved_token), token);
        editor.apply();
    }
}
