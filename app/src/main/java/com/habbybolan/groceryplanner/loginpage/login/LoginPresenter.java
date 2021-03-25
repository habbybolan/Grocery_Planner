package com.habbybolan.groceryplanner.loginpage.login;

public interface LoginPresenter {

    /**
     * Attach the Fragment's view to the presenter.
     * @param view  Implemented methods in Fragment to communicate with it.
     */
    void setView(LoginView view);

    /**
     * Destroy the presenter and its attached view.
     */
    void destroy();

    /**
     * Use user input to login with the username and password.
     * @param username              Username of the new user
     * @param password              Password of the new user
     */
    void login(String username, String password);
}
