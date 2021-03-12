package com.getsetgoapp.Adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.Fragment.MyCourseDetailFragment;
import com.getsetgoapp.R;
import com.getsetgoapp.util.CheckConnection;
import com.getsetgoapp.util.Config;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.MyCourseViewHolder> {

    Context context;
    JsonList activeCourseJsonList;
    MyCourseDetailFragment courseDetailFragment;
    Bundle bundle = new Bundle();

    public MyCourseAdapter(Context context, JsonList activeCourseJsonList) {
        this.context = context;
        this.activeCourseJsonList = activeCourseJsonList;
    }

    @NonNull
    @Override
    public MyCourseAdapter.MyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_my_course, parent, false);
        return new  MyCourseViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyCourseAdapter.MyCourseViewHolder holder, int position) {

        Json json = activeCourseJsonList.get(position);

        try {
            holder.txtCourseDes.setText(json.getString("course_name"));
            if (!TextUtils.isEmpty(json.getString("image"))){
                Picasso.get().load(json.getString("image")).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.ivCourse);
            }else {
                Picasso.get().load(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.ivCourse);
            }
            holder.progressCourse.setProgress(Integer.parseInt(json.getString("completion_percent")), true);
            holder.txtCourseTech.setText(json.getString("category_name"));
            holder.txtCourseStatus.setText(json.getString("completion_percent") + "% Complete");


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String courseSlug = json.getString("slug");
                    String courseName = json.getString("course_name");
                    if (CheckConnection.isVailable(context)){
                        loadFragment(view,courseSlug,courseName);
                    }else {
                        H.showMessage(context,"No internet connection available");
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return activeCourseJsonList.size();
    }

    public class MyCourseViewHolder extends RecyclerView.ViewHolder{

        TextView txtCourseDes,txtCourseProgramme,txtCourseStatus,txtCourseTech;
        RoundedImageView ivCourse;
        ProgressBar progressCourse;


        public MyCourseViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCourseDes = itemView.findViewById(R.id.txtCourseDes);
            txtCourseProgramme = itemView.findViewById(R.id.txtCourseProgramme);
            txtCourseStatus = itemView.findViewById(R.id.txtCourseStatus);
            txtCourseTech = itemView.findViewById(R.id.txtCourseTech);
            ivCourse = itemView.findViewById(R.id.ivMyCourse);
            progressCourse = itemView.findViewById(R.id.progress_barCourse);
        }
    }

    private void loadFragment(View v, String courseSlug,String courseName) {
        Config.myCourseSlug = courseSlug;
        Config.myCourseTitle = courseName;
//        string = P.baseUrl + "series_check/" + json.getString(P.series_slug) + "/" + json.getString(P.video_slug);
//        it = json.getInt(P.time);
//        it *= 1000;
//        bundle.putString(P.url, string);
//        bundle.putInt("videoProgress", it);
        bundle.putString("slug",courseSlug);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        if (courseDetailFragment == null)
            courseDetailFragment = new MyCourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}

