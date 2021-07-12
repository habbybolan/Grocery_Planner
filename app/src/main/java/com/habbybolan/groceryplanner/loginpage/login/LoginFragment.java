package com.habbybolan.groceryplanner.loginpage.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentLoginBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.LoginModule;
import com.habbybolan.groceryplanner.di.module.SignUpModule;

import javax.inject.Inject;

public class LoginFragment extends Fragment implements LoginContract.LoginView {

    private FragmentLoginBinding binding;
    private LoginListener loginListener;

    @Inject
    LoginContract.LoginPresenter presenter;

    public LoginFragment() {}

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        // todo:
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((GroceryApp) getActivity().getApplication()).getAppComponent().loginSubComponent(new SignUpModule(), new LoginModule()).inject(this);
        presenter.setView(this);
        if (getArguments() != null) {
            // todo:
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginListener = (LoginListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loginListener = null;
        presenter.destroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        setOnClicks();
        return binding.getRoot();
    }

    /**
     * Set up the onClick events.
     */
    private void setOnClicks() {
        // onClick for signing up
        binding.btnSignUp.setOnClickListener(l -> {
            loginListener.gotoSignUpScreen();
        });

        // onClick for logging in
        binding.btnLogin.setOnClickListener(l -> {
            String username = binding.editTxtUsername.getText().toString();
            String password = binding.editTxtPassword.getText().toString();
            presenter.login(username, password);
        });

        // onClick for continuing into offline mode
        binding.btnContOffline.setOnClickListener(l -> {
            loginListener.continueOffline();
        });
    }

    /**
     * Resets the UI when returning to this Fragment
     */
    public void resetUI() {
        // todo:
    }

    @Override
    public void loadingStarted() {
        Toast.makeText(getContext(), "Loading Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccessful() {
        loginListener.loginSuccessful();
    }

    @Override
    public void loginUnSuccessful(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface LoginListener {

        /**
         * Go to the SignUp Fragment.
         */
        void gotoSignUpScreen();

        /**
         * Successful login, goto main screen.
         */
        void loginSuccessful();

        /**
         * Continue in offline mode, going to main screen without a token.
         */
        void continueOffline();
    }
}
