package com.getsetgoapp.adapterview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.Fragment.ViewAllCategoriesFragment;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;

public class HomeParentCourseAdapter extends RecyclerView
        .Adapter<HomeParentCourseAdapter.ParentViewHolder> {

    public RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    Context context;
    JsonList jsonList;
    ViewAllCategoriesFragment viewAllCategoriesFragment;

    public HomeParentCourseAdapter(Context context, JsonList jsonList) {
        this.context = context;
        this.jsonList = jsonList;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_parent_item, viewGroup, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ParentViewHolder holder,
            int position) {

        Json json = jsonList.get(position);
        JsonList childJsonList = new JsonList();
//        childJsonList = json.getJsonList("courses");
        childJsonList = json.getJsonList("courses");

        holder.ParentItemTitle.setText(json.getString("category_name"));
        JsonList finalChildJsonList = childJsonList;
        holder.txtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view, holder.ParentItemTitle.getText().toString(), json.getString("category_slug"));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.ChildRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setInitialPrefetchItemCount(childJsonList.size());
        HomeChildAdapter childItemAdapter = new HomeChildAdapter(context, childJsonList);
        holder.ChildRecyclerView.setLayoutManager(layoutManager);
        holder.ChildRecyclerView.setAdapter(childItemAdapter);
        holder.ChildRecyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {

        return jsonList.size();
    }

    class ParentViewHolder
            extends RecyclerView.ViewHolder {

        private final TextView ParentItemTitle;
        private final TextView txtViewAll;
        private final RecyclerView ChildRecyclerView;

        ParentViewHolder(final View itemView) {
            super(itemView);

            ParentItemTitle = itemView.findViewById(R.id.txtCourseName);
            txtViewAll = itemView.findViewById(R.id.txtViewAll);
            ChildRecyclerView = itemView.findViewById(R.id.recyclerViewChild);
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

