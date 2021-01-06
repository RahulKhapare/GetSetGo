package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class ChildItemAdapter extends RecyclerView.Adapter<ChildItemAdapter.ChildViewHolder> {

    Context context;
    JsonList jsonList;

    ChildItemAdapter(Context context, JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_best_selling_course,viewGroup, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ChildViewHolder holder, int position) {

        Json childItem = jsonList.get(position);

        holder.txtCourseName.setText(childItem.getString("course_name"));
        holder.txtOldPrice.setText("₹ " + childItem.getString("price"));
        holder.txtNewPrice.setText("₹ " + childItem.getString("discounted_price"));
        //LoadImage.picasso(holder.ivCourseImage,childItem.getString("image_path"));
    }

    @Override
    public int getItemCount() {
        return jsonList.size();
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView txtCourseName,txtProfName,txtReview,txtNewPrice,txtOldPrice,txtBestSeller;
        RoundedImageView ivCourseImage;
        ImageView imgReview1,imgReview2,imgReview3,imgReview4,imgReview5;

        ChildViewHolder(View itemView) {
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

