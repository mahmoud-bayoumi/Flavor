package com.example.flavor.presentation.auth.login;

import android.content.Context;

import com.example.flavor.data.repo.AuthRepository;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;
    private final AuthRepository authRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.authRepository = AuthRepository.getInstance(context);
    }

    @Override
    public void login(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            view.onLoginError("Fields cannot be empty");
            return;
        }

        view.showLoading();

        compositeDisposable.add(
                authRepository
                        .signInWithEmailAndPassword(email, password)
                        .subscribe(
                                firebaseUser -> {
                                    view.hideLoading();
                                    view.onLoginSuccess();
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.onLoginError("Invaild Email Or Password. Try Again!");
                                }
                        )
        );
    }

    @Override
    public void loginWithGoogle(String idToken) {

        view.showLoading();

        compositeDisposable.add(
                authRepository
                        .signInWithGoogle(idToken)
                        .subscribe(
                                firebaseUser -> {
                                    view.hideLoading();
                                    view.onLoginSuccess();
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.onLoginError(throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    public void loginWithFacebook() {
        // Will be implemented later
    }

    @Override
    public void detach() {
        compositeDisposable.clear();
        view = null;
    }
}
