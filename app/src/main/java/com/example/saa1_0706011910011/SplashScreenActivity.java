package com.example.saa1_0706011910011;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.saa1_0706011910011.fragment.ScheduleFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1500;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseUser fUser= fAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fUser != null) {
                    startActivity(new Intent(SplashScreenActivity.this, StudentMainActivity.class));
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, StarterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            },SPLASH_TIME_OUT);

        }
    }