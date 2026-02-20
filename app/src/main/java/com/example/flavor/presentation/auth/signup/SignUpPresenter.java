package com.example.flavor.presentation.auth.signup;

import android.content.Context;

import com.example.flavor.data.model.User;
import com.example.flavor.data.repo.AuthRepository;

import io.reactivex.disposables.CompositeDisposable;


public class SignUpPresenter implements SignUpContract.Presenter {

    private SignUpContract.View view;
    private final AuthRepository authRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SignUpPresenter(SignUpContract.View view, Context context) {
        this.view = view;
        this.authRepository = AuthRepository.getInstance(context);
    }

    @Override
    public void signUp(String name, String email, String password) {

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            view.onSignUpError("All fields are required");
            return;
        }

        if (password.length() < 6) {
            view.onSignUpError("Password must be at least 6 characters");
            return;
        }

        view.showLoading();

        compositeDisposable.add(
                authRepository
                        .createUserWithEmailAndPassword(email, password)
                        .flatMap(firebaseUser -> {
                            User user = new User(
                                    firebaseUser.getUid(),
                                    name,
                                    email
                            );
                            return authRepository.saveUser(user);
                        })
                        .subscribe(
                                success -> {
                                    view.hideLoading();
                                    view.onSignUpSuccess();
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.onSignUpError(throwable.getMessage());
                                }
                        )
        );
    }
    @Override
    public void signUpWithGoogle(String idToken) {

        view.showLoading();

        compositeDisposable.add(
                authRepository
                        .signInWithGoogle(idToken)
                        .flatMap(firebaseUser -> {

                            User user = new User(
                                    firebaseUser.getUid(),
                                    firebaseUser.getDisplayName(),
                                    firebaseUser.getEmail()
                            );

                            return authRepository.saveUser(user);
                        })
                        .subscribe(
                                success -> {
                                    view.hideLoading();
                                    view.onSignUpSuccess();
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.onSignUpError(throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    public void detach() {
        compositeDisposable.clear();
        view = null;
    }
}
