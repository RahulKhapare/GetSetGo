package com.getsetgo.adapterview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Model.CourseLinkModel;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityCourseLinkListBinding;
import com.getsetgo.util.Click;

import java.util.List;

public class CourseLinkAdapter extends RecyclerView.Adapter<CourseLinkAdapter.ViewHolder> {

    Context context;
    List<CourseLinkModel> courseLinkModelList;


    public CourseLinkAdapter(Context context, List<CourseLinkModel> courseLinkModelList) {
        this.context = context;
        this.courseLinkModelList = courseLinkModelList;
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
        holder.binding.txtUrl.setText(model.getLink());

        holder.binding.lnrLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
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
