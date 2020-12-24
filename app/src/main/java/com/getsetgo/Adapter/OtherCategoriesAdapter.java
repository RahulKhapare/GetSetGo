package com.getsetgo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Fragment.ViewAllCategoriesFragment;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

public class OtherCategoriesAdapter extends RecyclerView.Adapter<OtherCategoriesAdapter.OtherCategoriesViewHolder> {

    Context context;
    ViewAllCategoriesFragment viewAllCategoriesFragment;

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view,"Smart School");
            }
        });

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
    private void loadFragment(View v,String title){
        Bundle bundle = new Bundle();
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        bundle.putString("subTitle", title);
        bundle.putBoolean("isFromHome", true);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        viewAllCategoriesFragment = new ViewAllCategoriesFragment();
        viewAllCategoriesFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, viewAllCategoriesFragment)
                .addToBackStack(null)
                .commit();
    }
}

