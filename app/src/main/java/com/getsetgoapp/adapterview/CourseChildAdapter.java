package com.getsetgoapp.adapterview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.getsetgoapp.Fragment.MyCourseDetailFragment;
import com.getsetgoapp.Fragment.MyCrashCourseDetailFragment;
import com.getsetgoapp.Fragment.MyCrashCourseFragment;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityCourseChildListBinding;

import java.util.List;

public class CourseChildAdapter extends RecyclerView.Adapter<CourseChildAdapter.ViewHolder> {

    Context context;
    List<CourseChildModel> courseChildModelList;
    boolean flagValue = false;
    MyCourseDetailFragment fragment;
    MyCrashCourseDetailFragment crashCourseDetailFragment;
    boolean fromCrash = false;

    public interface childAction{
        void calledChild(CourseChildModel model,int childVideoPosition);
    }


    public CourseChildAdapter(Context context, List<CourseChildModel> courseChildModelList, boolean flagValue,MyCourseDetailFragment fragment) {
        this.context = context;
        this.courseChildModelList = courseChildModelList;
        this.flagValue = flagValue;
        this.fragment = fragment;
        fromCrash = false;
    }

    public CourseChildAdapter(Context context, List<CourseChildModel> courseChildModelList, boolean flagValue,MyCrashCourseDetailFragment fragment) {
        this.context = context;
        this.courseChildModelList = courseChildModelList;
        this.flagValue = flagValue;
        this.crashCourseDetailFragment = fragment;
        fromCrash = true;
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

        int index = position+1;
        holder.binding.txtCount.setText(index + "");
        holder.binding.txtVideoTitle.setText(model.getVideo_title());
        holder.binding.txtDuration.setText("Duration "+ model.getDuration() + " mins");

        if (!TextUtils.isEmpty(model.getIs_completed()) && !model.getIs_completed().equals("null")){
            holder.binding.txtCompleted.setVisibility(View.VISIBLE);
            holder.binding.txtPreview.setVisibility(View.GONE);
        }

        if (flagValue){
            flagValue = false;
            if (fromCrash){
                ((MyCrashCourseDetailFragment)crashCourseDetailFragment).calledChild(model,position);
            }else {
                ((MyCourseDetailFragment)fragment).calledChild(model,position);
            }

        }

        holder.binding.lnrChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromCrash){
                    ((MyCrashCourseDetailFragment)crashCourseDetailFragment).calledChild(model,position);
                }else {
                    ((MyCourseDetailFragment)fragment).calledChild(model,position);
                }
            }
        });

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
