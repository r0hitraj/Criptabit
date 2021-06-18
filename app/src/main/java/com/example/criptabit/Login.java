package com.example.criptabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Pair;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private Button callSignUp,loginBtn;
    private ImageView image;
    private TextView logoText;
    private TextInputLayout password,email;
    private String nameFromDB,usernameFromDB,phoneFromDB,emailFromDB,passwordFromDB;
    private String userEnteredEmail,userEnteredPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


//    private Boolean validateUsername(){
//        String val=username.getEditText().getText().toString();
//
//        if(val.isEmpty()){
//            username.setError("Field Cannot Be Empty");
//            return false;
//        }
//        else{
//            username.setError(null);
//            username.setErrorEnabled(false);
//            return true;
//        }
//    }
    private Boolean validateEmail(){
        String val=email.getEditText().getText().toString();
        String emailPattern ="[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            email.setError("Field Cannot Be Empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            email.setError("Invalid email address");
            return false;
        }
        else{
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val=password.getEditText().getText().toString();

        if(val.isEmpty()){
            password.setError("Field Cannot Be Empty");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }


    @Override
    @SuppressWarnings("DEPRECATION")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
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

        loginBtn=findViewById(R.id.loginBtn);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        progressBar=findViewById(R.id.progressbar);
        mAuth=FirebaseAuth.getInstance();

    }


    public void goToSignUp(View view) {
        Intent intent = new Intent(Login.this, Signup.class);
        startActivity(intent);
    }

    public void goToForgot(View view) {
        Intent intent = new Intent(Login.this, ForgotPassword.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        if( !validateEmail() | ! validatePassword()){
            return;
        }
        else
        {
            isUser();
        }
    }


    private void isUser(){

         userEnteredEmail= email.getEditText().getText().toString().trim();
        userEnteredPassword= password.getEditText().getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(userEnteredEmail,userEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {

                if(task.isSuccessful()){

                    email.setError(null);
                    email.setErrorEnabled(false);

                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){

                        Intent intent =new Intent(Login.this,Dashboard.class);
                        startActivity(intent);
                    }
                    else{

                        user.sendEmailVerification();
                        Toast.makeText(Login.this,"Check Your Email To Verify Your Account!",Toast.LENGTH_LONG).show();
                    }

//                    nameFromDB =dataSnapshot.child(userEnteredEmail).child("name").getValue(String.class);
//                    usernameFromDB =dataSnapshot.child(userEnteredEmail).child("username").getValue(String.class);
//                    phoneFromDB =dataSnapshot.child(userEnteredEmail).child("phone").getValue(String.class);
//                    emailFromDB =dataSnapshot.child(userEnteredEmail).child("email").getValue(String.class);



//                    intent.putExtra("name",nameFromDB);
//                    intent.putExtra("username",usernameFromDB);
//                    intent.putExtra("email",phoneFromDB);
//                    intent.putExtra("phone",emailFromDB);
//                    intent.putExtra("password",passwordFromDB);




                }else {
                    Toast.makeText(Login.this,"Failed To Login Please Check Your Credential's",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });



        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference= database.getReference("users"); //for connecting database with parent schema
        Query checkUser =reference.orderByChild("username").equalTo(userEnteredEmail);  //for checking Username for the given user name
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    email.setError(null);
                    email.setErrorEnabled(false);

                     passwordFromDB =dataSnapshot.child(userEnteredEmail).child("password").getValue(String.class);
                    System.out.println("Toast-\n"+passwordFromDB);
                    System.out.println("Toast-\n"+userEnteredPassword);
                    Toast.makeText(Login.this, passwordFromDB, Toast.LENGTH_SHORT).show();
                    if(passwordFromDB != null && userEnteredPassword != null && passwordFromDB.equals(userEnteredPassword)){


                    }
                    else{
                        password.setError("Wrong Password");
                    }
                }
                else{
                    email.setError("No Such User Exist");
                    email.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }


        });


    }

    public void goToDashboard(View view) {

        Intent intent = new Intent(Login.this, Dashboard.class);
        startActivity(intent);
    }

}