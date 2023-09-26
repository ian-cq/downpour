package com.example.downpour_d;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signinpg extends AppCompatActivity {

    ImageButton googlesignin;

    TextView signup, forgetpassword;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    public String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        signup = (TextView) findViewById(R.id.textsignup);
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(signinpg.this, signuppg.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(googleSignInAccount != null){
            startActivity(new Intent(signinpg.this, MainActivity.class));
            finish();
        }

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                handleSignInTask(task);
            }
        });

        final EditText email = findViewById(R.id.editTextEmailAddress_signin);
        final EditText password = findViewById(R.id.editTextTextPassword_signin);

        googlesignin = findViewById(R.id.google2);

        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinintent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(signinintent);
            }
        });

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final Button signin = (Button) findViewById(R.id.buttonsignin);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();

                if(!emailTxt.matches(emailPattern)){
                    email.setError("Enter Correct Email");
                }else if(passwordTxt.isEmpty()){
                    password.setError("Password is wrong :(");
                }else{
                    progressDialog.setMessage("Please wait, it might take a while...");
                    progressDialog.setTitle("Sign In");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                nextPage();
                                Toast.makeText(signinpg.this, "Welcome :)", Toast.LENGTH_SHORT).show();

                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(signinpg.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }

    private void handleSignInTask(Task<GoogleSignInAccount> task) {
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            final String getFullName = account.getDisplayName();
            final String getEmail = account.getEmail();
            final String getLname = account.getFamilyName();
            final String getFname = account.getGivenName();
            final Uri getPhoto = account.getPhotoUrl();

            Intent okintent = new Intent(signinpg.this, MainActivity.class);
            okintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(okintent);
            finish();

        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed or canceled :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextPage(){
        Intent intent = new Intent(signinpg.this, MainActivity.class);
        startActivity(intent);
    }
}