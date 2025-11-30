package com.rakib.studentportal.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rakib.studentportal.DatabaseHelper;
import com.rakib.studentportal.R;

public class AddStudentActivity extends AppCompatActivity {
    private TextInputEditText etName, etEmail, etPhone, etPassword;
    private TextInputLayout nameInputLayout, emailInputLayout, phoneInputLayout, passwordInputLayout;
    private Button btnAddStudent, btnBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        initializeViews();

        btnAddStudent.setOnClickListener(v -> addStudent());
        btnBack.setOnClickListener(v -> finish());
    }

    private void initializeViews() {
        // TextInputLayouts
        nameInputLayout = findViewById(R.id.nameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        phoneInputLayout = findViewById(R.id.phoneInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);

        // EditTexts
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);

        // Buttons
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnBack = findViewById(R.id.btnBack);
    }

    private void addStudent() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Clear previous errors
        clearErrors();

        if (!validateInputs(name, email, phone, password)) {
            return;
        }

        boolean success = dbHelper.addStudent(name, email, phone, password);
        if (success) {
            Toast.makeText(this, "Student added successfully!", Toast.LENGTH_SHORT).show();
            clearForm();
            finish();
        } else {
            Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String name, String email, String phone, String password) {
        boolean isValid = true;

        if (name.isEmpty()) {
            nameInputLayout.setError("Full name is required");
            isValid = false;
        }

        if (email.isEmpty()) {
            emailInputLayout.setError("Email is required");
            isValid = false;
        } else if (!isValidEmail(email)) {
            emailInputLayout.setError("Please enter a valid email");
            isValid = false;
        }

        if (phone.isEmpty()) {
            phoneInputLayout.setError("Phone number is required");
            isValid = false;
        } else if (!isValidPhone(phone)) {
            phoneInputLayout.setError("Please enter a valid phone number");
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
        nameInputLayout.setError(null);
        emailInputLayout.setError(null);
        phoneInputLayout.setError(null);
        passwordInputLayout.setError(null);
    }

    private void clearForm() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etPassword.setText("");
        clearErrors();
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPhone(String phone) {
        // Basic phone validation - adjust as needed
        return phone.length() >= 10;
    }
}