package com.example.criptabit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Looper;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=2000;

    //Variable For Animation
    Animation topAnim,bottomAnim;
    ImageView image;
    TextView logo;
    boolean isLoggedIn=false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //to remove status bar

        setContentView(R.layout.activity_main);

        //animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        image=findViewById(R.id.imageView);
        logo=findViewById(R.id.textView);
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);

        mAuth = FirebaseAuth.getInstance(); //for firebase auth

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){

            @Override
            public void run(){

                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){

                    Intent intent1 = new Intent(MainActivity.this,Dashboard.class);
                    startActivity(intent1);
                    finish();

                }
                else{

                    Intent intent2 = new Intent(MainActivity.this,Login.class);
                    startActivity(intent2);
                    finish();
                }

            }
        },SPLASH_SCREEN);


    }
}