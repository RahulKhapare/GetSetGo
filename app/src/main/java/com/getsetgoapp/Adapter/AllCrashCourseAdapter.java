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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.getsetgoapp.Fragment.BuyCourseFragment;
import com.getsetgoapp.Fragment.CrashCourseDetailFragment;
import com.getsetgoapp.Model.AllCrashCourseModel;
import com.getsetgoapp.Model.ChildCrashCourseModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.LayoutLiveCourseListBinding;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllCrashCourseAdapter extends RecyclerView.Adapter<AllCrashCourseAdapter.ViewHolder> {

    Context context;
    List<AllCrashCourseModel> allCrashCourseModelList;

    CrashCourseDetailFragment courseDetailFragment;
    BuyCourseFragment buyCourseFragment;

    public AllCrashCourseAdapter(Context context, List<AllCrashCourseModel> allCrashCourseModelList) {
        this.context = context;
        this.allCrashCourseModelList = allCrashCourseModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.layout_all_crash_course_list, parent, false);
//        return new ViewHolder(view);
        LayoutLiveCourseListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_live_course_list, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AllCrashCourseModel model = allCrashCourseModelList.get(position);

//        holder.txtCourseName.setText(model.getText());
//        holder.llSmartSchoolLabel.setVisibility(View.GONE);
//
//        List<ChildCrashCourseModel> childCrashCourseModelList = new ArrayList<>();
//
//        if (model.getCrash_courses()!=null && !model.getCrash_courses().isEmpty()){
//            for (Json json : model.getCrash_courses()){
//                ChildCrashCourseModel courseModel = new ChildCrashCourseModel();
//                courseModel.setId(json.getString("id"));
//                courseModel.setName(json.getString("name"));
//                courseModel.setSlug(json.getString("slug"));
//                courseModel.setProgram_date(json.getString("program_date"));
//                courseModel.setProgram_end_date(json.getString("program_end_date"));
//                courseModel.setImage(json.getString("image"));
//                courseModel.setCategory_name(json.getString("category_name"));
//                courseModel.setPrice(json.getString("price"));
//                courseModel.setSale_price(json.getString("sale_price"));
//                courseModel.setShare_link(json.getString("share_link"));
//                courseModel.setText(model.getText());
//                childCrashCourseModelList.add(courseModel);
//            }
//        }
//
//        ChildCrashCourseAdapter adapter = new ChildCrashCourseAdapter(context,childCrashCourseModelList);
//        holder.recyclerViewChild.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
//        holder.recyclerViewChild.setHasFixedSize(true);
//        holder.recyclerViewChild.setAdapter(adapter);

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
            holder.binding.txtPost.setText(checkString(designation,holder.binding.txtPost));
            if (rating != null && !rating.equals("")) {
                try{
                    holder.binding.ratingBar.setRating(Integer.parseInt(rating));
                }catch (Exception e){
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

        if (model.getIs_purchased() == 1) {
            holder.binding.btnEnrollNow.setVisibility(View.GONE);
        } else {
            holder.binding.btnEnrollNow.setVisibility(View.VISIBLE);
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
                loadFragment(v, model.getName(), model.getSlug());
            }
        });

        holder.binding.btnEnrollNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                loadBuyFragment(model.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return allCrashCourseModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        LinearLayout llSmartSchoolLabel;
//        TextView txtCourseName;
//        RecyclerView recyclerViewChild;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            llSmartSchoolLabel = itemView.findViewById(R.id.llSmartSchoolLabel);
//            txtCourseName = itemView.findViewById(R.id.txtCourseName);
//            recyclerViewChild = itemView.findViewById(R.id.recyclerViewChild);
//        }

        LayoutLiveCourseListBinding binding;

        public ViewHolder(@NonNull LayoutLiveCourseListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    private void loadFragment(View v, String title, String slug) {
        Config.POP_HOME = true;
        Bundle bundle = new Bundle();
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        bundle.putString("title", title);
        bundle.putString("slug", slug);
        bundle.putBoolean("isFromHome", true);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        courseDetailFragment = new CrashCourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadBuyFragment(String course_id) {
        Bundle bundle = new Bundle();
        bundle.putString("course_id", course_id);
        bundle.putString("fromCrash", "1");
        AppCompatActivity activity = (AppCompatActivity) context;
        buyCourseFragment = new BuyCourseFragment();
        buyCourseFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, buyCourseFragment)
                .addToBackStack("")
                .commit();
    }

    private void shareApp(Context context, String link) {
        String shareMessage = link;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Share Using"));
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
