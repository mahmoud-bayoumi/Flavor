package com.example.flavor.presentation.auth.signup;

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View view;

    public SignUpPresenter(SignUpContract.View view) {
        this.view = view;
    }

    @Override
    public void signUp(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            view.onSignUpError("Please fill all fields");
            return;
        }
        view.showLoading();
        // Network logic here...
        view.onSignUpSuccess();
    }
}
