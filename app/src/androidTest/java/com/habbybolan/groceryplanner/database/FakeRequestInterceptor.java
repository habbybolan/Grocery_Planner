package com.habbybolan.groceryplanner.database;

import android.content.Context;

import java.io.IOException;
import java.net.URI;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class FakeRequestInterceptor implements Interceptor  {

    private Context application;

    @Inject
    public FakeRequestInterceptor(Context application) {
        this.application = application;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // produce response to satisfy request
        Request request = chain.request();
        final URI uri = request.url().uri();
        final String query = uri.getPath();
        return null;
    }

}
