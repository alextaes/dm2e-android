package com.example.dm2e.login.presenter;

public interface LoginPresenter {

    void signIn(String username, String password); //interactuará con el Interactor
    void loginSuccess();
    void loginError(String error);
}
