package com.rakib.studentportal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.rakib.studentportal.DatabaseHelper;
import com.rakib.studentportal.R;
import com.rakib.studentportal.model.Student;

public class StudentLoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister, btnBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        btnLogin.setOnClickListener(v -> loginStudent());
        btnBack.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentRegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginStudent() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Student student = dbHelper.loginStudent(email, password);
        if (student != null) {
            Intent intent = new Intent(this, StudentDashboardActivity.class);
            intent.putExtra("STUDENT_ID", student.getId());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
        }
    }
}