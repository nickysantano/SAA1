package com.example.saa1_0706011910011.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.saa1_0706011910011.Glovar;
import com.example.saa1_0706011910011.R;
import com.example.saa1_0706011910011.StarterActivity;
import com.example.saa1_0706011910011.model.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {
    TextView mName, mNim, mEmail, mGender, mAge, mAddress;
    Button logout;
    FirebaseUser fUser;
    DatabaseReference reference;
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mName = view.findViewById(R.id.student_name);
        mNim = view.findViewById(R.id.student_nim);
        mEmail = view.findViewById(R.id.student_email);
        mGender = view.findViewById(R.id.student_gender);
        mAge = view.findViewById(R.id.student_age);
        mAddress = view.findViewById(R.id.student_address);
        logout = view.findViewById(R.id.btn_logout);
        dialog = Glovar.loadingDialog(getActivity());

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("student").child(fUser.getUid());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), StarterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student student = snapshot.getValue(Student.class);
                mName.setText(student.getName());
                mNim.setText(student.getNim());
                mEmail.setText(student.getEmail());
                mGender.setText(student.getGender());
                mAge.setText(student.getAge());
                mAddress.setText(student.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
                return view;
            }



}
