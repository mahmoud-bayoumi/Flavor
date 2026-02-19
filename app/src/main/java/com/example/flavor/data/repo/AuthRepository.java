package com.example.flavor.data.repo;

import android.content.Context;

import com.example.flavor.data.model.User;
import com.example.flavor.data.source.AuthFirebaseDatasource;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Single;

public class AuthRepository {

    private static AuthRepository instance;
    private final AuthFirebaseDatasource authFirebaseDatasource;

    private AuthRepository(Context context) {
        authFirebaseDatasource =
                AuthFirebaseDatasource.getInstance(context);
    }

    public static AuthRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRepository(context);
        }
        return instance;
    }

    public Single<FirebaseUser> createUserWithEmailAndPassword(
            String email, String password) {
        return authFirebaseDatasource
                .createUserWithEmailAndPassword(email, password);
    }
    public Single<FirebaseUser> signInWithEmailAndPassword(
            String email, String password) {
        return authFirebaseDatasource.signInWithEmailAndPassword(email, password);
    }

    public Single<Boolean> saveUser(User user) {
        return authFirebaseDatasource.saveUser(user);
    }
}
