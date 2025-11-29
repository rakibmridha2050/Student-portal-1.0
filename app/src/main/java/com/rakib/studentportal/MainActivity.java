package com.rakib.studentportal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.rakib.studentportal.activities.AdminLoginActivity;
import com.rakib.studentportal.activities.StudentLoginActivity;
import com.rakib.studentportal.activities.StudentRegisterActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnAdminLogin, btnStudentLogin, btnStudentRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        btnStudentLogin = findViewById(R.id.btnStudentLogin);
        btnStudentRegister = findViewById(R.id.btnStudentRegister);

        btnAdminLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });

        btnStudentLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentLoginActivity.class);
            startActivity(intent);
        });

        btnStudentRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentRegisterActivity.class);
            startActivity(intent);
        });
    }
}