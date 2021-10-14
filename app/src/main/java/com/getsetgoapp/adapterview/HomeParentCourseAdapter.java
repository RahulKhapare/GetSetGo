package com.getsetgoapp.adapterview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.Fragment.ViewAllCategoriesFragment;
import com.getsetgoapp.Model.HomeParentModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.util.Click;

import java.util.List;

public class HomeParentCourseAdapter extends RecyclerView.Adapter<HomeParentCourseAdapter.ParentViewHolder> {

    Context context;
    JsonList jsonList;
    ViewAllCategoriesFragment viewAllCategoriesFragment;
    private List<HomeParentModel> homeParentModelList;

    public HomeParentCourseAdapter(Context context, List<HomeParentModel> homeParentModelList) {
        this.context = context;
        this.homeParentModelList = homeParentModelList;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_parent_item, viewGroup, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {

        HomeParentModel model = homeParentModelList.get(position);

        JsonList childJsonList = model.getCourses();

        holder.ParentItemTitle.setText(model.getCategory_name());

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        HomeChildAdapter childItemAdapter = new HomeChildAdapter(context, childJsonList);
        holder.ChildRecyclerView.setLayoutManager(layoutManager);
        holder.ChildRecyclerView.setNestedScrollingEnabled(false);
        holder.ChildRecyclerView.setHasFixedSize(true);
        holder.ChildRecyclerView.setAdapter(childItemAdapter);

        holder.txtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                loadFragment(view, holder.ParentItemTitle.getText().toString(), model.getCategory_slug());
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeParentModelList.size();
    }

    class ParentViewHolder extends RecyclerView.ViewHolder {

        private final TextView ParentItemTitle;
        private final TextView txtViewAll;
        private final RecyclerView ChildRecyclerView;
        private LinearLayout lnrCategoryView;
        private LinearLayout llSmartSchoolLabel;

        ParentViewHolder(final View itemView) {
            super(itemView);
            ParentItemTitle = itemView.findViewById(R.id.txtCourseName);
            txtViewAll = itemView.findViewById(R.id.txtViewAll);
            ChildRecyclerView = itemView.findViewById(R.id.recyclerViewChild);
            lnrCategoryView = itemView.findViewById(R.id.lnrCategoryView);
            llSmartSchoolLabel = itemView.findViewById(R.id.llSmartSchoolLabel);
        }
    }

    private void loadFragment(View v, String title, String slug) {
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString("subTitle", title);
        bundle.putString("slug", slug);
        bundle.putBoolean("isFromHome", true);
//        bundle.putSerializable("categoryList", list);
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

