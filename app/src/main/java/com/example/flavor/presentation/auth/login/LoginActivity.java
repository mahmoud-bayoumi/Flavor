package com.example.flavor.presentation.auth.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.flavor.R;
import com.example.flavor.presentation.main.activity.MainActivity;
import com.example.flavor.presentation.auth.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private LoginPresenter presenter;
    private EditText etEmail, etPassword;
    private AppCompatButton btnLogin; // Use AppCompatButton for custom backgrounds
    private TextView tvGoToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this , this);

        initViews();

        setupBottomText();

        btnLogin.setOnClickListener(v ->
                presenter.login(etEmail.getText().toString().trim(),
                        etPassword.getText().toString().trim()));
    }

    private void initViews(){
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToSignUp = findViewById(R.id.tvGoToSignUp);

    }
    private void setupBottomText() {
        String fullText = "Don't have an account? Create Account";
        SpannableString spannable = new SpannableString(fullText);

        int start = fullText.indexOf("Create Account");
        int end = start + "Create Account".length();

         int orangeColor = Color.parseColor("#F59331");
        spannable.setSpan(new ForegroundColorSpan(orangeColor),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

         spannable.setSpan(new StyleSpan(Typeface.BOLD),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(orangeColor);
                ds.setUnderlineText(false);
            }
        };

        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvGoToSignUp.setText(spannable);
        tvGoToSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        tvGoToSignUp.setHighlightColor(Color.TRANSPARENT);
    }
    @Override
    public void onLoginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        btnLogin.setEnabled(false);
        btnLogin.setText("Loading...");
    }

    @Override
    public void hideLoading() {
        btnLogin.setEnabled(true);
        btnLogin.setText("Log In →");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            // It's good practice to cleanup the presenter reference
            // if you implemented a cleanup method there.
        }
    }
}