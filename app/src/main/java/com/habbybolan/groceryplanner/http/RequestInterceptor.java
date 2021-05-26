package com.habbybolan.groceryplanner.http;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    @Inject
    public RequestInterceptor() {}

    @Override
    public Response intercept(Chain chain) throws IOException {
        // produce response to satisfy request
        Request request = chain.request();

        // add authentication to each Http request
        Request addAuthenticationRequest = request.newBuilder()
                // todo: add authentication
                .build();

        return chain.proceed(addAuthenticationRequest);
    }
}
