package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.R;

public class MyEarningsCommonAdapter extends RecyclerView.Adapter<MyEarningsCommonAdapter.MyEarningsCommonViewHolder> {

    Context context;

    public MyEarningsCommonAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyEarningsCommonAdapter.MyEarningsCommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_myearnings_row, parent, false);
        return new MyEarningsCommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEarningsCommonAdapter.MyEarningsCommonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class MyEarningsCommonViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate,txtAmount;

        public MyEarningsCommonViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAmount = itemView.findViewById(R.id.txtEarnAmount);
            txtDate = itemView.findViewById(R.id.txtEarnDate);

        }
    }
}

