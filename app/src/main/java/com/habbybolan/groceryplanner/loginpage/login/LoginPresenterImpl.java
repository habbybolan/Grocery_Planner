package com.habbybolan.groceryplanner.loginpage.login;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class LoginPresenterImpl implements LoginPresenter {

    private LoginInteractor interactor;
    private LoginView view;
    private ObservableField<JSONObject> loginResponse = new ObservableField<>();

    @Inject
    public LoginPresenterImpl(LoginInteractor interactor) {
        this.interactor = interactor;
        setLoginCallback();
    }

    /**
     * Sets up the observable callback for the web service response from logging in.
     */
    private void setLoginCallback() {
        loginResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                loginResponse();
            }
        });
    }

    /**
     * Check if the login was successful and send back to view.
     */
    private void loginResponse() {
        JSONObject response = loginResponse.get();
        try {
            if (response.has("code")) {
                switch (response.getInt("code")) {
                    case 200:
                        interactor.saveTokenToPreferences(response.getString("token"));
                        view.loginSuccessful();
                        break;
                    case 401:
                        view.loginUnSuccessful("Incorrect login credentials");
                        break;
                    default:
                        view.loginUnSuccessful("Something went wrong");
                        break;
                }
            }
        } catch (JSONException e) {
            view.loadingFailed("Something went wrong");
        }
    }


    @Override
    public void setView(LoginView view) {
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
