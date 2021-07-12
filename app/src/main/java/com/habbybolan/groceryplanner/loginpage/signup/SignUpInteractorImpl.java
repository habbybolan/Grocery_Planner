package com.habbybolan.groceryplanner.loginpage.signup;

import android.content.Context;

import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.webmodels.Signup;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpInteractorImpl implements SignUpContract.SignUpInteractor {

    private RestWebService restWebService;
    private Context context;

    @Inject
    public SignUpInteractorImpl(RestWebService restWebService, Context context) {
        this.restWebService = restWebService;
        this.context = context;
    }

    @Override
    public void signUp(String username, String password, String email, SignUpContract.SignUpWebServiceCallback callback) {
        Call<Void> call = restWebService.signUp(new Signup(email, username, password));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // if successful, send back to callback
                if (response.isSuccessful()) {
                    callback.onResponse();
                    // otherwise, signal failed signup
                } else {
                    callback.onFailure(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(404, "Something went wrong");
            }
        });
    }
}

