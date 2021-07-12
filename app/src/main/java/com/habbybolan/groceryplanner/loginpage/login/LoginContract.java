package com.habbybolan.groceryplanner.loginpage.login;

public interface LoginContract {

    interface LoginInteractor {

        /**
         * Send the information to the web service to attempt to login.
         * @param username              Username of the new user
         * @param password              Password of the new user
         * @param callback              Callback to signal if login was successful or not
         */
        void login(String username, String password, LoginWebServiceCallback callback);

        /**
         * Saves the web service JWT token to SharedPreferences
         * @param token     JWT String token to save
         */
        void saveTokenToPreferences(String token);
    }

    interface LoginPresenter {

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

    interface LoginView {

        void loadingStarted();
        void loadingFailed(String message);

        void loginSuccessful();
        void loginUnSuccessful(String message);
    }

    interface LoginWebServiceCallback {

        void onResponse();
        void onFailure(int responseCode, String message);
    }
}
