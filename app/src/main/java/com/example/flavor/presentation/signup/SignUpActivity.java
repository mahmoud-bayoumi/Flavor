package com.example.flavor.presentation.signup;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.flavor.R;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    private SignUpPresenter presenter;
    private EditText etName, etEmail, etPassword;
    private AppCompatButton btnSignUp;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        presenter = new SignUpPresenter(this);

        initViews();
        setupBottomText();

        btnSignUp.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            presenter.signUp(name, email, password);
        });
    }
    private void initViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);
    }
    private void setupBottomText() {
        String fullText = "Already have an account? Log In";
        SpannableString spannable = new SpannableString(fullText);

        int start = fullText.indexOf("Log In");
        int end = start + "Log In".length();

        int orangeColor = Color.parseColor("#F59331");

        spannable.setSpan(new ForegroundColorSpan(orangeColor),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new StyleSpan(Typeface.BOLD),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(orangeColor);
                ds.setUnderlineText(false);
            }
        };

        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvGoToLogin.setText(spannable);
        tvGoToLogin.setMovementMethod(LinkMovementMethod.getInstance());
        tvGoToLogin.setHighlightColor(Color.TRANSPARENT);
    }



    @Override
    public void onSignUpSuccess() {
        hideLoading();
        Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Returns to Login screen
    }

    @Override
    public void onSignUpError(String message) {
        hideLoading();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        btnSignUp.setEnabled(false);
        btnSignUp.setText("Processing...");
    }

    @Override
    public void hideLoading() {
        btnSignUp.setEnabled(true);
        btnSignUp.setText("Create Account →");
    }
}