package com.example.flavor.data.source;

import android.content.Context;

import com.example.flavor.data.model.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.Single;

public class AuthFirebaseDatasource {

    private static AuthFirebaseDatasource instance;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    private AuthFirebaseDatasource(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public static AuthFirebaseDatasource getInstance(Context context) {
        if (instance == null) {
            instance = new AuthFirebaseDatasource(context);
        }
        return instance;
    }

    public Single<FirebaseUser> createUserWithEmailAndPassword(
            String email, String password) {

        return Single.create(emitter ->
                firebaseAuth
                        .createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(result -> {
                            if (!emitter.isDisposed()) {
                                emitter.onSuccess(result.getUser());
                            }
                        })
                        .addOnFailureListener(e -> {
                            if (!emitter.isDisposed()) {
                                emitter.onError(e);
                            }
                        })
        );
    }
    public Single<FirebaseUser> signInWithEmailAndPassword(
            String email, String password) {

        return Single.create(emitter ->
                firebaseAuth
                        .signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(result -> {
                            if (!emitter.isDisposed()) {
                                emitter.onSuccess(result.getUser());
                            }
                        })
                        .addOnFailureListener(e -> {
                            if (!emitter.isDisposed()) {
                                emitter.onError(e);
                            }
                        })
        );
    }


    public Single<FirebaseUser> signInWithGoogle(String idToken) {
        return Single.create(emitter -> {
            AuthCredential credential =
                    GoogleAuthProvider.getCredential(idToken, null);

            firebaseAuth
                    .signInWithCredential(credential)
                    .addOnSuccessListener(result -> {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(result.getUser());
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }



    public Single<Boolean> saveUser(User user) {
        return Single.create(emitter ->
                firestore
                        .collection("users")
                        .document(user.uid)
                        .set(user)
                        .addOnSuccessListener(unused -> {
                            if (!emitter.isDisposed()) {
                                emitter.onSuccess(true);
                            }
                        })
                        .addOnFailureListener(e -> {
                            if (!emitter.isDisposed()) {
                                emitter.onError(e);
                            }
                        })
        );
    }
    public void logout() {
        firebaseAuth.signOut();
    }
}
