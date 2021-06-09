package com.getsetgoapp.adapterview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.Fragment.CourseDetailFragment;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.P;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class HomeChildAdapter extends RecyclerView.Adapter<HomeChildAdapter.ChildViewHolder> {

    Context context;
    JsonList jsonList;
    CourseDetailFragment courseDetailFragment;


    HomeChildAdapter(Context context, JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_best_selling_course_vertical, viewGroup, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {

        Json childItem = jsonList.get(position);

        holder.txtCourseName.setText(childItem.getString("course_name"));
        holder.txtProfName.setText(childItem.getString("instructor_name"));
        String review = childItem.getString("rating");
        holder.txtReview.setText(review);
        setReview(review, holder);

//        holder.txtOldPrice.setText("₹ " + childItem.getString("price"));
//        holder.txtNewPrice.setText("₹ " + childItem.getString("sale_price"));

        String salePrice = childItem.getString("sale_price");
        if (!TextUtils.isEmpty(salePrice) && !salePrice.equals("null") ){
            if (salePrice.equals("0")){
                holder.txtNewPrice.setText("FREE");
            }else {
                holder.txtNewPrice.setText("PAID");
            }
        }

//        String imagePath = childItem.getString("image_path")+"";
//        if (imagePath.equals("null") || imagePath.isEmpty())
//            imagePath = "pathMustNotBeEmpty";
//
//        LoadImage.picasso(holder.ivCourseImage, imagePath);
        Picasso.get().load(childItem.getString("image")).placeholder(R.drawable.ic_wp).error(R.drawable.ic_wp).into(holder.ivCourseImage);

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view, childItem.getString("course_name"),childItem.getString("slug"));

            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                shareApp(context,childItem.getString(P.share_link));
            }
        });
    }

    @Override
    public int getItemCount() {
        return jsonList.size();
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView txtCourseName, txtProfName, txtReview, txtNewPrice, txtOldPrice, txtBestSeller;
        RoundedImageView ivCourseImage;
        ImageView imgReview1, imgReview2, imgReview3, imgReview4, imgReview5,imgShare;
        CardView llMain;

        public ChildViewHolder(View itemView) {
            super(itemView);
            llMain = itemView.findViewById(R.id.llMain);
            txtCourseName = itemView.findViewById(R.id.txtCourseName);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
            txtProfName = itemView.findViewById(R.id.txtProfName);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtNewPrice = itemView.findViewById(R.id.txtNewPrice);
            txtOldPrice = itemView.findViewById(R.id.txtOldPrice);
            txtOldPrice.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.strike_line));
            imgReview1 = itemView.findViewById(R.id.imgReview1);
            imgReview2 = itemView.findViewById(R.id.imgReview2);
            imgReview3 = itemView.findViewById(R.id.imgReview3);
            imgReview4 = itemView.findViewById(R.id.imgReview4);
            imgReview5 = itemView.findViewById(R.id.imgReview5);
            imgShare = itemView.findViewById(R.id.imgShare);
            txtBestSeller = itemView.findViewById(R.id.txtBestSeller);
            txtBestSeller.setVisibility(View.GONE);
        }
    }

    private void setReview(String review, ChildViewHolder viewHolder) {
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

