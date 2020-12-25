package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.MyCourseViewHolder> {

    Context context;

    public MyCourseAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyCourseAdapter.MyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_my_course, parent, false);
        return new  MyCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCourseAdapter.MyCourseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyCourseViewHolder extends RecyclerView.ViewHolder{

        TextView txtCourseDes,txtCourseProgramme,txtCourseStatus,txtCourseTech;
        RoundedImageView imvCourse;
        ProgressBar progressCourse;


        public MyCourseViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCourseDes = itemView.findViewById(R.id.txtCourseDes);
            txtCourseProgramme = itemView.findViewById(R.id.txtCourseProgramme);
            txtCourseStatus = itemView.findViewById(R.id.txtCourseStatus);
            txtCourseTech = itemView.findViewById(R.id.txtCourseTech);
            imvCourse = itemView.findViewById(R.id.imvMyCourse);
            progressCourse = itemView.findViewById(R.id.progress_barCourse);
        }
    }
}

