package com.getsetgoapp.adapterview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.getsetgoapp.Fragment.MyPointsFragment;
import com.getsetgoapp.Model.MyPointsModel;
import com.getsetgoapp.R;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Utilities;

import java.util.List;

public class MyPointsAdapter extends RecyclerView.Adapter<MyPointsAdapter.ViewHolder> {

    Context context;
    List<MyPointsModel> myPointsModelList;
    MyPointsFragment fragment;

    public interface onClick{
        void showData(Json json);
    }

    public MyPointsAdapter(Context context, List<MyPointsModel> myPointsModelList, MyPointsFragment fragment) {
        this.context = context;
        this.myPointsModelList = myPointsModelList;
        this.fragment = fragment;
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

        holder.txtTransactionAmount.setText(model.getPoints());
        holder.txtTransDate.setText(Utilities.getShortMonthNames(model.getCreate_date_text()));
        holder.txtTransMode.setText(model.getAction_type());

        holder.lrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((MyPointsFragment)fragment).showData(model.getJson());
            }
        });

    }

    @Override
    public int getItemCount() {
        return myPointsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout lrView;
        TextView txtTransDate;
        TextView txtTransMode;
        TextView txtTransactionAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lrView = itemView.findViewById(R.id.lrView);
            txtTransDate = itemView.findViewById(R.id.txtTransDate);
            txtTransMode = itemView.findViewById(R.id.txtTransMode);
            txtTransactionAmount = itemView.findViewById(R.id.txtTransactionAmount);
        }
    }
}
