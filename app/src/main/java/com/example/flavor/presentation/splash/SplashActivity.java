package com.example.flavor.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.flavor.R;
import com.example.flavor.presentation.auth.login.LoginActivity;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    private SplashPresenter presenter;
    private LottieAnimationView lottieView;
    private TextView appName, subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
        startTextAnimation();

        presenter = new SplashPresenter(this);
        presenter.start();
    }

    private void initViews() {
        lottieView = findViewById(R.id.lottieView);
        appName = findViewById(R.id.appName);
        subTitle = findViewById(R.id.subTitle);

        lottieView.playAnimation();
    }

    private void startTextAnimation() {
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_from_left);

        new Handler().postDelayed(() -> {
            appName.startAnimation(slideIn);
            subTitle.startAnimation(slideIn);

            appName.setTranslationX(0);
            subTitle.setTranslationX(0);
        }, 600); // delay after Lottie starts
    }

    @Override
    public void navigateToNextScreen() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
