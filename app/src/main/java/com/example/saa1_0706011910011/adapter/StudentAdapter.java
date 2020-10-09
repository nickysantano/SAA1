package com.example.saa1_0706011910011.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saa1_0706011910011.R;
import com.example.saa1_0706011910011.RegisterActivity;
import com.example.saa1_0706011910011.StudentData;
import com.example.saa1_0706011910011.model.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.CardViewViewHolder> {

    private Context context;
    private ArrayList<Student> listStudent;
    Button btn_edit, btn_delete;
    AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    private ArrayList<Student> getListStudent() {
        return listStudent;
    }
    public void setListStudent(ArrayList<Student> listStudent) {
        this.listStudent = listStudent;
    }

    public StudentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StudentAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter, parent, false);
        return new StudentAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.CardViewViewHolder holder, int position) {
        final Student student = getListStudent().get(position);
        holder.lbl_name.setText(student.getName());
        holder.lbl_nim.setText(student.getNim());
        holder.lbl_email.setText(student.getEmail());
        holder.lbl_gender.setText(student.getGender());
        holder.lbl_age.setText(student.getAge());
        holder.lbl_address.setText(student.getAddress());
    }

    @Override
    public int getItemCount() {
        return getListStudent().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView lbl_name, lbl_nim, lbl_email, lbl_gender, lbl_age, lbl_address;
        OnCardListener onCardListener;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_name = itemView.findViewById(R.id.student_name);
            lbl_nim = itemView.findViewById(R.id.student_nim);
            lbl_email = itemView.findViewById(R.id.student_email);
            lbl_gender = itemView.findViewById(R.id.student_gender);
            lbl_age = itemView.findViewById(R.id.student_age);
            lbl_address = itemView.findViewById(R.id.student_address);
            btn_edit = itemView.findViewById(R.id.btn_edit_student);
            btn_delete = itemView.findViewById(R.id.btn_delete_student);

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, RegisterActivity.class);
                    intent.putExtra("action", "edit");
                    intent.putExtra("data_student", listStudent.get(position));
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, StudentData.class);
                    intent.putExtra("action", "delete");
                    intent.putExtra("data_student", listStudent.get(position));
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            onCardListener.OnCardClick(getAdapterPosition());
        }
    }

    public interface OnCardListener{
        void OnCardClick (int position);
    }
}
