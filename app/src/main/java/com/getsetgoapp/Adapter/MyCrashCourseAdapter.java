package com.getsetgoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.Fragment.MyCourseDetailFragment;
import com.getsetgoapp.Fragment.MyCrashCourseDetailFragment;
import com.getsetgoapp.R;
import com.getsetgoapp.util.CheckConnection;
import com.getsetgoapp.util.Config;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class MyCrashCourseAdapter extends RecyclerView.Adapter<MyCrashCourseAdapter.MyCourseViewHolder> {

    Context context;
    JsonList activeCourseJsonList;
    MyCrashCourseDetailFragment courseDetailFragment;
    Bundle bundle = new Bundle();

    public MyCrashCourseAdapter(Context context, JsonList activeCourseJsonList) {
        this.context = context;
        this.activeCourseJsonList = activeCourseJsonList;
    }

    @NonNull
    @Override
    public MyCrashCourseAdapter.MyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_my_crash_course, parent, false);
        return new  MyCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCrashCourseAdapter.MyCourseViewHolder holder, int position) {

        Json json = activeCourseJsonList.get(position);

        try {
            holder.txtCourseDes.setText(json.getString("name"));
            if (!TextUtils.isEmpty(json.getString("image"))){
                Picasso.get().load(json.getString("image")).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.ivCourse);
            }else {
                Picasso.get().load(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.ivCourse);
            }

            holder.txtCourseTech.setText(json.getString("category_name"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String courseSlug = json.getString("slug");
                    String courseName = json.getString("name");
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

        TextView txtCourseDes,txtCourseProgramme,txtCourseTech;
        RoundedImageView ivCourse;


        public MyCourseViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCourseDes = itemView.findViewById(R.id.txtCourseDes);
            txtCourseProgramme = itemView.findViewById(R.id.txtCourseProgramme);
            txtCourseTech = itemView.findViewById(R.id.txtCourseTech);
            ivCourse = itemView.findViewById(R.id.ivMyCourse);
        }
    }

    private void loadFragment(View v, String courseSlug,String courseName) {
        Config.POP_HOME = true;
        Config.myCourseSlug = courseSlug;
        Config.myCourseTitle = courseName;
        bundle.putString("slug",courseSlug);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        if (courseDetailFragment == null)
            courseDetailFragment = new MyCrashCourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean checkString(String string) {
        boolean value = true;

        if (TextUtils.isEmpty(string) || string.equals("null")) {
            value = false;
        }
        return value;
    }
}

