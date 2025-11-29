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
import com.rakib.studentportal.model.Payment;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private Context context;
    private List<Payment> payments;
    private DatabaseHelper dbHelper;

    public PaymentAdapter(Context context, DatabaseHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = payments.get(position);
        holder.text1.setText(payment.getStudentName() + " - " + payment.getType() + " - $" + payment.getAmount());
        holder.text2.setText("Status: " + payment.getStatus() + " - Date: " + payment.getDate());

        holder.itemView.setOnClickListener(v -> showPaymentOptions(payment));
    }

    @Override
    public int getItemCount() {
        return payments == null ? 0 : payments.size();
    }

    private void showPaymentOptions(Payment payment) {
        if ("Pending".equals(payment.getStatus())) {
            new AlertDialog.Builder(context)
                    .setTitle("Payment Options")
                    .setMessage("Accept payment from " + payment.getStudentName() + "?")
                    .setPositiveButton("Accept", (dialog, which) -> acceptPayment(payment))
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void acceptPayment(Payment payment) {
        boolean success = dbHelper.updatePaymentStatus(payment.getPaymentId(), "Accepted");
        if (success) {
            payment.setStatus("Accepted");
            notifyDataSetChanged();
            Toast.makeText(context, "Payment accepted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to accept payment", Toast.LENGTH_SHORT).show();
        }
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}