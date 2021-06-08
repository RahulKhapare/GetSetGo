package com.getsetgoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.getsetgoapp.Fragment.CourseDetailFragment;
import com.getsetgoapp.Model.BestSellingCourseModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.util.CheckConnection;
import com.getsetgoapp.util.Click;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BestSellingCourseAdapter extends RecyclerView.Adapter<BestSellingCourseAdapter.BestSellingCourseViewHolder> {

    Context context;
    List<BestSellingCourseModel> bestSellingCourseModelList;
    String rupees = "₹ ";
    int flag = 0;
    CourseDetailFragment courseDetailFragment;


    public BestSellingCourseAdapter(Context context, List<BestSellingCourseModel> bestSellingCourseModelList,int flag) {
        this.context = context;
        this.bestSellingCourseModelList = bestSellingCourseModelList;
        this.flag = flag;
    }

    @NonNull
    @Override
    public BestSellingCourseAdapter.BestSellingCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (flag==1){
            view = LayoutInflater.from(context).inflate(R.layout.layout_best_selling_course_vertical, parent, false);
        }else if (flag==2){
            view = LayoutInflater.from(context).inflate(R.layout.layout_best_selling_course_horizontal, parent, false);
        }
        return new BestSellingCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingCourseAdapter.BestSellingCourseViewHolder holder, int position) {

        BestSellingCourseModel model = bestSellingCourseModelList.get(position);
        Picasso.get().load(model.getImage()).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.ivCourseImage);
        holder.txtCourseName.setText(model.getCourse_name());
        holder.txtProfName.setText(model.getInstructor_name());
        holder.txtReview.setText(model.getRating());
        setReview(model.getRating(),holder);
        holder.txtNewPrice.setText(rupees + model.getSale_price());
        holder.txtOldPrice.setText(rupees + model.getPrice());

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (CheckConnection.isVailable(context)){
                    loadFragment(v,model.getCourse_name(),model.getSlug());
                }else {
                    H.showMessage(context,"No internet connection available");
                }
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                shareApp(context,model.getShare_link());
            }
        });

    }

    @Override
    public int getItemCount() {
        return bestSellingCourseModelList.size();
    }

    public class BestSellingCourseViewHolder extends RecyclerView.ViewHolder {

        TextView txtCourseName, txtProfName, txtReview, txtNewPrice, txtOldPrice;
        RoundedImageView ivCourseImage;
        ImageView imgReview1, imgReview2, imgReview3, imgReview4, imgReview5,imgShare;
        RelativeLayout lnrBestSaller;
        CardView llMain;


        public BestSellingCourseViewHolder(@NonNull View itemView) {
            super(itemView);

            llMain = itemView.findViewById(R.id.llMain);
            txtCourseName = itemView.findViewById(R.id.txtCourseName);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
            txtProfName = itemView.findViewById(R.id.txtProfName);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtNewPrice = itemView.findViewById(R.id.txtNewPrice);
            txtOldPrice = itemView.findViewById(R.id.txtOldPrice);
            txtOldPrice.setPaintFlags(txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            txtOldPrice.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.strike_through_line));
            imgReview1 = itemView.findViewById(R.id.imgReview1);
            imgReview2 = itemView.findViewById(R.id.imgReview2);
            imgReview3 = itemView.findViewById(R.id.imgReview3);
            imgReview4 = itemView.findViewById(R.id.imgReview4);
            imgReview5 = itemView.findViewById(R.id.imgReview5);
            lnrBestSaller = itemView.findViewById(R.id.lnrBestSaller);
            imgShare = itemView.findViewById(R.id.imgShare);

        }
    }

    private void setReview(String review, BestSellingCourseAdapter.BestSellingCourseViewHolder viewHolder) {
        if (review.equalsIgnoreCase("1.00")) {
            viewHolder.imgReview1.setVisibility(View.VISIBLE);
        } else if (review.equalsIgnoreCase("1.50")) {
            viewHolder.imgReview1.setVisibility(View.VISIBLE);
            viewHolder.imgReview2.setVisibility(View.VISIBLE);
            viewHolder.imgReview2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_half_star_review));
        } else if (review.equalsIgnoreCase("2.00")) {
            viewHolder.imgReview1.setVisibility(View.VISIBLE);
            viewHolder.imgReview2.setVisibility(View.VISIBLE);
        } else if (review.equalsIgnoreCase("2.50")) {
            viewHolder.imgReview1.setVisibility(View.VISIBLE);
            viewHolder.imgReview2.setVisibility(View.VISIBLE);
            viewHolder.imgReview3.setVisibility(View.VISIBLE);
            viewHolder.imgReview3.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_half_star_review));
        } else if (review.equalsIgnoreCase("3.00")) {
            viewHolder.imgReview1.setVisibility(View.VISIBLE);
            viewHolder.imgReview2.setVisibility(View.VISIBLE);
            viewHolder.imgReview3.setVisibility(View.VISIBLE);
        } else if (review.equalsIgnoreCase("3.50")) {
            viewHolder.imgReview1.setVisibility(View.VISIBLE);
            viewHolder.imgReview2.setVisibility(View.VISIBLE);
            viewHolder.imgReview3.setVisibility(View.VISIBLE);
            viewHolder.imgReview4.setVisibility(View.VISIBLE);
            viewHolder.imgReview4.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_half_star_review));
        } else if (review.equalsIgnoreCase("4.00")) {
            viewHolder.imgReview1.setVisibility(View.VISIBLE);
            viewHolder.imgReview2.setVisibility(View.VISIBLE);
            viewHolder.imgReview3.setVisibility(View.VISIBLE);
            viewHolder.imgReview4.setVisibility(View.VISIBLE);
        } else if (review.equalsIgnoreCase("4.50")) {
            viewHolder.imgReview1.setVisibility(View.VISIBLE);
            viewHolder.imgReview2.setVisibility(View.VISIBLE);
            viewHolder.imgReview3.setVisibility(View.VISIBLE);
            viewHolder.imgReview4.setVisibility(View.VISIBLE);
            viewHolder.imgReview5.setVisibility(View.VISIBLE);
            viewHolder.imgReview5.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_half_star_review));
        } else if (review.equalsIgnoreCase("5.00")) {
            viewHolder.imgReview1.setVisibility(View.VISIBLE);
            viewHolder.imgReview2.setVisibility(View.VISIBLE);
            viewHolder.imgReview3.setVisibility(View.VISIBLE);
            viewHolder.imgReview4.setVisibility(View.VISIBLE);
            viewHolder.imgReview5.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgReview1.setVisibility(View.VISIBLE);
        }
    }

    private void loadFragment(View v, String title,String slug) {
        Bundle bundle = new Bundle();
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        bundle.putString("title", title);
        bundle.putString("slug", slug);
        bundle.putBoolean("isFromHome", true);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        courseDetailFragment = new CourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
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
}

