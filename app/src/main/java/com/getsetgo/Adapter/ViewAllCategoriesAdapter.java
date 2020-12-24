package com.getsetgo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.daimajia.swipe.SwipeLayout;
import com.getsetgo.Fragment.CourseDetailFragment;
import com.getsetgo.Fragment.ViewAllCategoriesFragment;
import com.getsetgo.R;
import com.getsetgo.util.P;
import com.makeramen.roundedimageview.RoundedImageView;

public class ViewAllCategoriesAdapter extends RecyclerView.Adapter<ViewAllCategoriesAdapter.ViewAllCategoriesViewHolder> {

    Context context;
    CourseDetailFragment courseDetailFragment;

    Bundle bundle = new Bundle();
    Json json = new Json();
    String string = null;
    int it;

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view);
            }
        });


    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewAllCategoriesViewHolder extends RecyclerView.ViewHolder {

        TextView txtCourse, txtProfName, txtReview,
                txtNewPrice, txtOldPrice, txtBestSeller;
        RoundedImageView imgCategory;
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

    private void loadFragment(View v) {
        if (courseDetailFragment == null)
            string = P.baseUrl + "series_check/" + json.getString(P.series_slug) + "/" + json.getString(P.video_slug);
        it = json.getInt(P.time);
        it *= 1000;
        bundle.putString(P.url, string);
        bundle.putInt("videoProgress", it);

        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        courseDetailFragment = new CourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}

