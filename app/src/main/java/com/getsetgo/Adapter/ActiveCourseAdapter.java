package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.R;
import com.sasank.roundedhorizontalprogress.ProgressBarAnimation;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

public class ActiveCourseAdapter extends RecyclerView.Adapter<ActiveCourseAdapter.ActiveCourseViewHolder> {

    Context context;

    public ActiveCourseAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ActiveCourseAdapter.ActiveCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_active_course, parent, false);
        return new  ActiveCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveCourseAdapter.ActiveCourseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ActiveCourseViewHolder extends RecyclerView.ViewHolder{

        TextView txtStatus,txtActiveProgram,txtCountStudents,txtTech;
        ImageView imvActiveCourse;
        RoundedHorizontalProgressBar progressActiveCourse;


        public ActiveCourseViewHolder(@NonNull View itemView) {
            super(itemView);

            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtActiveProgram = itemView.findViewById(R.id.txtActiveProgram);
            txtCountStudents = itemView.findViewById(R.id.txtCountStudents);
            txtTech = itemView.findViewById(R.id.textTech);
            imvActiveCourse = itemView.findViewById(R.id.imvActiveCourse);
            progressActiveCourse = itemView.findViewById(R.id.progressActiveCourse);
        }
    }
}

