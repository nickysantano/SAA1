package com.example.saa1_0706011910011;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        mToolbar = findViewById(R.id.toolbar_lect_data);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Spinner spinnerDay = findViewById(R.id.spinner_day);
        ArrayAdapter<CharSequence> adapterDay = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapterDay);
        spinnerDay.setOnItemSelectedListener(this);

        Spinner spinnerTime = findViewById(R.id.spinner_time);
        ArrayAdapter<CharSequence> adapterTime = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapterTime);
        spinnerTime.setOnItemSelectedListener(this);

        Spinner spinnerTime2 = findViewById(R.id.spinner_time2);
        ArrayAdapter<CharSequence> adapterTime2 = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapterTime2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime2.setAdapter(adapterTime2);
        spinnerTime2.setOnItemSelectedListener(this);

        Spinner spinnerLecturer = findViewById(R.id.spinner_lecturer);
        ArrayAdapter<CharSequence> adapterLecturer = ArrayAdapter.createFromResource(this, R.array.lecturer, android.R.layout.simple_spinner_item);
        adapterLecturer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLecturer.setAdapter(adapterLecturer);
        spinnerLecturer.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}