package com.getsetgoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.Fragment.TransactionsHistoryDetailsFragment;
import com.getsetgoapp.R;
import com.getsetgoapp.util.Utilities;


public class CrashTransactionsAdapter extends RecyclerView.Adapter<CrashTransactionsAdapter.CrashTransactionsViewHolder> {

    Context context;
    JsonList jsonList;

    public CrashTransactionsAdapter(Context context, JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public CrashTransactionsAdapter.CrashTransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_transaction_row, parent, false);
        return new CrashTransactionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrashTransactionsAdapter.CrashTransactionsViewHolder holder, int position) {

        Json json = jsonList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String dataJson = json.toString();
                bundle.putString("jsonObj",dataJson);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
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
//        holder.txtMode.setText(json.getString("income_type"));

        switch (json.getString("income_type")) {

            case "RF":
                holder.txtMode.setText(context.getString(R.string.referral_income));
                break;
            case "T":
                holder.txtMode.setText(context.getString(R.string.transfer));
                break;
            case "MF":
                holder.txtMode.setText("MF");
                break;
            default:
                holder.txtMode.setText("RF");

        }
    }

    @Override
    public int getItemCount() {
        return jsonList.size();
    }

    public class CrashTransactionsViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtMode, txtAmount;

        public CrashTransactionsViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtTransDate);
            txtMode = itemView.findViewById(R.id.txtTransMode);
            txtAmount = itemView.findViewById(R.id.txtTransactionAmount);

        }
    }

}

