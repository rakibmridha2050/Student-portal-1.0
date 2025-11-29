package com.rakib.studentportal.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rakib.studentportal.DatabaseHelper;
import com.rakib.studentportal.R;
import com.rakib.studentportal.adapter.PaymentHistoryAdapter;
import com.rakib.studentportal.model.Payment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentDashboardActivity extends AppCompatActivity {
    private Button btnMakePayment, btnLogout;
    private RecyclerView rvPaymentHistory;
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

        // Initialize views
        btnMakePayment = findViewById(R.id.btnMakePayment);
        btnLogout = findViewById(R.id.btnLogout);
        rvPaymentHistory = findViewById(R.id.rvPaymentHistory);

        setupRecyclerView();
        loadPaymentHistory();

        btnMakePayment.setOnClickListener(v -> showPaymentDialog());
        btnLogout.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        rvPaymentHistory.setLayoutManager(new LinearLayoutManager(this));
        paymentHistoryAdapter = new PaymentHistoryAdapter();
        rvPaymentHistory.setAdapter(paymentHistoryAdapter);
    }

    private void loadPaymentHistory() {
        List<Payment> payments = dbHelper.getStudentPayments(studentId);
        paymentHistoryAdapter.setPayments(payments);
    }

    private void showPaymentDialog() {
        String[] paymentTypes = {"Monthly Fees", "Exam Fees"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Select Payment Type")
                .setItems(paymentTypes, (dialog, which) -> {
                    String type = paymentTypes[which];
                    showAmountDialog(type);
                })
                .show();
    }

    private void showAmountDialog(String type) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Enter Amount for " + type);

        final android.widget.EditText input = new android.widget.EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("Pay", (dialog, which) -> {
            String amountStr = input.getText().toString();
            if (!amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                makePayment(type, amount);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void makePayment(String type, double amount) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        boolean success = dbHelper.makePayment(studentId, amount, type, currentDate);
        if (success) {
            Toast.makeText(this, "Payment submitted successfully", Toast.LENGTH_SHORT).show();
            loadPaymentHistory();
        } else {
            Toast.makeText(this, "Failed to submit payment", Toast.LENGTH_SHORT).show();
        }
    }
}