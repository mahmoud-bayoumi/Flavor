package com.example.flavor.presentation.splash;

import android.os.Handler;
import android.os.Looper;

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View view;
    private static final long SPLASH_DURATION = 2500;

    public SplashPresenter(SplashContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (view != null) {
                view.navigateToNextScreen();
            }
        }, SPLASH_DURATION);
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}

