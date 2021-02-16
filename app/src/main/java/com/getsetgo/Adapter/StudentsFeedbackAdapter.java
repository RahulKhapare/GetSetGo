package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.R;
import com.squareup.picasso.Picasso;

public class StudentsFeedbackAdapter extends RecyclerView.Adapter<StudentsFeedbackAdapter.StudentsFeedbackViewHolder> {

    Context context;
    int count;
    JsonList reviewList;

    public StudentsFeedbackAdapter(Context context, JsonList reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public StudentsFeedbackAdapter.StudentsFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_students_feedback, parent, false);
        return new StudentsFeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsFeedbackAdapter.StudentsFeedbackViewHolder holder, int position) {

        Json list = reviewList.get(position);

        try {
            holder.txtFeedback.setText(list.getJsonArray("course_testimonials").getJSONObject(position).getString("testimonial").trim());
            holder.txtName.setText(list.getJsonArray("course_testimonials").getJSONObject(position).getString("name").trim() + " " + list.getJsonArray("course_testimonials").getJSONObject(position).getString("lastname").trim());
//            holder.txtDesignation.setText(list.getJsonArray("course_testimonials").getJSONObject(position).getString("designation"));
            if (list.getJsonArray("course_testimonials").getJSONObject(position).getString("image") == null) {

                holder.ivProfile.setVisibility(View.GONE);

            } else {
                holder.ivProfile.setVisibility(View.VISIBLE);

                Picasso.get().load(list.getJsonArray("course_testimonials").getJSONObject(position).getString("image"))
                        .placeholder(R.drawable.ic_wp).error(R.drawable.ic_wp).into(holder.ivProfile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class StudentsFeedbackViewHolder extends RecyclerView.ViewHolder {

        TextView txtFeedback, txtDesignation, txtName;
        ImageView ivProfile;

        public StudentsFeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFeedback = itemView.findViewById(R.id.txtFedback);
            txtDesignation = itemView.findViewById(R.id.txtDesignation);
            txtName = itemView.findViewById(R.id.txtName);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }
    }
}

