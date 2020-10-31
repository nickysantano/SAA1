package com.example.saa1_0706011910011;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText mEmail, mPassword;
    TextView mCreateBtn;
    Toolbar mToolbar;
    Button btn_login;
    String email, password;
    FirebaseAuth auth;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCreateBtn = findViewById(R.id.text_createAcc);
        mToolbar = findViewById(R.id.toolbar_lect_data);
        mEmail = findViewById(R.id.text_email);
        mPassword = findViewById(R.id.text_password);
        btn_login = findViewById(R.id.button_login);
        auth = FirebaseAuth.getInstance();
        dialog = Glovar.loadingDialog(LoginActivity.this);

        mEmail.addTextChangedListener(textWatcher);
        mPassword.addTextChangedListener(textWatcher);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            email = mEmail.getText().toString().trim();
            password = mPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()){
                btn_login.setEnabled(true);
            }else{
                btn_login.setEnabled(false);
            }
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            dialog.show();
                    if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                        dialog.cancel();
                        Toast.makeText(LoginActivity.this, "All field are required!", Toast.LENGTH_SHORT).show();
                    }else{
                        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.cancel();
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(LoginActivity.this, StudentMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
//            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        Intent intent = new Intent(LoginActivity.this, StudentMainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    }else{
//                        Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}