package com.getsetgoapp.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgoapp.Model.LiveCoursesModel;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.LayoutLiveCourseListBinding;

import java.util.List;

public class LiveCourseAdapter extends RecyclerView.Adapter<LiveCourseAdapter.ViewHolder> {

    Context context;
    List<LiveCoursesModel> liveCoursesModelList;

    public LiveCourseAdapter(Context context, List<LiveCoursesModel> liveCoursesModelList) {
        this.context = context;
        this.liveCoursesModelList = liveCoursesModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutLiveCourseListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_live_course_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        LiveCoursesModel model = liveCoursesModelList.get(position);

        holder.binding.txtActualPrice.setPaintFlags(holder.binding.txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
//        return liveCoursesModelList.size();
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutLiveCourseListBinding binding;

        public ViewHolder(@NonNull LayoutLiveCourseListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
