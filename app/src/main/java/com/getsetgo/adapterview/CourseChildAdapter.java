package com.getsetgo.adapterview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Model.CourseLinkModel;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityCourseChildListBinding;
import com.getsetgo.databinding.ActivityCourseLinkListBinding;

import java.util.List;

public class CourseChildAdapter extends RecyclerView.Adapter<CourseChildAdapter.ViewHolder> {

    Context context;
    List<CourseChildModel> courseChildModelList;


    public CourseChildAdapter(Context context, List<CourseChildModel> courseChildModelList) {
        this.context = context;
        this.courseChildModelList = courseChildModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCourseChildListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_course_child_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseChildModel model = courseChildModelList.get(position);
    }

    @Override
    public int getItemCount() {
        return courseChildModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivityCourseChildListBinding binding;
        public ViewHolder(@NonNull ActivityCourseChildListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
