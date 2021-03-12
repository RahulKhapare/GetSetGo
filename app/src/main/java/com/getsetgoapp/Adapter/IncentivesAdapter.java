package com.getsetgoapp.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.R;
import com.getsetgoapp.util.Utilities;

public class IncentivesAdapter extends RecyclerView.Adapter<IncentivesAdapter.IncentivesViewHolder> {

    Context context;
    JsonList jsonList;

    public IncentivesAdapter(Context context, JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public IncentivesAdapter.IncentivesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_incentives_row, parent, false);
        return new IncentivesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncentivesAdapter.IncentivesViewHolder holder, int position) {


        Json json = jsonList.get(position);
        holder.txtAmount.setText(json.getString("amount"));
        holder.txtAmountNp.setText(json.getString("recive_amount"));
        holder.txtTDSAmount.setText(json.getString("tds_amount"));
        holder.txtTransactionId.setText("Transaction Id - " +json.getString("transaction_id") );
        holder.txtRemark.setText("Remark - " + json.getString("remark"));
        String date = Utilities.getShortMonthNames(json.getString("date"));
        holder.txtDate.setText(date);
        holder.txtNetPayAmount.setText(json.getString("recive_amount"));
        String updateDate = json.getString("update_date") != null ? json.getString("update_date") : "";
        String dateValue = Utilities.getShortMonthNames(updateDate);
        if (!TextUtils.isEmpty(dateValue) && !dateValue.equals("null")){
            holder.txtStatusDate.setText(dateValue);
        }else {
            holder.txtStatusDate.setText("");
        }
        holder.txtApproveDate.setText("Approved Date - "+ Utilities.getShortMonthNames(json.getString("date")));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rlDetails.getVisibility() == View.VISIBLE) {
                    holder.rlDetails.setVisibility(View.GONE);
                } else {
                    holder.rlDetails.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return jsonList.size();
    }

    public class IncentivesViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtAmountNp, txtNP, txtStatusDate, txtStatus,txtTDSAmount,
                txtAmount, txtTransactionId, txtNetPayAmount,txtApproveDate, txtSlipDetails, txtRemark;
        RelativeLayout rlDetails;

        public IncentivesViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAmount = itemView.findViewById(R.id.txtIncAmount);
            txtDate = itemView.findViewById(R.id.txtIncDate);
            txtTDSAmount = itemView.findViewById(R.id.txtTDSAmount);
            txtAmountNp = itemView.findViewById(R.id.txtIncAmountNP);
            txtNP = itemView.findViewById(R.id.txtIncNP);
            txtNetPayAmount = itemView.findViewById(R.id.txtNetPayAmount);
            txtStatus = itemView.findViewById(R.id.txtStatusTitle);
            txtStatusDate = itemView.findViewById(R.id.txtStatusDate);
            txtTransactionId = itemView.findViewById(R.id.txtTransactionId);
            txtApproveDate = itemView.findViewById(R.id.txtApproveDate);
            txtSlipDetails = itemView.findViewById(R.id.txtSlipDetails);
            txtRemark = itemView.findViewById(R.id.txtRemark);
            rlDetails = itemView.findViewById(R.id.rlDetails);

        }
    }
}

