package com.habbybolan.groceryplanner.loginpage.signup;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class SignUpPresenterImpl implements SignUpPresenter {

    private SignUpView view;
    private SignUpInteractor interactor;
    private ObservableField<JSONObject> signUpResponse = new ObservableField<>();

    @Inject
    public SignUpPresenterImpl(SignUpInteractor interactor) {
        this.interactor = interactor;
        setObserver();
    }

    /**
     * Sets up the observer callback on value change.
     */
    private void setObserver() {
        signUpResponse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                signUpResponse();
            }
        });
    }

    /**
     * Deals with the response from the web service for signing up
     */
    private void signUpResponse() {
        JSONObject response = signUpResponse.get();
        try {
            if (response.has("code")) {
                switch (response.getInt("code")) {
                    case 200:
                        interactor.saveTokenToPreferences(response.getString("token"));
                        view.signUpSuccessful();
                        break;
                    case 401:
                        view.signUpUnSuccessful("Invalid username, email, or password");
                        break;
                    default:
                        view.signUpUnSuccessful("Something went wrong");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            view.signUpUnSuccessful("Something went wrong");
        }
    }

    @Override
    public void setView(SignUpView view) {
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
