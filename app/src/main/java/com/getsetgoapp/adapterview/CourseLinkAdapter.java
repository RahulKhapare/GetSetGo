package com.getsetgoapp.adapterview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgoapp.Fragment.MyCourseDetailFragment;
import com.getsetgoapp.Fragment.MyCrashCourseDetailFragment;
import com.getsetgoapp.Model.CourseLinkModel;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityCourseLinkListBinding;
import com.getsetgoapp.util.Click;

import java.util.List;

public class CourseLinkAdapter extends RecyclerView.Adapter<CourseLinkAdapter.ViewHolder> {

    Context context;
    List<CourseLinkModel> courseLinkModelList;
    String zoom = "us.zoom.videomeetings";
    MyCourseDetailFragment fragment;
    MyCrashCourseDetailFragment crashCourseDetailFragment;
    boolean fromCrash = false;

    public interface onClick{
        void onLinkClick(String link);
    }

    public CourseLinkAdapter(Context context, List<CourseLinkModel> courseLinkModelList,MyCourseDetailFragment fragment) {
        this.context = context;
        this.courseLinkModelList = courseLinkModelList;
        this.fragment = fragment;
        fromCrash = false;
    }

    public CourseLinkAdapter(Context context, List<CourseLinkModel> courseLinkModelList, MyCrashCourseDetailFragment fragment) {
        this.context = context;
        this.courseLinkModelList = courseLinkModelList;
        this.crashCourseDetailFragment = fragment;
        fromCrash = true;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCourseLinkListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_course_link_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseLinkModel model = courseLinkModelList.get(position);

        holder.binding.txtTitle.setText(model.getLink_name());

        holder.binding.lnrLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (fromCrash){
                    ((MyCrashCourseDetailFragment)crashCourseDetailFragment).onLinkClick(model.getLink());
                }else {
                    ((MyCourseDetailFragment)fragment).onLinkClick(model.getLink());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseLinkModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivityCourseLinkListBinding binding;

        public ViewHolder(@NonNull ActivityCourseLinkListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
