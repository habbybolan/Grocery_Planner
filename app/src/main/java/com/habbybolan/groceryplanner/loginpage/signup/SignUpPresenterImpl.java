package com.habbybolan.groceryplanner.loginpage.signup;

import javax.inject.Inject;

public class SignUpPresenterImpl implements SignUpContract.SignUpPresenter {

    private SignUpContract.SignUpView view;
    private SignUpContract.SignUpInteractor interactor;
    private SignUpContract.SignUpWebServiceCallback signUpResponse = new SignUpContract.SignUpWebServiceCallback() {
        @Override
        public void onResponse() {
            view.signUpSuccessful();
        }

        @Override
        public void onFailure(int responseCode, String message) {
            view.signUpUnSuccessful(responseCode + ": " + message);
        }
    };

    @Inject
    public SignUpPresenterImpl(SignUpContract.SignUpInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(SignUpContract.SignUpView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void signUp(String username, String password, String email) {
        interactor.signUp(username, password, email, signUpResponse);
    }
}
