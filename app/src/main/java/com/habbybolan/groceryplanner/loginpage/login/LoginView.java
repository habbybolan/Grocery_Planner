package com.habbybolan.groceryplanner.loginpage.login;

public interface LoginView {

    void loadingStarted();
    void loadingFailed(String message);

    void loginSuccessful();
    void loginUnSuccessful(String message);
}
