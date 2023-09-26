package com.example.downpour_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signuppg extends AppCompatActivity {

    TextView signin;
    public EditText email,password, confirm_password;
    public String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public String emailTxt, passwordTxt;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signin = (TextView) findViewById(R.id.textsignin_);
        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(signuppg.this, signinpg.class);
                startActivity(intent);
            }
        });

        email = findViewById(R.id.editTextTextEmailAddress_signup);
        password = findViewById(R.id.editTextTextPassword_signup);
        confirm_password = findViewById(R.id.editTextTextPasswordsignup2);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final ImageButton nextbtn = (ImageButton) findViewById(R.id.buttonnext_signup);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailTxt = email.getText().toString();
                passwordTxt = password.getText().toString();
                String confirmpassTxt = confirm_password.getText().toString();

                if(!emailTxt.matches(emailPattern)){
                    email.setError("Enter Correct Email");
                }else if(passwordTxt.isEmpty() || passwordTxt.length()<8){
                    password.setError("Password has to be more than 7 characters");
                }else if(!passwordTxt.equals(confirmpassTxt)){
                    confirm_password.setError("Password is not match");
                }else{
                    progressDialog.setMessage("Directing to the next page...");
                    progressDialog.setTitle("Registration");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    register(emailTxt, passwordTxt);

                }
            }
        });


    }

    public void register(String emailA, String passwordA){
        mAuth.createUserWithEmailAndPassword(emailA, passwordA).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    nextPage();
                    Toast.makeText(signuppg.this, "Registering...", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(signuppg.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void nextPage() {
        Intent intent = new Intent(signuppg.this, signupper_pg.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public String getEmailTxt(){
        return emailTxt;
    }
}