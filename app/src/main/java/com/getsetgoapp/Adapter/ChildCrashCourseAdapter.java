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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.getsetgoapp.Fragment.BuyCourseFragment;
import com.getsetgoapp.Fragment.CourseDetailFragment;
import com.getsetgoapp.Fragment.CrashCourseDetailFragment;
import com.getsetgoapp.Model.AllCrashCourseModel;
import com.getsetgoapp.Model.ChildCrashCourseModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.LayoutLiveCourseListBinding;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChildCrashCourseAdapter extends RecyclerView.Adapter<ChildCrashCourseAdapter.ViewHolder> {

    Context context;
    List<ChildCrashCourseModel> allCrashCourseModelList;
    CrashCourseDetailFragment courseDetailFragment;
    BuyCourseFragment buyCourseFragment;

    public ChildCrashCourseAdapter(Context context, List<ChildCrashCourseModel> allCrashCourseModelList) {
        this.context = context;
        this.allCrashCourseModelList = allCrashCourseModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_child_crash_course_list, parent, false);
        return new ViewHolder(view);
//        LayoutLiveCourseListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_live_course_list, parent, false);
//        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChildCrashCourseModel model = allCrashCourseModelList.get(position);

        if (TextUtils.isEmpty(model.getImage())){
            Picasso.get().load(R.drawable.ic_no_image).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.ivCourseImage);
        }else {
            Picasso.get().load(model.getImage()).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.ivCourseImage);
        }
        holder.txtCourseName.setText(model.getName());
        holder.txtCategoryName.setText(model.getCategory_name());
        holder.txtOldPrice.setText("₹ " + model.getPrice());
        holder.txtNewPrice.setText("₹ " + model.getSale_price());

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                loadFragment(v, model.getName(),model.getSlug());
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                shareApp(context,model.getShare_link());
            }
        });

//        if (TextUtils.isEmpty(model.getImage())) {
//            Picasso.get().load(R.drawable.ic_no_image).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.binding.imgImage);
//        } else {
//            Picasso.get().load(model.getImage()).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.binding.imgImage);
//        }
//
//        holder.binding.txtCourse.setText(checkString(model.getName()));
//        holder.binding.txtName.setText("XYZ");
//        holder.binding.txtPost.setText(checkString(model.getCategory_name()));
//        holder.binding.txtDate.setText(checkString(model.getText()));
//        holder.binding.txtTime.setText(checkString(model.getProgram_date()));
//        holder.binding.txtActualPrice.setText("₹ " + checkString(model.getPrice()));
//        holder.binding.txtOfferPrice.setText("₹ " + checkString(model.getSale_price()));
//        holder.binding.txtActualPrice.setPaintFlags(holder.binding.txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//        holder.binding.imgShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Click.preventTwoClick(v);
//                shareApp(context, model.getShare_link());
//            }
//        });
//
//        holder.binding.btnViewDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Click.preventTwoClick(v);
//                loadFragment(v, model.getName(), model.getSlug());
//            }
//        });
//
//        holder.binding.btnEnrollNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Click.preventTwoClick(v);
//                loadBuyFragment(model.getId());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return allCrashCourseModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout lnrCourse;
        RoundedImageView ivCourseImage;
        TextView txtCourseName;
        TextView txtCategoryName;
        TextView txtNewPrice;
        TextView txtOldPrice;
        CardView llMain;
        ImageView imgShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llMain = itemView.findViewById(R.id.llMain);
            lnrCourse = itemView.findViewById(R.id.lnrCourse);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
            txtCourseName = itemView.findViewById(R.id.txtCourseName);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            txtNewPrice = itemView.findViewById(R.id.txtNewPrice);
            imgShare = itemView.findViewById(R.id.imgShare);
            txtOldPrice = itemView.findViewById(R.id.txtOldPrice);
            txtOldPrice.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.strike_line));
        }

//        LayoutLiveCourseListBinding binding;
//
//        public ViewHolder(@NonNull LayoutLiveCourseListBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//
//        }

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
        if (value != null && !value.equals("")) {
            return value.trim();
        } else {
            return "";
        }
    }
}
