package com.rakib.studentportal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rakib.studentportal.model.Payment;

import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentViewHolder> {
    private List<Payment> payments;

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = payments.get(position);
        holder.text1.setText(payment.getType() + " - $" + payment.getAmount());
        holder.text2.setText("Status: " + payment.getStatus() + " - Date: " + payment.getDate());
    }

    @Override
    public int getItemCount() {
        return payments == null ? 0 : payments.size();
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