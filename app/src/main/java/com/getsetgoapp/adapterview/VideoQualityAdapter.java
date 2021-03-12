package com.getsetgoapp.adapterview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgoapp.Fragment.MyCourseDetailFragment;
import com.getsetgoapp.Model.VideoUrlModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.VideoPlayActivity;
import com.getsetgoapp.databinding.ActivityCourseUrlListBinding;

import java.util.List;

public class VideoQualityAdapter extends RecyclerView.Adapter<VideoQualityAdapter.ViewHolder> {

    Context context;
    List<VideoUrlModel> videoUrlModelList;
    MyCourseDetailFragment fragment;
    private int lastCheckPosition;
    private int checkActivity;

    public interface onClick{
        void qualityClick(VideoUrlModel model,int position);
    }

    public VideoQualityAdapter(Context context, List<VideoUrlModel> videoUrlModelList,MyCourseDetailFragment fragment,int lastSelectedPosition,int checkActivity) {
        this.context = context;
        this.videoUrlModelList = videoUrlModelList;
        this.fragment = fragment;
        this.lastCheckPosition = lastSelectedPosition;
        this.checkActivity = checkActivity;

    }

    public VideoQualityAdapter(Context context, List<VideoUrlModel> videoUrlModelList,int lastSelectedPosition,int checkActivity) {
        this.context = context;
        this.videoUrlModelList = videoUrlModelList;
        this.lastCheckPosition = lastSelectedPosition;
        this.checkActivity = checkActivity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCourseUrlListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_course_url_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoUrlModel model = videoUrlModelList.get(position);

        holder.binding.radioButton.setChecked(position == lastCheckPosition);
        holder.binding.radioButton.setText(model.getQuality());
        holder.binding.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCheckPosition = holder.getAdapterPosition();
                if (checkActivity==1){
                    ((MyCourseDetailFragment)fragment).qualityClick(model,position);
                }else if (checkActivity==2){
                    ((VideoPlayActivity)context).qualityClick(model,position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return videoUrlModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivityCourseUrlListBinding binding;

        public ViewHolder(@NonNull ActivityCourseUrlListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
