package com.getsetgo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.R;
import com.getsetgo.activity.TransactionHistoryDetails;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

public class AllTransactionsAdapter extends RecyclerView.Adapter<AllTransactionsAdapter.AllTransactionsViewHolder> {

    Context context;

    public AllTransactionsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AllTransactionsAdapter.AllTransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_transaction_row, parent, false);
        return new AllTransactionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllTransactionsAdapter.AllTransactionsViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TransactionHistoryDetails.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class AllTransactionsViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtMode, txtAmount;

        public AllTransactionsViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtTransDate);
            txtMode = itemView.findViewById(R.id.txtTransMode);
            txtAmount = itemView.findViewById(R.id.txtTransactionAmount);

        }
    }
}

