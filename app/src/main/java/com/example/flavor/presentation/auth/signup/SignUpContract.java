package com.example.flavor.presentation.auth.signup;


public interface SignUpContract {

    interface View {
        void showLoading();
        void hideLoading();
        void onSignUpSuccess();
        void onSignUpError(String message);
    }

    interface Presenter {
        void signUp(String name, String email, String password);
        void signUpWithGoogle(String idToken);

        void detach();
    }
}
