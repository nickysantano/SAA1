package com.example.saa1_0706011910011;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saa1_0706011910011.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity{
    TextView mLoginBtn, mName, mEmail, mPassword, mNim, mAge, mAddress, txt_register;
    Button btn_register;
    RadioGroup rg_gender;
    RadioButton radioButton, radioMale, radioFemale;
    Toolbar mToolbar;
    String uid="", email="", pass="", name="", nim="", age="", gender="male", address="", action="";
    Student student;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    int position = 0;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLoginBtn = findViewById(R.id.text_loginAcc);
        mName = findViewById(R.id.text_name);
        mEmail = findViewById(R.id.text_email);
        mPassword = findViewById(R.id.text_password);
        mNim = findViewById(R.id.text_nim);
        mAge = findViewById(R.id.text_age);
        radioMale = findViewById(R.id.radio_gender_male);
        radioFemale = findViewById(R.id.radio_gender_female);
        mAddress = findViewById(R.id.text_address);
        mToolbar = findViewById(R.id.toolbar_student_data);
        btn_register = findViewById(R.id.button_register);
        txt_register = findViewById(R.id.txt_register);
        dialog = Glovar.loadingDialog(RegisterActivity.this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("student");

        mName.addTextChangedListener(textWatcher);
        mEmail.addTextChangedListener(textWatcher);
        mPassword.addTextChangedListener(textWatcher);
        mNim.addTextChangedListener(textWatcher);
        mAge.addTextChangedListener(textWatcher);
        mAddress.addTextChangedListener(textWatcher);


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        btn_register = findViewById(R.id.button_register);
        rg_gender = findViewById(R.id.radg_gender_reg_student);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = findViewById(i);
                gender = radioButton.getText().toString();
            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        position = intent.getIntExtra("position", 0);
        student = intent.getParcelableExtra("data_student");

        if(action.equalsIgnoreCase("add")){
            btn_register.setText(R.string.regStudent);
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addStudent();
                }
            });
        }else if (action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("login")){
            mToolbar.setTitle("Edit Student");
            txt_register.setText("// EDIT STUDENT");
            btn_register.setText("Edit");

            mEmail.setEnabled(false);

            mName.setText(student.getName());
            mEmail.setText(student.getEmail());
            mPassword.setText(student.getPass());
            mNim.setText(student.getNim());
            if(student.getGender().equalsIgnoreCase("male")){
                rg_gender.check(R.id.radio_gender_male);
            }else{
                rg_gender.check(R.id.radio_gender_female);
            }
            mAge.setText(student.getAge());
            mAddress.setText(student.getAddress());
        }

        if (student.getGender().equalsIgnoreCase("female")){
            radioFemale.setChecked(true);
            radioMale.setChecked(false);
        }else{
            radioFemale.setChecked(false);
            radioMale.setChecked(true);
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action.equalsIgnoreCase("add")){
                    addStudent();
                }else if (action.equalsIgnoreCase("edit") || (action.equalsIgnoreCase("login"))){
                    editStudent();
                }
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (!action.equalsIgnoreCase("login")) {
                    intent = new Intent(RegisterActivity.this, StarterActivity.class);
                }else{
                    intent = new Intent(RegisterActivity.this, StudentMainActivity.class);
                    intent.putExtra("action", "login");
                }
                startActivity(intent);
                finish();
            }
        });
    }

    public void addStudent(){
        getFormValue();
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            dialog.cancel();
                            uid = mAuth.getCurrentUser().getUid();
                            Student student = new Student(uid, email, pass, name, nim, gender, age, address);
                            mDatabase.child(uid).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Student registration completed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            mAuth.signOut();
                        }else {
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthInvalidCredentialsException malFormed){
                                Toast.makeText(RegisterActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                            }catch (FirebaseAuthUserCollisionException existEmail){
                                Toast.makeText(RegisterActivity.this, "Email already registered!", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            dialog.cancel();
                        }
                    }
                });
    }

    public void editStudent(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        name = mName.getText().toString().trim();
        email = mEmail.getText().toString().trim();
        pass = mPassword.getText().toString().trim();
        nim = mNim.getText().toString().trim();
        age = mAge.getText().toString().trim();
        address = mAddress.getText().toString().trim();

//        if (student.getGender().equalsIgnoreCase("female")){
//            radioFemale.setChecked(true);
//            radioMale.setChecked(false);
//        }else{
//            radioFemale.setChecked(false);
//            radioMale.setChecked(true);
//        }


        Map<String,Object> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("pass", pass);
        params.put("nim", nim);
        params.put("age", age);
        params.put("address", address);
        params.put("gender", gender);

        mDatabase.child("student").child(student.getUid()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                            dialog.cancel();
                Intent intent;
                if (action.equalsIgnoreCase("login")){
                    intent = new Intent(RegisterActivity.this, StudentMainActivity.class);
                    intent.putExtra("action", "login");
                }else{
                    intent = new Intent(RegisterActivity.this, StudentData.class);
                    intent.putExtra("action", "edit");
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!action.equalsIgnoreCase("login")) {
            getMenuInflater().inflate(R.menu.student_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.student_list){
            Intent intent;
            intent = new Intent(RegisterActivity.this, StudentData.class);
            intent.putExtra("action", "not_delete");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
        @Override
        public void onBackPressed() {
            Intent intent;
            if (!action.equalsIgnoreCase("login")) {
                intent = new Intent(RegisterActivity.this, StarterActivity.class);
            }else{
                intent = new Intent(RegisterActivity.this, StudentMainActivity.class);
                intent.putExtra("action", "login");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
            startActivity(intent, options.toBundle());
            finish();
        }

    public void getFormValue() {
        name = mName.getText().toString().trim();
        email = mEmail.getText().toString().trim();
        pass = mPassword.getText().toString().trim();
        nim = mNim.getText().toString().trim();
        age = mAge.getText().toString().trim();
        address = mAddress.getText().toString().trim();
    }

        private TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getFormValue();

                if (!name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !nim.isEmpty() && !age.isEmpty() && !address.isEmpty()) {
                    btn_register.setEnabled(true);
                } else {
                    btn_register.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
}


