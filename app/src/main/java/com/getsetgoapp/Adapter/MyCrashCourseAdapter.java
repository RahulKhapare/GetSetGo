package com.getsetgoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.Fragment.MyCrashCourseDetailFragment;
import com.getsetgoapp.Model.MyCrashCourseModel;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.LayoutMyCourseListBinding;
import com.getsetgoapp.util.CheckConnection;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyCrashCourseAdapter extends RecyclerView.Adapter<MyCrashCourseAdapter.ViewHolder> {

    Context context;
    JsonList activeCourseJsonList;
    MyCrashCourseDetailFragment courseDetailFragment;
    List<MyCrashCourseModel> myCrashCourseModelList;
    Bundle bundle = new Bundle();

//    public MyCrashCourseAdapter(Context context, JsonList activeCourseJsonList) {
//        this.context = context;
//        this.activeCourseJsonList = activeCourseJsonList;
//    }

    public MyCrashCourseAdapter(Context context, List<MyCrashCourseModel> myCrashCourseModelList) {
        this.context = context;
        this.myCrashCourseModelList = myCrashCourseModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.layout_my_crash_course, parent, false);
//        return new MyCourseViewHolder(view);
        LayoutMyCourseListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_my_course_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        Json json = activeCourseJsonList.get(position);
//
//        try {
//            holder.txtCourseDes.setText(json.getString("name"));
//            if (!TextUtils.isEmpty(json.getString("image"))) {
//                Picasso.get().load(json.getString("image")).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.ivCourse);
//            } else {
//                Picasso.get().load(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.ivCourse);
//            }
//
//            holder.txtCourseTech.setText(json.getString("category_name"));
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    String courseSlug = json.getString("slug");
//                    String courseName = json.getString("name");
//                    if (CheckConnection.isVailable(context)) {
//                        loadFragment(view, courseSlug, courseName);
//                    } else {
//                        H.showMessage(context, "No internet connection available");
//                    }
//
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        MyCrashCourseModel model = myCrashCourseModelList.get(position);

        holder.binding.txtCourse.setText(checkString(model.getName()));
        holder.binding.txtStart.setText(checkString(model.getSkill_level()));
        holder.binding.txtLanguage.setText(checkString(model.getLanguage()));
        holder.binding.txtDate.setText(checkString(model.getProgram_date()));
        holder.binding.txtTime.setText(checkString(model.getProgram_time()));
        holder.binding.txtActualPrice.setText("₹ " + checkString(model.getPrice()));
        holder.binding.txtOfferPrice.setText("₹ " + checkString(model.getMlm_price()));
        holder.binding.txtActualPrice.setPaintFlags(holder.binding.txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (model.getInstructors().size() == 1) {
            String name = model.getInstructors().get(0).getString("instructor_name");
            String image = model.getInstructors().get(0).getString("instructor_image");
            String designation = model.getInstructors().get(0).getString("designation");
            String rating = model.getInstructors().get(0).getString("rating");

            if (TextUtils.isEmpty(image)) {
                Picasso.get().load(R.drawable.ic_no_image).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.binding.imgImage);
            } else {
                Picasso.get().load(image).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.binding.imgImage);
            }
            holder.binding.txtName.setText(name);
            holder.binding.txtPost.setText(checkString(designation, holder.binding.txtPost));
            if (rating != null && !rating.equals("")) {
                try {
                    holder.binding.ratingBar.setRating(Integer.parseInt(rating));
                } catch (Exception e) {
                    holder.binding.ratingBar.setRating(5);
                }
            } else {
                holder.binding.ratingBar.setRating(5);
            }
        } else {
            String name = "";
            for (Json json : model.getInstructors()) {
                if (name.equals("")) {
                    name = json.getString("instructor_name");
                } else {
                    name = name + "\n" + json.getString("instructor_name");
                }
            }
            holder.binding.txtName.setText(name);
            holder.binding.cardImage.setVisibility(View.GONE);
            holder.binding.txtPost.setVisibility(View.GONE);
            holder.binding.ratingBar.setVisibility(View.GONE);
        }

        holder.binding.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                shareApp(context, model.getShare_url());
            }
        });

        holder.binding.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (CheckConnection.isVailable(context)) {
                    loadFragment(v, model.getSlug(),model.getName());
                } else {
                    H.showMessage(context, "No internet connection available");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return myCrashCourseModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView txtCourseDes, txtCourseProgramme, txtCourseTech;
//        RoundedImageView ivCourse;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            txtCourseDes = itemView.findViewById(R.id.txtCourseDes);
//            txtCourseProgramme = itemView.findViewById(R.id.txtCourseProgramme);
//            txtCourseTech = itemView.findViewById(R.id.txtCourseTech);
//            ivCourse = itemView.findViewById(R.id.ivMyCourse);
//        }

        LayoutMyCourseListBinding binding;

        public ViewHolder(@NonNull LayoutMyCourseListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    private void shareApp(Context context, String link) {
        String shareMessage = link;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Share Using"));
    }

    private void loadFragment(View v, String courseSlug, String courseName) {
        Config.POP_HOME = true;
        Config.myCourseSlug = courseSlug;
        Config.myCourseTitle = courseName;
        bundle.putString("slug", courseSlug);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        if (courseDetailFragment == null)
            courseDetailFragment = new MyCrashCourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    private String checkString(String value) {
        if (value != null && !value.equals("") && !value.equalsIgnoreCase("null")) {
            return value.trim();
        } else {
            return "";
        }
    }

    private String checkString(String value, TextView textView) {
        if (value != null && !value.equals("") && !value.equalsIgnoreCase("null")) {
            return value.trim();
        } else {
            textView.setVisibility(View.GONE);
            return "";
        }
    }
}

