package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class BestSellingCourseAdapter extends RecyclerView.Adapter<BestSellingCourseAdapter.BestSellingCourseViewHolder> {

    Context context;

    public BestSellingCourseAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BestSellingCourseAdapter.BestSellingCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_best_selling_course, parent, false);
        return new BestSellingCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingCourseAdapter.BestSellingCourseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class BestSellingCourseViewHolder extends RecyclerView.ViewHolder {

        TextView txtCourseName,txtProfName,txtReview,txtNewPrice,txtOldPrice;
        RoundedImageView ivCourseImage;
        ImageView imgReview1,imgReview2,imgReview3,imgReview4,imgReview5;


        public BestSellingCourseViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCourseName = itemView.findViewById(R.id.txtCourseName);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
            txtProfName = itemView.findViewById(R.id.txtProfName);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtNewPrice = itemView.findViewById(R.id.txtNewPrice);
            txtOldPrice = itemView.findViewById(R.id.txtOldPrice);
            txtOldPrice.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.strike_through_line));
            imgReview1 = itemView.findViewById(R.id.imgReview1);
            imgReview2 = itemView.findViewById(R.id.imgReview2);
            imgReview3 = itemView.findViewById(R.id.imgReview3);
            imgReview4 = itemView.findViewById(R.id.imgReview4);
            imgReview5 = itemView.findViewById(R.id.imgReview5);

        }
    }
}

