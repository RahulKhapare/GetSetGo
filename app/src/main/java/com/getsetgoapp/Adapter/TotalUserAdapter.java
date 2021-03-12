package com.getsetgoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.R;

public class TotalUserAdapter extends RecyclerView.Adapter<TotalUserAdapter.TotalUserViewHolder> {

    Context context;
    JsonList jsonList;
    int lastCheckPosition = -1;

    public TotalUserAdapter(Context context,JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public TotalUserAdapter.TotalUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_totaluser_row, parent, false);
        return new TotalUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TotalUserAdapter.TotalUserViewHolder holder, int position) {

        Json json = jsonList.get(position);

        holder.txtName.setText(json.getString("name") +" " + json.getString("lastname"));
        holder.txtDate.setText(json.getString("update_timestamp"));
        holder.txtPhone.setText(json.getString("contact"));
        holder.txtEmail.setText(json.getString("email"));
        holder.txtParent.setText(json.getString("franchise_parent"));
        holder.txtPEmail.setText(json.getString("fp_email"));
        holder.txtPPhone.setText(json.getString("fp_contact"));
        holder.txtAddDate.setText(json.getString("add_timestamp"));
        holder.txtStatus.setText(json.getString("status"));

        holder.rlyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                lastCheckPosition = holder.getAdapterPosition();
//                notifyItemRangeChanged(0,jsonList.size());
                if (holder.rlDetails.getVisibility()==View.VISIBLE){
                    holder.rlDetails.setVisibility(View.GONE);
                    holder.chkShowHide.setChecked(false);
                }else {
                    holder.rlDetails.setVisibility(View.VISIBLE);
                    holder.chkShowHide.setChecked(true);
                }
            }
        });

//        if (lastCheckPosition==position){
//            holder.rlDetails.setVisibility(View.VISIBLE);
//            holder.chkShowHide.setChecked(true);
//        }else {
//            holder.rlDetails.setVisibility(View.GONE);
//            holder.chkShowHide.setChecked(false);
//        }
    }

    @Override
    public int getItemCount() {
        return jsonList.size();
    }

    public class TotalUserViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate,txtName,txtColor,txtUserAddDate,txtAddDate,
        txtPhone,txtEmail,txtParent,txtPEmail,txtPPhone,txtStatus;
        CheckBox chkShowHide;
        RelativeLayout rlDetails,rlyView;

        public TotalUserViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtUserName);
            txtColor = itemView.findViewById(R.id.txtUserColor);
            txtUserAddDate = itemView.findViewById(R.id.txtUserAddDate);
            txtDate = itemView.findViewById(R.id.txtUserDate);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtParent = itemView.findViewById(R.id.txtParent);
            txtPEmail = itemView.findViewById(R.id.txtPEmail);
            txtPPhone = itemView.findViewById(R.id.txtPPhone);
            txtAddDate = itemView.findViewById(R.id.txtAddDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            chkShowHide = itemView.findViewById(R.id.chcShowHide);
            rlDetails = itemView.findViewById(R.id.rlDetails);
            rlyView = itemView.findViewById(R.id.rlyView);

        }
    }
}

