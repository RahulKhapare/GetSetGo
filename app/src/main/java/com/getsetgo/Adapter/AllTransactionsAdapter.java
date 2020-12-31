package com.getsetgo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.Fragment.TransactionsHistoryDetailsFragment;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.util.Utilities;


public class AllTransactionsAdapter extends RecyclerView.Adapter<AllTransactionsAdapter.AllTransactionsViewHolder> {

    Context context;
    JsonList jsonList;

    public AllTransactionsAdapter(Context context,JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public AllTransactionsAdapter.AllTransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_transaction_row, parent, false);
        return new AllTransactionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllTransactionsAdapter.AllTransactionsViewHolder holder, int position) {

        Json json = jsonList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String dataJson = json.toString();
                bundle.putString("jsonObj",dataJson);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
                TransactionsHistoryDetailsFragment myFragment = new TransactionsHistoryDetailsFragment();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, myFragment)
                        .addToBackStack(null).commit();
            }
        });
        holder.txtAmount.setText(json.getString("amount"));
        holder.txtDate.setText(Utilities.getShortMonthNames(json.getString("create_date_text")));
        holder.txtMode.setText(json.getString("income_type"));
    }

    @Override
    public int getItemCount() {
        return jsonList.size();
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

