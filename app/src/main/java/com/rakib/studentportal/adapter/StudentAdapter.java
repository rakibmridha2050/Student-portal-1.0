package com.rakib.studentportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.rakib.studentportal.DatabaseHelper;
import com.rakib.studentportal.model.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private Context context;
    private List<Student> students;
    private DatabaseHelper dbHelper;

    public StudentAdapter(Context context, DatabaseHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        holder.textView.setText(student.getName() + " - " + student.getEmail());

        holder.itemView.setOnClickListener(v -> showStudentOptions(student));
    }

    @Override
    public int getItemCount() {
        return students == null ? 0 : students.size();
    }

    private void showStudentOptions(Student student) {
        String[] options = {"Edit", "Delete"};
        new AlertDialog.Builder(context)
                .setTitle("Student Options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        editStudent(student);
                    } else if (which == 1) {
                        deleteStudent(student);
                    }
                })
                .show();
    }

    private void editStudent(Student student) {
        // For simplicity, we'll just show a toast. You can implement edit functionality
        Toast.makeText(context, "Edit student: " + student.getName(), Toast.LENGTH_SHORT).show();
    }

    private void deleteStudent(Student student) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete " + student.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean success = dbHelper.deleteStudent(student.getId());
                    if (success) {
                        students.remove(student);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Student deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to delete student", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}