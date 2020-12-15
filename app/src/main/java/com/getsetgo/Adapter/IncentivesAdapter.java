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

public class IncentivesAdapter extends RecyclerView.Adapter<IncentivesAdapter.IncentivesViewHolder> {

    Context context;

    public IncentivesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public IncentivesAdapter.IncentivesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_incentives_row, parent, false);
        return new IncentivesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncentivesAdapter.IncentivesViewHolder holder, int position) {

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

    public class IncentivesViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate,txtAmountNp,txtNP,txtStatusDate, txtStatus, txtAmount;

        public IncentivesViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAmount = itemView.findViewById(R.id.txtIncAmount);
            txtDate = itemView.findViewById(R.id.txtIncDate);
            txtAmountNp = itemView.findViewById(R.id.txtIncAmountNP);
            txtNP = itemView.findViewById(R.id.txtIncNP);
            txtStatus = itemView.findViewById(R.id.txtStatusTitle);
            txtStatusDate = itemView.findViewById(R.id.txtStatusDate);

        }
    }
}

