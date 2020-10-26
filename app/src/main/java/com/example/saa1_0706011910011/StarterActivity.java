package com.example.saa1_0706011910011;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

public class StarterActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button mAddStudent, mAddLecturer, mAddCourse, mLoginStudent;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        mAddStudent = findViewById(R.id.button_addStudent);
        mAddLecturer = findViewById(R.id.button_add_course);
        mAddCourse = findViewById(R.id.button_addCourse);
        mLoginStudent = findViewById(R.id.button_loginStudent);

        mAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent intent = new Intent(StarterActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("action", "add");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
        mAddLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent intent = new Intent(StarterActivity.this, AddLecturerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("action", "add");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
        mAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent intent = new Intent(StarterActivity.this, AddCourseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("action", "add");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
        mLoginStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                Intent intent = new Intent(StarterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("action", "add");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
    }
}