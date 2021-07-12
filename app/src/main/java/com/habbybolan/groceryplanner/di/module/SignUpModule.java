package com.habbybolan.groceryplanner.di.module;

import android.content.Context;

import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.loginpage.signup.SignUpContract;
import com.habbybolan.groceryplanner.loginpage.signup.SignUpInteractorImpl;
import com.habbybolan.groceryplanner.loginpage.signup.SignUpPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class SignUpModule {

    @Provides
    SignUpContract.SignUpPresenter provideSignUpPresenter(SignUpContract.SignUpInteractor signUpInteractor) {
        return new SignUpPresenterImpl(signUpInteractor);
    }

    @Provides
    SignUpContract.SignUpInteractor provideSignUpInteractor(RestWebService restWebService, Context context) {
        return new SignUpInteractorImpl(restWebService, context);
    }
}
