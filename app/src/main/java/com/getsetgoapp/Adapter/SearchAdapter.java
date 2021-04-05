package com.getsetgoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.getsetgoapp.Fragment.CourseDetailFragment;
import com.getsetgoapp.Model.SearchModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.util.CheckConnection;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyCourseViewHolder> {

    Context context;
    List<SearchModel> searchModelList;

    CourseDetailFragment courseDetailFragment;

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
                if (CheckConnection.isVailable(context)){
                    loadFragment(v,model.getCourse_name(),model.getSlug());
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

    private void loadFragment(View v, String title,String slug) {
        Config.POP_HOME = true;
        Bundle bundle = new Bundle();
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        bundle.putString("title", title);
        bundle.putString("slug", slug);
        bundle.putBoolean("isFromHome", true);
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

