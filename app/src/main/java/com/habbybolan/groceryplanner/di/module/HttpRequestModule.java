package com.habbybolan.groceryplanner.di.module;

import android.app.Application;
import android.content.Context;

import com.habbybolan.groceryplanner.http.HttpRequest;
import com.habbybolan.groceryplanner.http.HttpRequestImpl;
import com.habbybolan.groceryplanner.http.requests.HttpLoginSignUp;
import com.habbybolan.groceryplanner.http.requests.HttpLoginSignUpImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HttpRequestModule {
    private Context context;

    public HttpRequestModule(Application context) {
        this.context = context;
    }

    @Singleton
    @Provides
    HttpRequest provideHttpRequest(Context context) {
        return new HttpRequestImpl(context);
    }

    @Singleton
    @Provides
    HttpLoginSignUp provideHttpLoginSignUp(Context context) {
        return new HttpLoginSignUpImpl(context);
    }
}
