package com.getsetgoapp.adapterview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.getsetgoapp.Model.CourseLinkModel;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityCourseLinkListBinding;
import com.getsetgoapp.util.Click;

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

        holder.binding.lnrLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                openWebPage(model.getLink());
            }
        });
    }

    public void openWebPage(String url) {
//        Log.e("TAG", "openWebPage: "+ url );
        try{
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }else{
                H.showMessage(context,"Something went wrong to open link");
            }
        }catch (Exception e){
            H.showMessage(context,"Something went wrong to open link");
        }
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
