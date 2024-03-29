package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.models.primarymodels.User;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module for when the User signs into the app, creating a globally available user object.
 */
@Module
public class UserModule {

    private User user;

    public UserModule() {
        user = new User();
    }

    @Singleton
    @Provides
    User provideUser() {
        return user;
    }

}
