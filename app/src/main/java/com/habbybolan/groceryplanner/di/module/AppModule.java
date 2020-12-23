package com.habbybolan.groceryplanner.di.module;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context context;

    public AppModule(Application application) {
        context = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }
}
