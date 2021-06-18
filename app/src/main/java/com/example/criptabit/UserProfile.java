package com.example.criptabit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    TextInputLayout fullName,password,email,phone;
    TextView fullNameLabel,usernameLabel;
    String  user_username,user_fullName,user_email,user_phone,user_password,userId;
    private FirebaseUser user;
    private DatabaseReference reference;
    AuthCredential credential;


    @Override
    @SuppressWarnings("DEPRECATION")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
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

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId=user.getUid();

        //Hooks
        fullName=findViewById(R.id.fullname);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        fullNameLabel=findViewById(R.id.full_name);
        usernameLabel=findViewById(R.id.username);

        //for retriving data from firebase session

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                UserHelper userProfile=snapshot.getValue(UserHelper.class); //helper class is the class which can helps and storing retrived data

                    if(userProfile != null)
                    {
                        user_username =userProfile.username;
                        user_fullName =userProfile.name;
                        user_email =userProfile.email;
                        user_phone =userProfile.phone;
                        user_password =userProfile.password;

                        //Hooks
                        fullNameLabel.setText(user_fullName);
                        usernameLabel.setText(user_username);
                        fullName.getEditText().setText(user_fullName);
                        email.getEditText().setText(user_email);
                        phone.getEditText().setText(user_phone);
                        password.getEditText().setText(user_password);
                    }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

                Toast.makeText(UserProfile.this,"Something Wrong Happened!",Toast.LENGTH_LONG).show();

            }
        });



    }

    public void update(View view){

        if(isNameChanged() | isPasswordChanged() | isEmailChanged() | isPhoneChanged() ){
            Toast.makeText(this,"Data Has Been Updated",Toast.LENGTH_LONG).show();

        }
        else
            Toast.makeText(this,"Data is Same and Cannot Be Updated  ",Toast.LENGTH_LONG).show();

    }

    private boolean isPasswordChanged() {

        if(user_password.equals(password.getEditText().getText().toString())){

            reference.child(userId).child("password").setValue(password.getEditText().getText().toString());
            user_password= password.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }

    }
    private boolean isPhoneChanged() {

        if(user_phone.equals(phone.getEditText().getText().toString())){

            reference.child(userId).child("phone").setValue(phone.getEditText().getText().toString());
            user_phone= phone.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }

    }

    private boolean isNameChanged() {

        if(user_fullName.equals(fullName.getEditText().getText().toString())){

            reference.child(userId).child("name").setValue(fullName.getEditText().getText().toString());
            user_fullName= fullName.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }
    private boolean isEmailChanged() {

        if(user_email.equals(email.getEditText().getText().toString())){

            reference.child(userId).child("email").setValue(email.getEditText().getText().toString());
            user_email= email.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }




    public void doLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(UserProfile.this,"Logout Successfully! ",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(UserProfile.this, Dashboard.class);
        startActivity(intent);
    }
    public void goToDashboard(View view) {
        Intent intent = new Intent(UserProfile.this, Dashboard.class);
        startActivity(intent);
    }

    public void goToDelete(View view) {
//        String usemail,uspassword;
//        Data
//        usemail=reference.child((userId).child("email").getValue;
        reference.child(userId).removeValue();
        Toast.makeText(UserProfile.this,"Account Deleted!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(UserProfile.this,Dashboard.class);
        startActivity(intent);
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
//        credential = EmailAuthProvider.getCredential(usemail,uspassword);
//
//        // Prompt the user to re-provide their sign-in credentials
//        user.reauthenticate(credential)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        user.delete()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(UserProfile.this,"Account Deleted!",Toast.LENGTH_LONG).show();
//                                            Intent intent = new Intent(UserProfile.this,Dashboard.class);
//                                            startActivity(intent);
//                                        }
//                                    }
//                                });
//
//                    }
//                });
    }
}