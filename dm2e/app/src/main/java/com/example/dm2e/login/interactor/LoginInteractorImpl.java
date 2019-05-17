package com.example.dm2e.login.interactor;

import com.example.dm2e.login.presenter.LoginPresenter;
import com.example.dm2e.login.repository.LoginRepository;
import com.example.dm2e.login.repository.LoginRepositoryImpl;

public class LoginInteractorImpl implements LoginInteractor {

    private LoginPresenter presenter;
    private LoginRepository repository;


    public LoginInteractorImpl(LoginPresenter presenter) {
        this.presenter = presenter;
        repository = new LoginRepositoryImpl(presenter);

    }

    @Override
    public void signIn(String username, String password) {
        repository.signIn(username, password);
    }
}
