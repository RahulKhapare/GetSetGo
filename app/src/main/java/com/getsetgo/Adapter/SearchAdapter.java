package com.getsetgo.Adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.Fragment.MyCourseDetailFragment;
import com.getsetgo.Model.SearchModel;
import com.getsetgo.R;
import com.getsetgo.util.CheckConnection;
import com.getsetgo.util.Click;
import com.getsetgo.util.Config;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyCourseViewHolder> {

    Context context;
    List<SearchModel> searchModelList;

    MyCourseDetailFragment courseDetailFragment;
    Bundle bundle = new Bundle();

    public SearchAdapter(Context context, List<SearchModel> searchModelList) {
        this.context = context;
        this.searchModelList = searchModelList;
    }

    @NonNull
    @Override
    public SearchAdapter.MyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_view, parent, false);
        return new  MyCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyCourseViewHolder holder, int position) {
        SearchModel model = searchModelList.get(position);

        holder.txtSearch.setText(model.getCourse_name());
        holder.txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                String courseSlug = model.getSlug();
                String courseName = model.getCourse_name();
                if (CheckConnection.isVailable(context)){
                    loadFragment(v,courseSlug,courseName);
                }else {
                    H.showMessage(context,"No internet connection available");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchModelList.size();
    }

    public class MyCourseViewHolder extends RecyclerView.ViewHolder{

        TextView txtSearch;

        public MyCourseViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSearch = itemView.findViewById(R.id.txtSearch);
        }
    }

    private void loadFragment(View v, String courseSlug,String courseName) {
        Config.myCourseSlug = courseSlug;
        Config.myCourseTitle = courseName;
        bundle.putString("slug",courseSlug);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        if (courseDetailFragment == null)
            courseDetailFragment = new MyCourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}

