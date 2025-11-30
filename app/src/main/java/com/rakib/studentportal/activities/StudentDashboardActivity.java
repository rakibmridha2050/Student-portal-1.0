package com.rakib.studentportal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.rakib.studentportal.DatabaseHelper;
import com.rakib.studentportal.R;
import com.rakib.studentportal.adapter.PaymentHistoryAdapter;
import com.rakib.studentportal.model.Payment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentDashboardActivity extends AppCompatActivity {
    private MaterialButton btnMakePayment, btnLogout, btnFilter;
    private RecyclerView rvPaymentHistory;
    private View emptyState;
    private TextView tvTotalPayments, tvBalance;
    private DatabaseHelper dbHelper;
    private int studentId;
    private PaymentHistoryAdapter paymentHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        dbHelper = new DatabaseHelper(this);
        studentId = getIntent().getIntExtra("STUDENT_ID", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Error: Student not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupRecyclerView();
        loadPaymentHistory();
        updateStats();

        setupClickListeners();
    }

    private void initializeViews() {
        btnMakePayment = findViewById(R.id.btnMakePayment);
        btnLogout = findViewById(R.id.btnLogout);
        btnFilter = findViewById(R.id.btnFilter);
        rvPaymentHistory = findViewById(R.id.rvPaymentHistory);
        emptyState = findViewById(R.id.emptyState);
        tvTotalPayments = findViewById(R.id.tvTotalPayments);
        tvBalance = findViewById(R.id.tvBalance);
    }

    private void setupRecyclerView() {
        rvPaymentHistory.setLayoutManager(new LinearLayoutManager(this));
        paymentHistoryAdapter = new PaymentHistoryAdapter();
        rvPaymentHistory.setAdapter(paymentHistoryAdapter);
    }

    private void setupClickListeners() {
        btnMakePayment.setOnClickListener(v -> showPaymentDialog());
        btnLogout.setOnClickListener(v -> logout());
        btnFilter.setOnClickListener(v -> showFilterDialog());
    }

    private void loadPaymentHistory() {
        List<Payment> payments = dbHelper.getStudentPayments(studentId);
        paymentHistoryAdapter.setPayments(payments);

        // Show empty state if no payments
        if (payments.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvPaymentHistory.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvPaymentHistory.setVisibility(View.VISIBLE);
        }
    }

    private void updateStats() {
        List<Payment> payments = dbHelper.getStudentPayments(studentId);

        // Update total payments count
        tvTotalPayments.setText(String.valueOf(payments.size()));

        // Calculate total balance (sum of all payments)
        double totalBalance = 0;
        for (Payment payment : payments) {
            totalBalance += payment.getAmount();
        }
        tvBalance.setText(String.format("₹%.2f", totalBalance));
    }

    private void showPaymentDialog() {
        String[] paymentTypes = {"Monthly Fees", "Exam Fees", "Library Fees", "Transport Fees"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Select Payment Type")
                .setItems(paymentTypes, (dialog, which) -> {
                    String type = paymentTypes[which];
                    showAmountDialog(type);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showAmountDialog(String type) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Enter Amount for " + type);

        final android.widget.EditText input = new android.widget.EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Enter amount in ₹");

        // Set some default amounts based on type
        switch (type) {
            case "Monthly Fees":
                input.setText("5000");
                break;
            case "Exam Fees":
                input.setText("1000");
                break;
            case "Library Fees":
                input.setText("500");
                break;
            case "Transport Fees":
                input.setText("2000");
                break;
        }

        builder.setView(input);

        builder.setPositiveButton("Pay Now", (dialog, which) -> {
            String amountStr = input.getText().toString();
            if (!amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                if (amount > 0) {
                    makePayment(type, amount);
                } else {
                    Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showFilterDialog() {
        String[] filterOptions = {"All Payments", "This Month", "Last 3 Months", "This Year"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Filter Payments")
                .setItems(filterOptions, (dialog, which) -> {
                    // Implement filtering logic here
                    Toast.makeText(this, "Filter: " + filterOptions[which], Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void makePayment(String type, double amount) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        boolean success = dbHelper.makePayment(studentId, amount, type, currentDate);
        if (success) {
            Toast.makeText(this, "✅ Payment submitted successfully!", Toast.LENGTH_SHORT).show();
            loadPaymentHistory();
            updateStats();
        } else {
            Toast.makeText(this, "❌ Failed to submit payment", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPaymentHistory();
        updateStats();
    }
}