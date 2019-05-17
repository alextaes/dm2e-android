package com.example.dm2e.login.view;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.dm2e.R;
import com.example.dm2e.login.presenter.LoginPresenter;
import com.example.dm2e.login.presenter.LoginPresenterImpl;
import com.example.dm2e.view.ContainerActivity;
import com.example.dm2e.view.CreateAccountActivity;

public class LoginActivity extends AppCompatActivity implements LoginView{

    private TextInputEditText username, password;
    private Button login;
    private ProgressBar progressBarLogin;
    private LoginPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username         = (TextInputEditText) findViewById(R.id.username);
        password         = (TextInputEditText) findViewById(R.id.password);
        login            = (Button) findViewById(R.id.login);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressbarLogin);

        hideProgressBar();

        presenter = new LoginPresenterImpl(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CHECKEAR QUE LOS TOAST MUESTREN TEXTO DE ERROR
                //if (username.getText().toString().equals("")&& password.getText().toString().equals("")) {
                    //loginError(R.string.login_toast);
                //} else if (username.getText().toString().equals("")){
                    //loginError(String.valueOf(R.string.login_toast_username));
                //} else if (password.getText().toString().equals("")){
                    //loginError(String.valueOf(R.string.login_toast_password));
                //} else {
                    presenter.signIn(username.getText().toString(),password.getText().toString());
                //}


            }
        });

    }


    @Override
    public void enableInputs() {
        username.setEnabled(true);
        password.setEnabled(true);
        login.setEnabled(true);
    }

    @Override
    public void disableInputs() {
        username.setEnabled(false);
        password.setEnabled(false);
        login.setEnabled(false);
    }

    @Override
    public void showProgressBar() {
        progressBarLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLogin.setVisibility(View.GONE);

    }

    @Override
    public void loginError(String error) {
        Toast.makeText(this,getString(R.string.login_error) + error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goCreateAccount() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void goHome() {
        Intent intent = new Intent(this, ContainerActivity.class);
        startActivity(intent);
    }


}
