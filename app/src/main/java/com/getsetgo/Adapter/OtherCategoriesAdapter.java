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
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

public class OtherCategoriesAdapter extends RecyclerView.Adapter<OtherCategoriesAdapter.OtherCategoriesViewHolder> {

    Context context;

    public OtherCategoriesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OtherCategoriesAdapter.OtherCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_other_cateories, parent, false);
        return new  OtherCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherCategoriesAdapter.OtherCategoriesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class OtherCategoriesViewHolder extends RecyclerView.ViewHolder{

        TextView txtOtherCategories;
        RoundedImageView imvOtherCategories;


        public OtherCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOtherCategories = itemView.findViewById(R.id.txtOtherCategories);
            imvOtherCategories = itemView.findViewById(R.id.imvOtherCategories);
        }
    }
}

