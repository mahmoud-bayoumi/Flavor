package com.example.flavor.presentation.auth.login;

public interface LoginContract {
    interface View {

        void showLoading();
        void hideLoading();
        void onLoginSuccess();
        void onLoginError(String message);
    }

    interface Presenter {
        void login(String email, String password);
        void loginWithGoogle();
        void loginWithApple();
    }
}
