package com.getsetgo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.Fragment.CourseDetailFragment;
import com.getsetgo.R;
import com.getsetgo.util.P;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class ViewAllCategoriesAdapter extends RecyclerView.Adapter<ViewAllCategoriesAdapter.ViewAllCategoriesViewHolder> {

    Context context;
    JsonList jsonList;
    CourseDetailFragment courseDetailFragment;
    Boolean isAdded = false;

    Bundle bundle = new Bundle();
    Json json = new Json();
    String string = null;
    int it;

    public ViewAllCategoriesAdapter(Context context, JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public ViewAllCategoriesAdapter.ViewAllCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_viewall_categories, parent, false);
        return new ViewAllCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllCategoriesAdapter.ViewAllCategoriesViewHolder holder, int position) {

        final Json json = jsonList.get(position);

        holder.txtCourse.setText(jsonList.get(position).getString("course_name"));
        holder.txtProfName.setText(jsonList.get(position).getString("instructor_name"));
        holder.txtOldPrice.setText("₹ " + jsonList.get(position).getString("price"));
        holder.txtNewPrice.setText("₹ " + jsonList.get(position).getString("sale_price"));
        holder.txtReview.setText(jsonList.get(position).getString("rating"));

        Picasso.get().load(json.getString("image")).placeholder(R.drawable.ic_wp).error(R.drawable.ic_wp).into(holder.imgCategory);


        /*holder.chkFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.chkFav.setSelected(true);


            }
        });*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        String courseSlug = jsonList.get(position).getString("slug");
                Log.d("Hardik","slug: "+courseSlug);
                loadFragment(view,courseSlug);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jsonList.size();
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
            txtBestSeller.setVisibility(View.INVISIBLE);
            imgCategory = itemView.findViewById(R.id.imvViewCategory);
            chkFav = itemView.findViewById(R.id.chkViewCategoryFavourites);

        }
    }

    private void loadFragment(View v, String courseSlug) {
//        string = P.baseUrl + "series_check/" + json.getString(P.series_slug) + "/" + json.getString(P.video_slug);
//        it = json.getInt(P.time);
//        it *= 1000;
//        bundle.putString(P.url, string);
//        bundle.putInt("videoProgress", it);
        bundle.putString("slug",courseSlug);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        if (courseDetailFragment == null)
            courseDetailFragment = new CourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}

