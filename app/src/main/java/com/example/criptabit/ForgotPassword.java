package com.example.criptabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {


    private TextInputLayout email;
    private ProgressBar progressBar;

    FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        email=findViewById(R.id.email);
        progressBar=findViewById(R.id.progressbar);

        auth=FirebaseAuth.getInstance();

    }

    public void forgot(View view) {
        resetPassword();
    }

    private void resetPassword() {
        String email1=email.getEditText().getText().toString().trim();

        if(email1.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            email.setError("Please provide a valid email!");
            email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check your email to reset your password!",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(ForgotPassword.this,"Try again! Something wrong happened!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}