package com.habbybolan.groceryplanner.loginpage.signup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.FragmentSignUpBinding;
import com.habbybolan.groceryplanner.di.GroceryApp;
import com.habbybolan.groceryplanner.di.module.LoginModule;
import com.habbybolan.groceryplanner.di.module.SignUpModule;

import javax.inject.Inject;

public class SignUpFragment extends Fragment implements SignUpView{

    @Inject
    SignUpPresenter presenter;
    private FragmentSignUpBinding binding;
    private SignUpListener listener;

    public SignUpFragment() {}

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // attach listener implemented by parent Activity
        listener = (SignUpListener) context;
    }

    @Override
    public void onDetach() {
        listener = null;
        presenter.destroy();
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        setOnClicks();
        return binding.getRoot();
    }

    /**
     * Sets up the OnClick events.
     */
    private void setOnClicks() {
        // button click for signing in with the current
        binding.btnSignUp.setOnClickListener(l -> {
            // take user input for sign in
            String username = binding.editTxtUsername.getText().toString();
            String password = binding.editTxtPassword.getText().toString();
            String email = binding.editTxtEmail.getText().toString();
            // use the user input to create new account if valid user input
            presenter.signUp(username, password, email);
        });
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
    public void signUpSuccessful() {
        listener.gotoLoginScreen();
    }

    @Override
    public void signUpUnSuccessful(String message) {
        // todo: Sign up unsuccessful, display failure to sign up
    }

    /**
     * Resets the UI when returning.
     */
    public void resetUI() {
        // todo:
    }

    /**
     * Listener implemented in the parent Activity to communicate with it
     */
    public interface SignUpListener {

        /**
         * Return to the login screen
         */
        void gotoLoginScreen();

        /**
         * Go to the main screen as signing up was successful.
         */
        void signUpSuccessful();
    }
}
