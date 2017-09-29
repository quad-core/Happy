package com.projects.diwanshusoni.happyapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.diwanshusoni.happyapp.R;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login, btn_reg;
    private EditText et_mail, et_pass;
    private TextInputLayout til_mail, til_pass;

    //firebaseAuth
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFire();
        //my code

        link();
        lSetup();
        //

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_help_login_id);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initFire() {
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null){
                    //// TODO: 28-09-2017 create dashboard activity
                    Toast.makeText(LoginActivity.this, "Already Logged In", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void lSetup() {
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //// TODO: 28-09-2017  Verify the credential and send to dashboard
                //function to login
                loginProcess();
                gotoDashBoard();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //// TODO: 28-09-2017  Verify the credential and send to dashboard
                goRegister();
            }
        });
    }

    private void loginProcess() {
        String email, pass;
        email = et_mail.getText().toString();
        pass = et_pass.getText().toString();

        //check for non Empty values
        boolean valid = true;
        if (email.isEmpty()){
            valid = false;
            til_mail.setError("Cannot be Empty");
        }
        if (pass.isEmpty()){
            valid = false;
            til_pass.setError("Cannot be Empty");
        }

        if (!valid){
            return;
        }

        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Cannot Sign In", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void gotoDashBoard() {
        Toast.makeText(this, "LoginPressed", Toast.LENGTH_SHORT).show();
    }

    private void link() {
        et_mail = (EditText) findViewById(R.id.et_login_mail);
        et_pass = (EditText) findViewById(R.id.et_login_password);
        til_mail = (TextInputLayout) findViewById(R.id.til_etmail_login_id);
        til_pass = (TextInputLayout) findViewById(R.id.til_etpass_login_id);
        btn_login = (Button) findViewById(R.id.btn_login_login_id);
        btn_reg = (Button) findViewById(R.id.btn_login_register);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener!=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
