package com.projects.diwanshusoni.happyapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projects.diwanshusoni.happyapp.R;
import com.projects.diwanshusoni.happyapp.constants.constants;
import com.projects.diwanshusoni.happyapp.pojos.PojoEmailUid;
import com.projects.diwanshusoni.happyapp.pojos.PojoUser;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_confirm;
    private EditText et_mail, et_pass, et_name;
    private TextInputLayout til_mail, til_pass, til_name;

    //progress dialog
    private ProgressDialog dialog;

    //firebaseAuth
    private FirebaseAuth auth;

    //firebase RealtimeDb
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setActionBar();
        initFire();
        link();
        lSetup();
    }

    private void initFire() {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(constants.USER_NODE);
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setTitle("AppName "+"Register");
        }
        catch (NullPointerException e){

        }
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void lSetup() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "RegPressed", Toast.LENGTH_SHORT).show();
                registerProcess();
            }
        });
    }

    private void registerProcess() {
        //here verify whether fields are not empty
        String name, email, pass;
        name = et_name.getText().toString();
        email = et_mail.getText().toString();
        pass = et_pass.getText().toString();
        boolean valid = true;

        if (name.isEmpty()){
            til_name.setError("Cannot Be Empty");
            valid = false;
        }
        if (pass.isEmpty()){
            til_pass.setError("Cannot Be Empty");
            valid = false;
        }
        if (email.isEmpty()){
            til_mail.setError("Cannot Be Empty");
            valid = false;
        }

        if (!valid){
            return;
        }

        //Now check whether name is valid
        // *only upto 255 characters allowed
        Pattern namePattern = Pattern.compile("A-Za-z");
        if (name.length() > 255 ){
            til_name.setError("Invalid Format");
            valid = false;
        }
        //check whether email is valid
        //*check proper format
        //*check for previous existence??
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            til_mail.setError("Invalid Format");
            valid = false;
        }
        //// TODO: 28-09-2017 check for existing mail

        if (!valid){
            return;
        }
        //begin create account

        showWaitDialog("Performing Requested task\nPlease Wait..");
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Cannot Create New Account", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //// TODO: 28-09-2017 verification mail, login, etc
                            // Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            createUserDataBase();
                        }
                    }
                });
    }

    private void createUserDataBase() {
        PojoUser pojoUser = new PojoUser();
        pojoUser.setEmail(et_mail.getText().toString());
        pojoUser.setName(et_name.getText().toString());

        try {

        }
        catch (NullPointerException e){
            databaseReference.child(constants.USER_NODE_UID).child(auth.getCurrentUser().getUid()).setValue(pojoUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                createEmaildb();
                            }
                        }
                    });
        }
    }

    private void createEmaildb() {
        PojoEmailUid pojoEmailUid = new PojoEmailUid();
        //pojoEmailUid.setEmail(et_mail.getText().toString());
        boolean valid = true;
        try {
            pojoEmailUid.setUid(auth.getCurrentUser().getUid());
        }
        catch (NullPointerException e){
            valid = false;
        }

        if (!valid){
            dialog.dismiss();
            Toast.makeText(this, "Nullpointer Exception", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(constants.USER_NODE_EMAIL).child(et_mail.getText().toString().replace(".","_")).setValue(pojoEmailUid)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (dialog.isShowing()){
                            dialog.dismiss();
                        }
                        if (!task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Created Account Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void link() {
        et_mail = (EditText) findViewById(R.id.et_reg_mail);
        et_pass = (EditText) findViewById(R.id.et_reg_password);
        et_name = (EditText) findViewById(R.id.et_reg_name);
        til_mail = (TextInputLayout) findViewById(R.id.til_reg_email);
        til_pass = (TextInputLayout) findViewById(R.id.til_reg_etpass);
        til_name = (TextInputLayout) findViewById(R.id.til_reg_name);
        btn_confirm = (Button) findViewById(R.id.btn_reg_confirm);
    }

    private void showWaitDialog(String PROGRESS_MESSAGE){
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(PROGRESS_MESSAGE);
        dialog.show();
    }

}
