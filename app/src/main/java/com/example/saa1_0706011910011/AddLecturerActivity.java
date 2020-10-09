package com.example.saa1_0706011910011;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.saa1_0706011910011.model.Lecturer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddLecturerActivity extends AppCompatActivity {

    TextInputEditText mName, mExpertise;
    RadioGroup rg_gender;
    RadioButton radioButton;
    String name="", expertise="", gender="male", action="";
    Dialog dialog;
    Button btn_add;
    Toolbar mToolbar;
    Lecturer lecturer;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecturer);
        mName = findViewById(R.id.text_name_lecturer);
        mExpertise = findViewById(R.id.text_expertise);
        rg_gender = findViewById(R.id.rg_gender);
        mToolbar = findViewById(R.id.toolbar_lect_data);
        btn_add = findViewById(R.id.button_addLecturer);

        mName.addTextChangedListener(textWatcher);
        mExpertise.addTextChangedListener(textWatcher);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = findViewById(i);
                gender = radioButton.getText().toString();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLecturer(name, gender, expertise);
                Intent intent = new Intent(AddLecturerActivity.this, StarterActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if(action.equals("add")){
            getSupportActionBar().setTitle(R.string.addlecturer);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mToolbar = findViewById(R.id.toolbar_lect_data);
            btn_add.setText(R.string.addlecturer);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = mName.getText().toString().trim();
                    expertise = mExpertise.getText().toString().trim();
                    addLecturer(name, gender, expertise);
                }
            });
        }else{ //saat activity dari lecturer detail & mau mengupdate data
            getSupportActionBar().setTitle(R.string.editlecturer);
            lecturer = intent.getParcelableExtra("edit_data_lect");
            mName.setText(lecturer.getName());
            mExpertise.setText(lecturer.getExpertise());
            if(lecturer.getGender().equalsIgnoreCase("male")){
                rg_gender.check(R.id.radio_gender_male2);
            }else{
                rg_gender.check(R.id.radio_gender_female2);
            }
            btn_add.setText(R.string.editlecturer);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    dialog.show();
                    name = mName.getText().toString().trim();
                    expertise = mExpertise.getText().toString().trim();
                    Map<String,Object> params = new HashMap<>();
                    params.put("name", name);
                    params.put("expertise", expertise);
                    params.put("gender", gender);
                    mDatabase.child("lecturer").child(lecturer.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            dialog.cancel();
                            Intent intent;
                            intent = new Intent(AddLecturerActivity.this, LecturerData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLecturerActivity.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }
    }

    public void addLecturer(String mnama, String mgender, String mexpertise){
        String mid = mDatabase.child("lecturer").push().getKey();
        Lecturer lecturer = new Lecturer(mid, mnama, mgender, mexpertise);
        mDatabase.child("lecturer").child(mid).setValue(lecturer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddLecturerActivity.this, "Add Lecturer Successfully!", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddLecturerActivity.this, "Add Lecturer Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.lecturer_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lecturer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.lecturer_list){
            Intent intent;
            intent = new Intent(AddLecturerActivity.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLecturerActivity.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            name = mName.getText().toString().trim();
            expertise = mExpertise.getText().toString().trim();
            if (!name.isEmpty() && !expertise.isEmpty()){
                btn_add.setEnabled(true);
            }else{
                btn_add.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}