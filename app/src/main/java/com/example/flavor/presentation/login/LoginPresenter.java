package com.example.flavor.presentation.login;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            view.onLoginError("Fields cannot be empty");
            return;
        }
        view.showLoading();
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            view.hideLoading();
            view.onLoginSuccess();
        }, 2000);
    }

    @Override public void loginWithGoogle() { /* Google logic */ }
    @Override public void loginWithApple() { /* Apple logic */ }
}
