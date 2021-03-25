package com.habbybolan.groceryplanner.di.component;

import com.habbybolan.groceryplanner.di.module.LoginModule;
import com.habbybolan.groceryplanner.di.module.SignUpModule;
import com.habbybolan.groceryplanner.di.scope.LoginScope;
import com.habbybolan.groceryplanner.loginpage.login.LoginFragment;
import com.habbybolan.groceryplanner.loginpage.signup.SignUpFragment;

import dagger.Subcomponent;

@LoginScope
@Subcomponent(modules = {SignUpModule.class, LoginModule.class})
public interface LoginSubComponent {

    void inject(SignUpFragment fragment);
    void inject(LoginFragment fragment);
}
