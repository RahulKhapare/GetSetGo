package com.getsetgo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.getsetgo.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class CategoriesCommonAdapter extends RecyclerView.Adapter<CategoriesCommonAdapter.CategoriesCommonViewHolder> {

    Context context;


    public CategoriesCommonAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public CategoriesCommonAdapter.CategoriesCommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_best_selling_course, parent, false);
        return new CategoriesCommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesCommonAdapter.CategoriesCommonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class CategoriesCommonViewHolder extends RecyclerView.ViewHolder {

        TextView txtCourseName,txtProfName,txtReview,txtNewPrice,txtOldPrice,txtBestSeller;
        RoundedImageView ivCourseImage;
        ImageView imgReview1,imgReview2,imgReview3,imgReview4,imgReview5;


        public CategoriesCommonViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCourseName = itemView.findViewById(R.id.txtCourseName);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
            txtProfName = itemView.findViewById(R.id.txtProfName);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtNewPrice = itemView.findViewById(R.id.txtNewPrice);
            txtOldPrice = itemView.findViewById(R.id.txtOldPrice);
            txtOldPrice.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.strike_white_through_line));
            imgReview1 = itemView.findViewById(R.id.imgReview1);
            imgReview2 = itemView.findViewById(R.id.imgReview2);
            imgReview3 = itemView.findViewById(R.id.imgReview3);
            imgReview4 = itemView.findViewById(R.id.imgReview4);
            imgReview5 = itemView.findViewById(R.id.imgReview5);
            txtBestSeller = itemView.findViewById(R.id.txtBestSeller);
            txtBestSeller.setVisibility(View.GONE);

        }
    }
}

