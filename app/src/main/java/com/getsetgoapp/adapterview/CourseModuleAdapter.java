package com.getsetgoapp.adapterview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.getsetgoapp.Fragment.MyCourseDetailFragment;
import com.getsetgoapp.Model.CourseModuleModel;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityCourseModuleListBinding;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.P;

import java.util.ArrayList;
import java.util.List;

public class CourseModuleAdapter extends RecyclerView.Adapter<CourseModuleAdapter.ViewHolder> {

    Context context;
    List<CourseModuleModel> courseModuleModelList;
    int lastCheckPosition = 0;
    MyCourseDetailFragment fragment;
    boolean flagValue = false;

    public interface click{
        void moduleSelection(CourseModuleModel moduleModel);
    }

    public CourseModuleAdapter(Context context, List<CourseModuleModel> courseModuleModelList,MyCourseDetailFragment fragment, boolean flagValue) {
        this.context = context;
        this.courseModuleModelList = courseModuleModelList;
        this.fragment = fragment;
        this.flagValue = flagValue;
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

        holder.binding.txtTitle.setText(model.getModule_name());

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
                if (model.getVideos().size()!=0){
                    holder.binding.lnrChildView.setVisibility(View.VISIBLE);
//                    ((MyCourseDetailFragment)fragment).moduleSelection(model);
                }else {
                    holder.binding.lnrChildView.setVisibility(View.GONE);
                }
            }
        }else {
            model.setClickFlag(false);
            holder.binding.imgTitle.setImageResource(R.drawable.ic_expand);
            holder.binding.lnrChildView.setVisibility(View.GONE);
        }

        List<CourseChildModel> courseChildModelList = new ArrayList<>();

        if (model.getVideos()!=null || model.getVideos().size()!=0){
            for (Json json : model.getVideos()){
                Log.e("TAG", "onBindViewHolder21133: " + json.toString() );
                CourseChildModel childModel = new CourseChildModel();
                childModel.setMainVideoList(model.getVideos());
                childModel.setVideo_id(json.getString(P.video_id));
                childModel.setVideo_title(json.getString(P.video_title));
                childModel.setDuration(json.getString(P.duration));
                childModel.setIs_completed(json.getString(P.is_completed));
                childModel.setVideo_urls(json.getJsonList(P.video_urls));
                childModel.setAdditional_links(json.getJsonList(P.additional_links));
                childModel.setAdditional_files(json.getJsonList(P.additional_files));
                courseChildModelList.add(childModel);
            }
        }

        CourseChildAdapter adapter = new CourseChildAdapter(context,courseChildModelList,flagValue,fragment);
        holder.binding.recyclerChild.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerChild.setNestedScrollingEnabled(false);
        holder.binding.recyclerChild.setAdapter(adapter);

        if (flagValue){
            flagValue = false;
        }

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
