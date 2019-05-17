package com.example.dm2e.login.presenter;

import com.example.dm2e.login.interactor.LoginInteractor;
import com.example.dm2e.login.interactor.LoginInteractorImpl;
import com.example.dm2e.login.view.LoginView;

public class LoginPresenterImpl implements LoginPresenter {


    private LoginView loginView;
    private LoginInteractor interactor;


    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        interactor = new LoginInteractorImpl(this);
    }

    @Override
    public void signIn(String username, String password) {
        loginView.disableInputs();
        loginView.showProgressBar();
        interactor.signIn(username, password);
    }

    @Override
    public void loginSuccess() {
        loginView.goHome();
        loginView.hideProgressBar();
    }

    @Override
    public void loginError(String error) {
        loginView.enableInputs();
        loginView.hideProgressBar();
        loginView.loginError(error);

    }

}
