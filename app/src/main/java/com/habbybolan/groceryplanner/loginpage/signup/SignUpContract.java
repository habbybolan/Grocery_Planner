package com.habbybolan.groceryplanner.loginpage.signup;

public interface SignUpContract {

    interface SignUpPresenter {

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

    interface SignUpInteractor {

        /**
         * Sends the information the database to complete the sign-up. Checks if the username, password, and email are all valid.
         * @param username              Username of the new user
         * @param password              Password of the new user
         * @param email                 Email of the new user
         * @param callback              Callback to notify if signUp was successful or not
         */
        void signUp(String username, String password, String email, SignUpWebServiceCallback callback);

    }

    interface SignUpView {

        void loadingStarted();
        void loadingFailed(String message);

        void signUpSuccessful();
        void signUpUnSuccessful(String messages);
    }

    interface SignUpWebServiceCallback {
        void onResponse();
        void onFailure(int responseCode, String message);
    }
}
