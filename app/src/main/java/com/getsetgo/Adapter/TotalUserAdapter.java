package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.R;

public class TotalUserAdapter extends RecyclerView.Adapter<TotalUserAdapter.TotalUserViewHolder> {

    Context context;

    public TotalUserAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TotalUserAdapter.TotalUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_totaluser_row, parent, false);
        return new TotalUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TotalUserAdapter.TotalUserViewHolder holder, int position) {


        holder.chkShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.rlDetails.getVisibility() == View.VISIBLE){
                    holder.rlDetails.setVisibility(View.GONE);
                }else{
                    holder.rlDetails.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class TotalUserViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate,txtName,txtColor,txtAddDate;
        CheckBox chkShowHide;
        RelativeLayout rlDetails;

        public TotalUserViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtUserName);
            txtColor = itemView.findViewById(R.id.txtUserColor);
            txtAddDate = itemView.findViewById(R.id.txtUserAddDate);
            txtDate = itemView.findViewById(R.id.txtUserDate);
            chkShowHide = itemView.findViewById(R.id.chcShowHide);
         /*   txtStatus = itemView.findViewById(R.id.txtStatusTitle);
            txtStatusDate = itemView.findViewById(R.id.txtStatusDate);
            txtTransactionId = itemView.findViewById(R.id.txtTransactionId);
            txtApproveDate = itemView.findViewById(R.id.txtApproveDate);
            txtSlipDetails = itemView.findViewById(R.id.txtSlipDetails);
            txtRemark = itemView.findViewById(R.id.txtRemark);*/
            rlDetails = itemView.findViewById(R.id.rlDetails);

        }
    }
}

