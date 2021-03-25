package com.habbybolan.groceryplanner.di.module;

import android.content.Context;

import com.habbybolan.groceryplanner.http.requests.HttpLoginSignUp;
import com.habbybolan.groceryplanner.loginpage.login.LoginInteractor;
import com.habbybolan.groceryplanner.loginpage.login.LoginInteractorImpl;
import com.habbybolan.groceryplanner.loginpage.login.LoginPresenter;
import com.habbybolan.groceryplanner.loginpage.login.LoginPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    LoginPresenter provideLoginPresenter(LoginInteractor loginInteractor) {
        return new LoginPresenterImpl(loginInteractor);
    }

    @Provides
    LoginInteractor provideLoginInteractor(HttpLoginSignUp httpRequest, Context context) {
        return new LoginInteractorImpl(httpRequest, context);
    }

}
