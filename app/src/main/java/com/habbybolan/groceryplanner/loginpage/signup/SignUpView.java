package com.habbybolan.groceryplanner.loginpage.signup;

public interface SignUpView {

    void loadingStarted();
    void loadingFailed(String message);

    void signUpSuccessful();
    void signUpUnSuccessful(String messages);
}
