package com.habbybolan.groceryplanner.loginpage.login;

import javax.inject.Inject;

public class LoginPresenterImpl implements LoginContract.LoginPresenter {

    private LoginContract.LoginInteractor interactor;
    private LoginContract.LoginView view;
    private LoginContract.LoginWebServiceCallback loginResponse = new LoginContract.LoginWebServiceCallback() {
        @Override
        public void onResponse() {
            view.loginSuccessful();
        }

        @Override
        public void onFailure(int responseCode, String message) {
            view.loginUnSuccessful(responseCode + ": " + message);
        }
    };

    @Inject
    public LoginPresenterImpl(LoginContract.LoginInteractor interactor) {
        this.interactor = interactor;
    }


    @Override
    public void setView(LoginContract.LoginView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void login(String username, String password) {
        interactor.login(username, password, loginResponse);
    }
}
