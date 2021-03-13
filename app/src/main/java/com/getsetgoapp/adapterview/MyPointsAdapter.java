package com.getsetgoapp.adapterview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgoapp.Fragment.TransactionsHistoryDetailsFragment;
import com.getsetgoapp.Model.MyPointsModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;
import com.getsetgoapp.util.Utilities;

import java.util.List;

public class MyPointsAdapter extends RecyclerView.Adapter<MyPointsAdapter.ViewHolder> {

    Context context;
    List<MyPointsModel> myPointsModelList;

    public MyPointsAdapter(Context context, List<MyPointsModel> myPointsModelList) {
        this.context = context;
        this.myPointsModelList = myPointsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_transaction_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyPointsModel model = myPointsModelList.get(position);

        holder.txtTransactionAmount.setText(model.getAmount());
        holder.txtTransDate.setText(Utilities.getShortMonthNames(model.getCreate_date_text()));
        switch (model.getIncome_type()) {

            case "RF":
                holder.txtTransMode.setText(context.getString(R.string.referral_income));
                break;
            case "T":
                holder.txtTransMode.setText(context.getString(R.string.transfer));
                break;
            case "MF":
                holder.txtTransMode.setText("MF");
                break;
            default:
                holder.txtTransMode.setText("RF");

        }

        holder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

                Config.MY_POINTS = true;
                Bundle bundle = new Bundle();
                String dataJson = model.getJson().toString();
                bundle.putString("jsonObj", dataJson);

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

    }

    @Override
    public int getItemCount() {
        return myPointsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlMain;
        TextView txtTransDate;
        TextView txtTransMode;
        TextView txtTransactionAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rlMain = itemView.findViewById(R.id.rlMain);
            txtTransDate = itemView.findViewById(R.id.txtTransDate);
            txtTransMode = itemView.findViewById(R.id.txtTransMode);
            txtTransactionAmount = itemView.findViewById(R.id.txtTransactionAmount);
        }
    }
}
