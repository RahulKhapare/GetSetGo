package com.getsetgo.adapterview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Model.CourseModuleModel;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityCourseModuleListBinding;
import com.getsetgo.util.Click;

import java.util.ArrayList;
import java.util.List;

public class CourseModuleAdapter extends RecyclerView.Adapter<CourseModuleAdapter.ViewHolder> {

    Context context;
    List<CourseModuleModel> courseModuleModelList;
    int lastCheckPosition = -1;

    public CourseModuleAdapter(Context context, List<CourseModuleModel> courseModuleModelList) {
        this.context = context;
        this.courseModuleModelList = courseModuleModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCourseModuleListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_course_module_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseModuleModel model = courseModuleModelList.get(position);

        holder.binding.txtTitle.setText(model.getModule_title());

        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                lastCheckPosition = holder.getAdapterPosition();
                notifyItemRangeChanged(0,courseModuleModelList.size());
            }
        });

        if (lastCheckPosition==position){
            if (model.isClickFlag()){
                model.setClickFlag(false);
                holder.binding.imgTitle.setImageResource(R.drawable.ic_expand);
                holder.binding.lnrChildView.setVisibility(View.GONE);
            }else {
                model.setClickFlag(true);
                holder.binding.imgTitle.setImageResource(R.drawable.ic_collapse);
                holder.binding.lnrChildView.setVisibility(View.VISIBLE);
            }
        }else {
            model.setClickFlag(false);
            holder.binding.imgTitle.setImageResource(R.drawable.ic_expand);
            holder.binding.lnrChildView.setVisibility(View.GONE);
        }

        List<CourseChildModel> courseChildModelList = new ArrayList<>();
        CourseChildAdapter adapter = new CourseChildAdapter(context,courseChildModelList);
        holder.binding.recyclerChild.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerChild.setNestedScrollingEnabled(false);
        holder.binding.recyclerChild.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return courseModuleModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivityCourseModuleListBinding binding;
        public ViewHolder(@NonNull ActivityCourseModuleListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
