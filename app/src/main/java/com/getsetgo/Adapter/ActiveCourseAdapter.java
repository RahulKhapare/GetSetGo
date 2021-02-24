package com.getsetgo.Adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.getsetgo.Fragment.CourseDetailFragment;
import com.getsetgo.Fragment.MyCourseDetailFragment;
import com.getsetgo.Fragment.ViewAllCategoriesFragment;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.util.CheckConnection;
import com.getsetgo.util.Config;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class ActiveCourseAdapter extends RecyclerView.Adapter<ActiveCourseAdapter.ActiveCourseViewHolder> {

    Context context;
    JsonList jsonList;
    MyCourseDetailFragment courseDetailFragment;
    Bundle bundle = new Bundle();

    public ActiveCourseAdapter(Context context, JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public ActiveCourseAdapter.ActiveCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_active_course, parent, false);
        return new ActiveCourseViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ActiveCourseAdapter.ActiveCourseViewHolder holder, int position) {

        final Json json = jsonList.get(position);

        try {
            holder.txtCourseName.setText(json.getString("course_name"));
            Picasso.get().load(json.getString("image")).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.imvActiveCourse);
            holder.progressActiveCourse.setProgress(Integer.parseInt(json.getString("completion_percent")), true);
            holder.txtTech.setText(json.getString("category_name"));
            holder.txtStatus.setText(json.getString("completion_percent") + "% Complete");

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
        return jsonList.size();
    }

    public class ActiveCourseViewHolder extends RecyclerView.ViewHolder {

        TextView txtStatus, txtActiveProgram, txtCountStudents, txtTech, txtCourseName;
        RoundedImageView imvActiveCourse;
        ProgressBar progressActiveCourse;


        public ActiveCourseViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCourseName = itemView.findViewById(R.id.txtCourseName);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtActiveProgram = itemView.findViewById(R.id.txtActiveProgram);
            txtCountStudents = itemView.findViewById(R.id.txtCountStudents);
            txtTech = itemView.findViewById(R.id.txtTech);
            imvActiveCourse = itemView.findViewById(R.id.imvActiveCourse);
            progressActiveCourse = itemView.findViewById(R.id.progressActiveCourse);
        }
    }

    /*private void loadFragment(View v, String courseSlug) {
//        string = P.baseUrl + "series_check/" + json.getString(P.series_slug) + "/" + json.getString(P.video_slug);
//        it = json.getInt(P.time);
//        it *= 1000;
//        bundle.putString(P.url, string);
//        bundle.putInt("videoProgress", it);
        bundle.putString("slug",courseSlug);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        if (courseDetailFragment == null)
            courseDetailFragment = new CourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, courseDetailFragment)
                .addToBackStack("")
                .commit();
    }*/

//    private void loadFragment(View v, String title) {
//        Bundle bundle = new Bundle();
//        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
//        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
//        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
//        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
//        bundle.putString("slug", title);
//        bundle.putBoolean("isFromHome", true);
//        AppCompatActivity activity = (AppCompatActivity) v.getContext();
//        if (courseDetailFragment == null)
//            courseDetailFragment = new CourseDetailFragment();
//
//        courseDetailFragment.setArguments(bundle);
//        activity.getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, courseDetailFragment)
//                .addToBackStack(null)
//                .commit();
//    }

    private void loadFragment(View v, String courseSlug,String courseName) {
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        Config.myCourseSlug = courseSlug;
        Config.myCourseTitle = courseName;
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

