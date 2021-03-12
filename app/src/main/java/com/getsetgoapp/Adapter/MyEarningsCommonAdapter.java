package com.getsetgoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.R;
import com.getsetgoapp.util.P;

public class MyEarningsCommonAdapter extends RecyclerView.Adapter<MyEarningsCommonAdapter.MyEarningsCommonViewHolder> {

    Context context;
    JsonList jsonList;

    public MyEarningsCommonAdapter(Context context, JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public MyEarningsCommonAdapter.MyEarningsCommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_myearnings_row, parent, false);
        return new MyEarningsCommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEarningsCommonAdapter.MyEarningsCommonViewHolder holder, int position) {


        Json json = jsonList.get(position);
        holder.txtAmount.setText(json.getString(P.referral_income));
        holder.txtDate.setText(json.getString("date"));


    }

    @Override
    public int getItemCount() {
        return jsonList.size();
    }

    public class MyEarningsCommonViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtAmount;

        public MyEarningsCommonViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAmount = itemView.findViewById(R.id.txtEarnAmount);
            txtDate = itemView.findViewById(R.id.txtEarnDate);

        }
    }
}

