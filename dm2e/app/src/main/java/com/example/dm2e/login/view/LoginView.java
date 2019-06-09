package com.example.dm2e.login.view;



/**
 *
 * @author Alejandro Taghavi Espinosa
 *
 * Proyecto DM2E
 */

public interface LoginView {

    void enableInputs();
    void disableInputs();

    void showProgressBar();
    void hideProgressBar();

    void loginError(String error);

    void goCreateAccount();
    void goHome();
}
