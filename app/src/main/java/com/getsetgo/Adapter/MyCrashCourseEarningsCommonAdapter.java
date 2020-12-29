package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.R;
import com.getsetgo.util.P;

public class MyCrashCourseEarningsCommonAdapter extends RecyclerView.Adapter<MyCrashCourseEarningsCommonAdapter.MyCrashCourseEarningsViewHolder> {

    Context context;
    JsonList jsonList;

    public MyCrashCourseEarningsCommonAdapter(Context context, JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public MyCrashCourseEarningsCommonAdapter.MyCrashCourseEarningsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_crash_course_earnings_row, parent, false);
        return new MyCrashCourseEarningsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCrashCourseEarningsCommonAdapter.MyCrashCourseEarningsViewHolder holder, int position) {
        Json json = jsonList.get(position);
        holder.txtAmount.setText(json.getString(P.event_income));
        holder.txtDate.setText(json.getString(P.date));
    }

    @Override
    public int getItemCount() {
        return jsonList.size();
    }

    public class MyCrashCourseEarningsViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtAmount;

        public MyCrashCourseEarningsViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAmount = itemView.findViewById(R.id.txtEarnAmount);
            txtDate = itemView.findViewById(R.id.txtEarnDate);

        }
    }
}

