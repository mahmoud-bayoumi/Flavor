package com.example.flavor.presentation.signup;

public interface SignUpContract {
    interface View {
        void showLoading();
        void hideLoading();
        void onSignUpSuccess();
        void onSignUpError(String message);
    }

    interface Presenter {
        void signUp(String name, String email, String password);
    }
}
