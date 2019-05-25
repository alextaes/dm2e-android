package com.example.dm2e;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Dm2eApplication extends Application {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseStorage firebaseStorage;
    private String TAG = "Dm2eApplication";




    @Override
    public void onCreate() {
        super.onCreate();

        Crashlytics.log("Inicio variables Dm2eApplication");

        FacebookSdk.sdkInitialize(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    //Log.w(TAG,"Usuario logueado " + firebaseUser.getEmail());
                    Crashlytics.log(Log.WARN,TAG,"Usuario logueado " + firebaseUser.getEmail());

                } else {
                    //Log.w(TAG,"Imposible hacer login");
                    Crashlytics.log(Log.WARN,TAG,"Imposible hacer login");
                }
            }
        };

        firebaseStorage = FirebaseStorage.getInstance();
    }

    public StorageReference getStorageReference() {
        return firebaseStorage.getReference();
    }
}
