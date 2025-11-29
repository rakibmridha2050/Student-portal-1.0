package com.rakib.studentportal.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.rakib.studentportal.DatabaseHelper;
import com.rakib.studentportal.R;

public class AddStudentActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone, etPassword;
    private Button btnAddStudent, btnBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnBack = findViewById(R.id.btnBack);

        btnAddStudent.setOnClickListener(v -> addStudent());
        btnBack.setOnClickListener(v -> finish());
    }

    private void addStudent() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.addStudent(name, email, phone, password);
        if (success) {
            Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show();
        }
    }
}