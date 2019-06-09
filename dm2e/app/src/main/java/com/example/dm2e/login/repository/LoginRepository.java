package com.example.dm2e.login.repository;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;


/**
 *
 * @author Alejandro Taghavi Espinosa
 *
 * Proyecto DM2E
 */

public interface LoginRepository {

    void signIn(String username, String password, Activity activity, FirebaseAuth firebaseAuth);

}
