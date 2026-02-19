package com.example.flavor.presentation.auth.signup;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.flavor.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    private SignUpPresenter presenter;
    private EditText etName, etEmail, etPassword;
    private AppCompatButton btnSignUp;
    private TextView tvGoToLogin;
    private static final int RC_GOOGLE_SIGN_IN = 1001;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        presenter = new SignUpPresenter(this, this);

        initViews();
        setupGoogleSignIn();
        setupSocialButtons();
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

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void setupSocialButtons() {
        findViewById(R.id.btnGoogle).setOnClickListener(v -> {
            googleSignInClient.revokeAccess().addOnCompleteListener(task -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
            });
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(account -> presenter.signUpWithGoogle(account.getIdToken()))
                    .addOnFailureListener(e -> onSignUpError(e.getMessage()));
        }
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

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

}