package com.habbybolan.groceryplanner.loginpage.signup;

public interface SignUpPresenter {

    /**
     * Attach the Fragment's view to the presenter.
     * @param view  Implemented methods in Fragment to communicate with it.
     */
    void setView(SignUpView view);

    /**
     * Destroy the presenter and its attached view.
     */
    void destroy();

    /**
     * Use the user input to sign up using the Username, Password, and Email. If invalid user input, notify the view.
     * @param username              Username of the new user
     * @param password              Password of the new user
     * @param email                 Email of the new user
     */
    void signUp(String username, String password, String email);
}
