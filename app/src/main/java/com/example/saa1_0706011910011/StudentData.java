package com.example.saa1_0706011910011;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saa1_0706011910011.adapter.LecturerAdapter;
import com.example.saa1_0706011910011.adapter.StudentAdapter;
import com.example.saa1_0706011910011.model.Lecturer;
import com.example.saa1_0706011910011.model.Student;
import com.example.utils.ItemClickSupport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentData extends AppCompatActivity {

    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    Toolbar toolbar;
    DatabaseReference dbStudent;
    ArrayList<Student> listStudent = new ArrayList<>();
    RecyclerView rv_student_data;
    String action="";
    Student student;
    int position=0;
    Dialog dialog;
    FirebaseAuth fAuth;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView mEmail;
        setContentView(R.layout.activity_student_data);
        toolbar = findViewById(R.id.toolbar_student_data);
        dialog = Glovar.loadingDialog(StudentData.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbStudent = FirebaseDatabase.getInstance().getReference("student");
        rv_student_data = findViewById(R.id.rv_student_data);

        mEmail = findViewById(R.id.student_email);

        fetchStudentData();

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        position = intent.getIntExtra("position", 0);

        student = intent.getParcelableExtra("data_student");

        if(action.equalsIgnoreCase("delete")) {
        new AlertDialog.Builder(StudentData.this)
                .setTitle("Konfirmasi")
                .setIcon(R.drawable.ic_android_goldtrans_24dp)
                .setMessage("Are you sure to delete "+student.getName()+" data?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        fAuth = FirebaseAuth.getInstance();
                        fUser = fAuth.getCurrentUser();

//                                dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fAuth.signInWithEmailAndPassword(student.getEmail(),student.getPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        fAuth.getCurrentUser().delete();
//                                        dialog.cancel();
                                        dbStudent.child(listStudent.get(position).getUid()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                fUser.delete();
                                                Intent in = new Intent(StudentData.this, StudentData.class);
                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                Toast.makeText(StudentData.this, "Delete success!", Toast.LENGTH_SHORT).show();
                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
                                                startActivity(in, options.toBundle());
                                                finish();
//                                                dialogInterface.cancel();
                                            }
                                        });
                                    }
                                });
                            }
                        }, 2000);
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
//    }
//});
        }

    }

    public void fetchStudentData(){
        dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listStudent.clear();
                rv_student_data.setAdapter(null);
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    Student student = childSnapshot.getValue(Student.class);
                    listStudent.add(student);
                }
                showStudentData(listStudent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showStudentData(final ArrayList<Student> list){
        rv_student_data.setLayoutManager(new LinearLayoutManager(StudentData.this));
        StudentAdapter studentAdapter = new StudentAdapter(StudentData.this);
        studentAdapter.setListStudent(list);
        rv_student_data.setAdapter(studentAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent;
            intent = new Intent(StudentData.this, RegisterActivity.class);
            intent.putExtra("action", "add");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(StudentData.this, AddLecturerActivity.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentData.this);
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

}