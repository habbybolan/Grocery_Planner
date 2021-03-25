package com.habbybolan.groceryplanner.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.habbybolan.groceryplanner.MainPage.MainActivity;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.ActivityLoginBinding;
import com.habbybolan.groceryplanner.loginpage.login.LoginFragment;
import com.habbybolan.groceryplanner.loginpage.signup.SignUpFragment;

public class LoginActivity extends AppCompatActivity
                                    implements SignUpFragment.SignUpListener,
                                                LoginFragment.LoginListener {

    ActivityLoginBinding binding;
    private String SIGN_UP_TAG;
    private String LOGIN_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        SIGN_UP_TAG = getResources().getString(R.string.SIGN_UP_TAG);
        LOGIN_TAG = getResources().getString(R.string.LOGIN_TAG);
    }

    @Override
    public void gotoLoginScreen() {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(LOGIN_TAG);
        if (loginFragment != null) loginFragment.resetUI();
        setLoginScreenVisibility(View.VISIBLE);
    }



    @Override
    public void gotoSignUpScreen() {
        SignUpFragment signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentByTag(SIGN_UP_TAG);
        if (signUpFragment != null) signUpFragment.resetUI();
        setLoginScreenVisibility(View.GONE);
    }

    /**
     * Sets the visibility of the login screen to visibility, and the sign up screen to the opposite.
     * @param visibility    The visibility of the login screen
     */
    private void setLoginScreenVisibility(int visibility) {
        binding.fragmentLoginContainer.setVisibility(visibility);
        binding.fragmentSignUpContainer.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    @Override
    public void signUpSuccessful() {
        gotoMainScreen();
    }

    @Override
    public void loginSuccessful() {
        gotoMainScreen();
    }

    @Override
    public void continueOffline() {
        gotoMainScreen();
    }

    /**
     * Go to the main screen.
     */
    private void gotoMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (binding.fragmentSignUpContainer.getVisibility() == View.VISIBLE) {
            setLoginScreenVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}
