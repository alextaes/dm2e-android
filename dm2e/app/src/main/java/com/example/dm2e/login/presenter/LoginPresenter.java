package com.example.dm2e.login.presenter;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

public interface LoginPresenter {

    void signIn(String username, String password, Activity activity, FirebaseAuth firebaseAuth); //interactuará con el Interactor
    void loginSuccess();
    void loginError(String error);
}
