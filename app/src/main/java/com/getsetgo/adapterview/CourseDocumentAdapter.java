package com.getsetgo.adapterview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Fragment.MyCourseDetailFragment;
import com.getsetgo.Model.CourseDocumentModel;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityCourseDocumentListBinding;
import com.getsetgo.databinding.ActivityCourseModuleListBinding;
import com.getsetgo.util.Click;

import java.util.List;

public class CourseDocumentAdapter extends RecyclerView.Adapter<CourseDocumentAdapter.ViewHolder> {

    Context context;
    List<CourseDocumentModel> courseDocumentModelList;
    MyCourseDetailFragment fragment;

    public interface onClick{
        void downloadPDF(CourseDocumentModel model);
    }


    public CourseDocumentAdapter(Context context, List<CourseDocumentModel> courseDocumentModelList,MyCourseDetailFragment fragment) {
        this.context = context;
        this.courseDocumentModelList = courseDocumentModelList;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCourseDocumentListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_course_document_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseDocumentModel model = courseDocumentModelList.get(position);

        holder.binding.txtTitle.setText(model.getFile_name());
        holder.binding.txtUrl.setText(model.getFile());
        holder.binding.lnrDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((MyCourseDetailFragment)fragment).downloadPDF(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseDocumentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivityCourseDocumentListBinding binding;
        public ViewHolder(@NonNull ActivityCourseDocumentListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
