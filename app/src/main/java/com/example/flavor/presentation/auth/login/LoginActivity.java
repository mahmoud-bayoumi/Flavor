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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private LoginPresenter presenter;
    private EditText etEmail, etPassword;
    private AppCompatButton btnLogin; // Use AppCompatButton for custom backgrounds
    private TextView tvGoToSignUp;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_GOOGLE_SIGN_IN = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this , this);

        initViews();
        setupGoogleSignIn();
        setupSocialButtons();

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

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
        presenter.detach();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null && account.getIdToken() != null) {
                    // Pass token to presenter
                    presenter.loginWithGoogle(account.getIdToken());
                } else {
                    Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign-in error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}