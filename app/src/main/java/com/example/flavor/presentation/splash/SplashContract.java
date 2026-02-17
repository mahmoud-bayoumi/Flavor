package com.example.flavor.presentation.splash;

public interface SplashContract {

    interface View {
        void navigateToNextScreen();
    }

    interface Presenter {
        void start();
        void onDestroy();
    }
}

