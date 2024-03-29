package com.habbybolan.groceryplanner.di.module;

import android.content.Context;

import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.loginpage.login.LoginContract;
import com.habbybolan.groceryplanner.loginpage.login.LoginInteractorImpl;
import com.habbybolan.groceryplanner.loginpage.login.LoginPresenterImpl;
import com.habbybolan.groceryplanner.models.primarymodels.User;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    LoginContract.LoginPresenter provideLoginPresenter(LoginContract.LoginInteractor loginInteractor) {
        return new LoginPresenterImpl(loginInteractor);
    }

    @Provides
    LoginContract.LoginInteractor provideLoginInteractor(RestWebService restWebService, User user, Context context) {
        return new LoginInteractorImpl(restWebService, user, context);
    }

}
