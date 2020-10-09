package com.example.saa1_0706011910011;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.saa1_0706011910011.fragment.AccountFragment;
import com.example.saa1_0706011910011.fragment.CourseFragment;
import com.example.saa1_0706011910011.fragment.ScheduleFragment;
import com.example.saa1_0706011910011.model.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class StudentMainActivity extends AppCompatActivity {
    Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =

            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_schedule:
                            selectedFragment = new ScheduleFragment();
                            break;

                        case R.id.nav_courses:
                            selectedFragment = new CourseFragment();
                            break;

                        case R.id.nav_account:
                            selectedFragment = new AccountFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        btn_logout = findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(StudentMainActivity.this, StarterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });
//        switch (item.getItemId()){
//            case R.id.btn_logout:
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(StudentMainActivity.this, StarterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                return true;
//        }
        return false;
    }
}