package com.rakib.studentportal.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.rakib.studentportal.DatabaseHelper;
import com.rakib.studentportal.MainActivity;
import com.rakib.studentportal.R;
import com.rakib.studentportal.adapter.PaymentAdapter;
import com.rakib.studentportal.adapter.StudentAdapter;
import com.rakib.studentportal.model.Payment;
import com.rakib.studentportal.model.Student;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminDashboardActivity extends AppCompatActivity {
    private CardView btnManageStudents, btnManagePayments;
    private MaterialButton btnAddStudent, btnLogout;
    private RecyclerView rvStudents, rvPayments;
    private DatabaseHelper dbHelper;
    private StudentAdapter studentAdapter;
    private PaymentAdapter paymentAdapter;
    private TabLayout tabLayout;
    private TextView tvTotalStudents, tvTotalPayments;
    private NumberFormat currencyFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        dbHelper = new DatabaseHelper(this);

        // Initialize currency formatter
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));

        // Initialize views with new IDs and types
        btnManageStudents = findViewById(R.id.btnManageStudents);
        btnManagePayments = findViewById(R.id.btnManagePayments);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnLogout = findViewById(R.id.btnLogout); // New logout button
        rvStudents = findViewById(R.id.rvStudents);
        rvPayments = findViewById(R.id.rvPayments);
        tabLayout = findViewById(R.id.tabLayout);
        tvTotalStudents = findViewById(R.id.tvTotalStudents);
        tvTotalPayments = findViewById(R.id.tvTotalPayments);

        setupRecyclerViews();
        loadData();
        setupTabLayout();
        setupClickListeners();
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

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // Students tab
                        rvStudents.setVisibility(android.view.View.VISIBLE);
                        rvPayments.setVisibility(android.view.View.GONE);
                        break;
                    case 1: // Payments tab
                        rvStudents.setVisibility(android.view.View.GONE);
                        rvPayments.setVisibility(android.view.View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupClickListeners() {
        // These CardViews now act as quick action buttons
        btnManageStudents.setOnClickListener(v -> {
            // Switch to Students tab
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            if (tab != null) {
                tab.select();
            }
        });

        btnManagePayments.setOnClickListener(v -> {
            // Switch to Payments tab
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            if (tab != null) {
                tab.select();
            }
        });

        btnAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStudentActivity.class);
            startActivity(intent);
        });

        // Logout button click listener
        btnLogout.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performLogout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void performLogout() {
        // Clear any session data if you have
        // dbHelper.clearSessionData(); // Uncomment if you have session management

        // Navigate back to main activity (login screen)
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void loadData() {
        // Load students data
        List<Student> students = dbHelper.getAllStudents();
        studentAdapter.setStudents(students);

        // Load payments data
        List<Payment> payments = dbHelper.getAllPayments();
        paymentAdapter.setPayments(payments);

        // Calculate total payment amount
        double totalAmount = calculateTotalPaymentAmount(payments);

        // Update stats
        updateStats(students.size(), totalAmount);
    }

    private double calculateTotalPaymentAmount(List<Payment> payments) {
        double total = 0.0;
        for (Payment payment : payments) {
            total += payment.getAmount(); // Assuming Payment model has getAmount() method
        }
        return total;
    }

    private void updateStats(int studentCount, double totalPaymentAmount) {
        tvTotalStudents.setText(String.valueOf(studentCount));
        tvTotalPayments.setText(currencyFormat.format(totalPaymentAmount));
    }
}