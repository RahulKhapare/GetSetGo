package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.R;

public class StudentsFeedbackAdapter extends RecyclerView.Adapter<StudentsFeedbackAdapter.StudentsFeedbackViewHolder> {

    Context context;
    int count;

    public StudentsFeedbackAdapter(Context context,int count) {
        this.context = context;
        this.count = count;

    }

    @NonNull
    @Override
    public StudentsFeedbackAdapter.StudentsFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_students_feedback, parent, false);
        return new StudentsFeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsFeedbackAdapter.StudentsFeedbackViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class StudentsFeedbackViewHolder extends RecyclerView.ViewHolder {

        TextView txtFeedback,txtDate;
        public StudentsFeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFeedback = itemView.findViewById(R.id.txtFedback);
            txtDate = itemView.findViewById(R.id.txtFeedbackDate);


        }
    }

}

