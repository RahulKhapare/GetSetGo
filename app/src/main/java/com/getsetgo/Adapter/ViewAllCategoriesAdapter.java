package com.getsetgo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.getsetgo.R;

public class ViewAllCategoriesAdapter extends RecyclerView.Adapter<ViewAllCategoriesAdapter.ViewAllCategoriesViewHolder> {

    Context context;


    public ViewAllCategoriesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewAllCategoriesAdapter.ViewAllCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_viewall_categories, parent, false);
        return new ViewAllCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllCategoriesAdapter.ViewAllCategoriesViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewAllCategoriesViewHolder extends RecyclerView.ViewHolder {

        TextView txtCourse, txtProfName, txtReview,
                txtNewPrice, txtOldPrice, txtBestSeller;
        ImageView imgCategory;
        CheckBox chkFav;


        public ViewAllCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCourse = itemView.findViewById(R.id.txtViewCategory);
            txtProfName = itemView.findViewById(R.id.txtViewCategoryProfName);
            txtReview = itemView.findViewById(R.id.txtViewCategoryReview);
            txtNewPrice = itemView.findViewById(R.id.txtViewCategoryNewPrice);
            txtOldPrice = itemView.findViewById(R.id.txtViewCategoryOldPrice);
            txtBestSeller = itemView.findViewById(R.id.txtViewCategoryBestSeller);
            imgCategory = itemView.findViewById(R.id.imvViewCategory);
            chkFav = itemView.findViewById(R.id.chkViewCategoryFavourites);

        }
    }
}

