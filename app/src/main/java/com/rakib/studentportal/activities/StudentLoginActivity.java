package com.rakib.studentportal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rakib.studentportal.DatabaseHelper;
import com.rakib.studentportal.R;
import com.rakib.studentportal.model.Student;

public class StudentLoginActivity extends AppCompatActivity {
    private TextInputEditText etEmail, etPassword;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private MaterialButton btnLogin, btnRegister, btnBack;
    private TextView tvForgotPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        initializeViews();

        setupClickListeners();
    }

    private void initializeViews() {
        // TextInputLayouts
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);

        // EditTexts
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        // Buttons
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        // Other views
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> loginStudent());
        btnBack.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> navigateToRegister());
        tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void loginStudent() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Clear previous errors
        clearErrors();

        if (!validateInputs(email, password)) {
            return;
        }

        Student student = dbHelper.loginStudent(email, password);
        if (student != null) {
            Toast.makeText(this, "🎉 Login successful!", Toast.LENGTH_SHORT).show();
            navigateToDashboard(student);
        } else {
            Toast.makeText(this, "❌ Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

        if (email.isEmpty()) {
            emailInputLayout.setError("Email is required");
            isValid = false;
        } else if (!isValidEmail(email)) {
            emailInputLayout.setError("Please enter a valid email address");
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordInputLayout.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordInputLayout.setError("Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }

    private void clearErrors() {
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
        return email.matches(emailPattern);
    }

    private void navigateToRegister() {
        Intent intent = new Intent(this, StudentRegisterActivity.class);
        startActivity(intent);
    }

    private void navigateToDashboard(Student student) {
        Intent intent = new Intent(this, StudentDashboardActivity.class);
        intent.putExtra("STUDENT_ID", student.getId());
        startActivity(intent);
        finish();
    }

    private void showForgotPasswordDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Forgot Password?")
                .setMessage("Please contact the administration to reset your password.")
                .setPositiveButton("OK", null)
                .setNegativeButton("Contact Admin", (dialog, which) -> {
                    Toast.makeText(this, "Admin contact feature coming soon!", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}