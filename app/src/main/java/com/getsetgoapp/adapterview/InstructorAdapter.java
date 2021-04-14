package com.getsetgoapp.adapterview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.getsetgoapp.Model.CourseLinkModel;
import com.getsetgoapp.Model.InstructorModel;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityCourseLinkListBinding;
import com.getsetgoapp.databinding.ActivityInstructotListBinding;
import com.getsetgoapp.util.Click;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder> {

    Context context;
    List<InstructorModel> instructorModelList;


    public InstructorAdapter(Context context, List<InstructorModel> instructorModelList) {
        this.context = context;
        this.instructorModelList = instructorModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityInstructotListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_instructot_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InstructorModel model = instructorModelList.get(position);

        if (!TextUtils.isEmpty(model.getImage()) && !model.getImage().equals("null")) {
            Picasso.get().load(model.getImage()).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.binding.imvViewCategory);
        }else {
            Picasso.get().load(model.getImage()).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.binding.imvViewCategory);
        }
        holder.binding.txtProff.setText("Curated by " + model.getName());
        holder.binding.txtCourse.setText(model.getCourse_count()+ " Courses");
        holder.binding.txtJoiningDate.setText(model.getJoining_date());

    }

    @Override
    public int getItemCount() {
        return instructorModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivityInstructotListBinding binding;
        public ViewHolder(@NonNull ActivityInstructotListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
