package com.getsetgo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.Fragment.ViewAllCategoriesFragment;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class OtherCategoriesAdapter extends RecyclerView.Adapter<OtherCategoriesAdapter.OtherCategoriesViewHolder> {

    Context context;
    ViewAllCategoriesFragment viewAllCategoriesFragment;
    JsonList jsonList;

    public OtherCategoriesAdapter(Context context, JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public OtherCategoriesAdapter.OtherCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_other_cateories, parent, false);
        return new OtherCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherCategoriesAdapter.OtherCategoriesViewHolder holder, int position) {

        final Json json = jsonList.get(position);

        String image = json.getString("category_image");
        if (!TextUtils.isEmpty(image)){
            Picasso.get().load(image).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.imvCategory);
        }else {
            Picasso.get().load(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(holder.imvCategory);
        }

        holder.txtOtherCategories.setText(json.getString("category_name"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view, json.getString("category_name"),json.getString("category_slug"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return jsonList.size();
    }

    public class OtherCategoriesViewHolder extends RecyclerView.ViewHolder {

        TextView txtOtherCategories;
        RoundedImageView imvCategory;


        public OtherCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOtherCategories = itemView.findViewById(R.id.txtOtherCategories);
            imvCategory = itemView.findViewById(R.id.imvCategory);
        }
    }

    private void loadFragment(View v, String title,String slug) {
        Bundle bundle = new Bundle();
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        bundle.putString("subTitle", title);
        bundle.putString("slug", slug);
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

