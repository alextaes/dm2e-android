package com.example.dm2e.login.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.example.dm2e.R;
import com.example.dm2e.model.User;
import com.example.dm2e.view.ContainerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button btnJoinUs;
    private TextInputEditText edtEmail, edtPassword,edtConfPassword, edtName;
    private ProgressBar progressBarCreate;
    private DatabaseReference firebaseDataUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        showToolbar(getResources().getString(R.string.toolbar_title_createaccount),true);

        Crashlytics.log("Inicio "+ TAG);

        btnJoinUs = (Button) findViewById(R.id.joinUs);
        edtName = (TextInputEditText) findViewById(R.id.name);
        edtEmail = (TextInputEditText) findViewById(R.id.email);
        edtPassword = (TextInputEditText) findViewById(R.id.password_createaccount);
        edtConfPassword = (TextInputEditText) findViewById(R.id.confirmPasswd);
        progressBarCreate = (ProgressBar) findViewById(R.id.progressbarCreate);

        hideProgressBar();

        //Instaciamos la variable FirebaseAuth para el proceso de autenticacion
        //de usuario.
        firebaseDataUsers = FirebaseDatabase.getInstance().getReference("Users");
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

        //Cuando pulsamos el boton de registrarse, obtenemos los 2
        //datos clave.
        btnJoinUs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    //Mostramos mensaje si la creacion del usuario fue exitosa
    //o mensaje de error en caso contrario
    private void createAccount() {

        final String name = edtName.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfPassword.getText().toString().trim();


        if (name.isEmpty()) {
            Toast.makeText(this, R.string.input_error_name, Toast.LENGTH_SHORT).show();
            edtName.requestFocus();
        }

        else if (email.isEmpty()) {
            Toast.makeText(this, R.string.input_error_email, Toast.LENGTH_SHORT).show();
            edtEmail.requestFocus();
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.input_error_email_invalid, Toast.LENGTH_SHORT).show();
            edtEmail.requestFocus();
        }

        else if (password.isEmpty()) {
            Toast.makeText(this, R.string.input_error_password, Toast.LENGTH_SHORT).show();
            edtPassword.requestFocus();
        }

        else if (password.length() < 6) {
            Toast.makeText(this, R.string.input_error_password_length, Toast.LENGTH_SHORT).show();
            edtPassword.requestFocus();
        }

        else if (!confirmPassword.equals(password)) {
            Toast.makeText(this, R.string.input_error_password_conf, Toast.LENGTH_SHORT).show();
            edtConfPassword.requestFocus();
        }

        else {

            showProgressBar();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                User user = new User(id, name, email);

                                firebaseDataUsers
                                        .child(id)
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(CreateAccountActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                            goHome();
                                        } else {
                                            //display a failure message
                                            Toast.makeText(CreateAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(CreateAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            hideProgressBar();
                        }
                    });

        }
    }


    public void goHome() {
        Intent intent = new Intent(this, ContainerActivity.class);
        startActivity(intent);
    }

    public void showToolbar(String title, boolean upButton) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }

    //Antes de onCreate generamos el listener
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    //Quitamos el listener cuando la Activity no este viva
    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }


    public void showProgressBar() {
        progressBarCreate.setVisibility(View.VISIBLE);
    }


    public void hideProgressBar() {
        progressBarCreate.setVisibility(View.GONE);

    }
}
