package com.habbybolan.groceryplanner.di.module;

import android.content.Context;

import com.habbybolan.groceryplanner.http.requests.HttpLoginSignUp;
import com.habbybolan.groceryplanner.loginpage.signup.SignUpInteractor;
import com.habbybolan.groceryplanner.loginpage.signup.SignUpInteractorImpl;
import com.habbybolan.groceryplanner.loginpage.signup.SignUpPresenter;
import com.habbybolan.groceryplanner.loginpage.signup.SignUpPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class SignUpModule {

    @Provides
    SignUpPresenter provideSignUpPresenter(SignUpInteractor signUpInteractor) {
        return new SignUpPresenterImpl(signUpInteractor);
    }

    @Provides
    SignUpInteractor provideSignUpInteractor(HttpLoginSignUp httpRequest, Context context) {
        return new SignUpInteractorImpl(httpRequest, context);
    }
}
