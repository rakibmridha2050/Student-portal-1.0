package com.rakib.studentportal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rakib.studentportal.DatabaseHelper;
import com.rakib.studentportal.R;
import com.rakib.studentportal.adapter.PaymentAdapter;
import com.rakib.studentportal.adapter.StudentAdapter;
import com.rakib.studentportal.model.Payment;
import com.rakib.studentportal.model.Student;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private Button btnManageStudents, btnManagePayments, btnAddStudent;
    private LinearLayout studentsSection, paymentsSection;
    private RecyclerView rvStudents, rvPayments;
    private DatabaseHelper dbHelper;
    private StudentAdapter studentAdapter;
    private PaymentAdapter paymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        btnManageStudents = findViewById(R.id.btnManageStudents);
        btnManagePayments = findViewById(R.id.btnManagePayments);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        studentsSection = findViewById(R.id.studentsSection);
        paymentsSection = findViewById(R.id.paymentsSection);
        rvStudents = findViewById(R.id.rvStudents);
        rvPayments = findViewById(R.id.rvPayments);

        setupRecyclerViews();
        loadData();

        btnManageStudents.setOnClickListener(v -> {
            studentsSection.setVisibility(android.view.View.VISIBLE);
            paymentsSection.setVisibility(android.view.View.GONE);
        });

        btnManagePayments.setOnClickListener(v -> {
            studentsSection.setVisibility(android.view.View.GONE);
            paymentsSection.setVisibility(android.view.View.VISIBLE);
        });

        btnAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStudentActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void setupRecyclerViews() {
        // Students RecyclerView
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(this, dbHelper);
        rvStudents.setAdapter(studentAdapter);

        // Payments RecyclerView
        rvPayments.setLayoutManager(new LinearLayoutManager(this));
        paymentAdapter = new PaymentAdapter(this, dbHelper);
        rvPayments.setAdapter(paymentAdapter);
    }

    private void loadData() {
        List<Student> students = dbHelper.getAllStudents();
        studentAdapter.setStudents(students);

        List<Payment> payments = dbHelper.getAllPayments();
        paymentAdapter.setPayments(payments);
    }
}