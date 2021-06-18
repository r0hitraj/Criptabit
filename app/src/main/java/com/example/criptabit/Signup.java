package com.example.criptabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    TextInputLayout regName,regUsername,regEmail,regPhone,regPassword;
private Button regBtn,loginBtn;
private ProgressBar progressBar;
private FirebaseDatabase rootNode;
private FirebaseAuth mAuth;
    String username,email,password,name,phone;

    private Boolean validateName(){
        String val=regName.getEditText().getText().toString();

        if(val.isEmpty()){
            regName.setError("Field Cannot Be Empty");
            return false;
        }
        else{
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val=regUsername.getEditText().getText().toString();
        String noWhitespace ="\\A\\w{4,20}\\z";

        if(val.isEmpty()){
            regUsername.setError("Field Cannot Be Empty");
            return false;
        }
        else if(val.length()>=15){
            regUsername.setError("Username Too Long");
            return false;
        }
        else if(!val.matches(noWhitespace)){
            regUsername.setError("White Spaces Are Not Allowed");
            return false;
        }
        else{
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val=regEmail.getEditText().getText().toString();
        String emailPattern ="[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            regEmail.setError("Field Cannot Be Empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            regEmail.setError("Invalid email address");
            return false;
        }
        else{
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhone() {
        String val=regPhone.getEditText().getText().toString();

        if(val.isEmpty()){
            regPhone.setError("Field Cannot Be Empty");
            return false;
        }
        else{
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val=regPassword.getEditText().getText().toString();
        String passwordVal="^" + "(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{8,}$";

//        (?=.*[0-9]) a digit must occur at least once
//                (?=.*[a-z]) a lower case letter must occur at least once
//                (?=.*[A-Z]) an upper case letter must occur at least once
//                (?=.*[@#$%^&+=]) a special character must occur at least once
//        (?=\\S+$) no whitespace allowed in the entire string
//                .{8,} at least 8 characters


        if(val.isEmpty()){
            regPassword.setError("Field Cannot Be Empty");
            return false;
        }
        else if(!val.matches(passwordVal)){
            regPassword.setError("Password Is Too Weak");
            return false;
        }
        else{
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }




    @Override
    @SuppressWarnings("DEPRECATION")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        // Hooks to all xml elements in signup activity

        regName=findViewById(R.id.name);
        regUsername=findViewById(R.id.username);
        regEmail=findViewById(R.id.email);
        regPhone=findViewById(R.id.phone);
        regPassword=findViewById(R.id.password);
        regBtn=findViewById(R.id.regbtn);
        loginBtn=findViewById(R.id.loginbtn);
        progressBar=findViewById(R.id.progressbar);
        mAuth=FirebaseAuth.getInstance();


        regBtn.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                if(!validateName()| !validateUsername()| !validatePassword() |!validatePhone() |!validateEmail()){  //for validation
                    return;
                }

                name=regName.getEditText().getText().toString().trim();
                username=regUsername.getEditText().getText().toString().trim();
                email=regEmail.getEditText().getText().toString().trim();
                phone=regPhone.getEditText().getText().toString().trim();
                password=regPassword.getEditText().getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE); //for showing progress bar

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            UserHelper helperClass =new UserHelper(name,username,email,phone,password);
                            FirebaseDatabase.getInstance()
                                    .getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser()
                                            .getUid()).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull  Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(Signup.this,"User Has Been Registered Successfully!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(view.GONE);
                                    }else{
                                        Toast.makeText(Signup.this,"Failed To Register Try Again!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(view.GONE);
                                    }

                                }
                            });
                        }
                        else{
                            Toast.makeText(Signup.this,"Failed To Register Try Again!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(view.GONE);
                        }

                    }
                });


               Intent intent = new Intent(Signup.this, Login.class);
               startActivity(intent);

            }
        });





    }




    public void goToLogin(View view) {

        Intent intent = new Intent(Signup.this, Login.class);
        startActivity(intent);
    }

    public void goToDashboard(View view) {

        Intent intent = new Intent(Signup.this, Dashboard.class);
        startActivity(intent);
    }



}
