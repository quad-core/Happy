package com.projects.diwanshusoni.happyapp.activities;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projects.diwanshusoni.happyapp.R;

public class LoginActivity extends AppCompatActivity {
    
    //debug
    String TAG = "1234";
    
    private Button btn_login, btn_reg;
    private EditText et_mail, et_pass;
    private TextInputLayout til_mail, til_pass;

    //progress dialog
    private ProgressDialog dialog;

    //firebaseAuth
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initFire();
        link();

        et_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_mail.getText().toString().trim().isEmpty()){
                    til_mail.setError("Cannot be Empty");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(et_mail.getText().toString().trim()).matches()){
                    til_mail.setError("Enter a valid email address");
                }
                else {
                    til_mail.setError(null);
                }

            }
        });
        et_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_pass.getText().toString().trim().isEmpty()){
                    til_pass.setError("Cannot be Empty");
                }
                else {
                    til_pass.setError(null);
                }

            }
        });
        lSetup();


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
                    //// TODO: 28-09-2017 create dashboard activity ---done
                    //Toast.makeText(LoginActivity.this, "Already Logged In", Toast.LENGTH_SHORT).show();
                   gotoDashBoard();
                }
            }
        };
    }

    private void lSetup() {

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //// TODO: 28-09-2017  Verify the credential and send to dashboard ---done
                //function to login
                loginProcess();
                //gotoDashBoard();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
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
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(til_mail);
            til_mail.setError("Cannot be Empty");
        }
        if (pass.isEmpty()){
            valid = false;
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(til_pass);
            til_pass.setError("Cannot be Empty");
        }

        if (!valid){
            return;
        }

        showWaitDialog("Performing Requested task\nPlease Wait..");
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (!task.isSuccessful()){
                            Log.d(TAG, "onComplete: "+task.getException());
                            YoYo.with(Techniques.Shake)
                                    .duration(500)
                                    .onStart(new YoYo.AnimatorCallback() {
                                        @Override
                                        public void call(Animator animator) {
                                            YoYo.with(Techniques.Shake)
                                                    .duration(500)
                                                    .playOn(til_mail);
                                        }
                                    })
                                    .playOn(til_pass);
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
        Intent intent = new Intent(LoginActivity.this, showPostedTasksActivity.class);
        startActivity(intent);
        finish();
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

    private void showWaitDialog(String PROGRESS_MESSAGE){
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(PROGRESS_MESSAGE);
        dialog.show();
    }
}
