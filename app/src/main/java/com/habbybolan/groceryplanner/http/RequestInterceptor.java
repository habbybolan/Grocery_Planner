package com.habbybolan.groceryplanner.http;

import android.content.Context;
import android.content.SharedPreferences;

import com.habbybolan.groceryplanner.R;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Intercepter that adds extra data to each http request.
 */
public class RequestInterceptor implements Interceptor {

    private Context application;

    @Inject
    public RequestInterceptor(Context application) {
        this.application = application;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // produce response to satisfy request
        Request request = chain.request();

        SharedPreferences sharedPref = application.getSharedPreferences(application.getResources().getString(R.string.sharedPref_name), Context.MODE_PRIVATE);
        String token = sharedPref.getString(application.getResources().getString(R.string.saved_token), "");

        // Adds token authentication to every http request
        Request addAuthenticationRequest = request.newBuilder()
                .addHeader("Authorization", token.equals("") ? "" : "bearer " + token)
                .build();

        return chain.proceed(addAuthenticationRequest);
    }
}
