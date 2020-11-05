package com.example.saa1_0706011910011.adapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saa1_0706011910011.R;
import com.example.saa1_0706011910011.model.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EnrollCourseAdapter extends RecyclerView.Adapter<EnrollCourseAdapter.CardViewViewHolder> {


    private DatabaseReference mDatabase;
    FirebaseDatabase dbEnroll;


    private Context context;
    private ArrayList<Course> listCourse;

    private ArrayList<Course> getListCourse() {
        return listCourse;
    }

    public void setListCourse(ArrayList<Course> listCourse) {
        this.listCourse = listCourse;
    }

    public EnrollCourseAdapter(final Context context) {
        this.context = context;
    }

    DatabaseReference dbCourse = FirebaseDatabase.getInstance().getReference("course");

    @NonNull
    @Override
    public EnrollCourseAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_adapter, parent, false);
        return new EnrollCourseAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final EnrollCourseAdapter.CardViewViewHolder holder, int position) {
        final Course course = getListCourse().get(position);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        holder.lbl_subject.setText(course.getSubject());
        holder.lbl_day.setText(course.getDay());
        holder.lbl_start.setText(course.getStart());
        holder.lbl_end.setText(course.getEnd());
        holder.lbl_lecturer.setText(course.getLecturer());

        holder.btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConflict(course);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getListCourse().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_subject, lbl_start, lbl_end, lbl_day, lbl_lecturer;
        Button btn_enroll;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_subject = itemView.findViewById(R.id.lbl_subject_course);
            lbl_lecturer = itemView.findViewById(R.id.lbl_lecturer_course);
            lbl_day = itemView.findViewById(R.id.lbl_date_course);
            lbl_start = itemView.findViewById(R.id.lbl_start_course);
            lbl_end = itemView.findViewById(R.id.lbl_end_course);
            btn_enroll = itemView.findViewById(R.id.btn_enroll_course);
        }
    }

    MutableLiveData<Course> courseToAdd = new MutableLiveData<>();

    public MutableLiveData<Course> getCourseToAdd(){
        return courseToAdd;
    }

    boolean conflict = false;

    public void checkConflict(final Course chosenCourse) {
        final int  chosenCourseStart = Integer.parseInt(chosenCourse.getStart().replace(":",""));
        final int chosenCourseEnd = Integer.parseInt(chosenCourse.getEnd().replace(":",""));
        final String course_temp_day = chosenCourse.getDay();

        FirebaseDatabase.getInstance()
                .getReference("student")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("course")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        conflict = false;
                        for(DataSnapshot childSnapshot : snapshot.getChildren()){
                            Course course = childSnapshot.getValue(Course.class);
                            int courseStart = Integer.parseInt(course.getStart().replace(":",""));
                            int courseEnd = Integer.parseInt(course.getEnd().replace(":",""));
                            String course_day = course.getDay();

                            if (course_day.equalsIgnoreCase(course_temp_day)){
                                if (chosenCourseStart >= courseStart && chosenCourseStart < courseEnd){
                                    conflict = true;
                                    break;
                                }
                                if (courseEnd > courseStart && chosenCourseStart <= courseEnd){
                                    conflict = true;
                                    break;
                                }
                            }
                        }

                        if (conflict){
                            Toast.makeText(context, "Course Conflict!", Toast.LENGTH_SHORT).show();
                        } else {
                            courseToAdd.setValue(chosenCourse);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}

