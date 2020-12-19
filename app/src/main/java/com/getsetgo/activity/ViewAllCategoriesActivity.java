package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.getsetgo.Adapter.CategoriesCommonAdapter;
import com.getsetgo.Adapter.ViewAllCategoriesAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityViewAllCategoriesBinding;
import com.getsetgo.util.WindowView;

public class ViewAllCategoriesActivity extends AppCompatActivity {
    ViewAllCategoriesActivity activity = this;
    ActivityViewAllCategoriesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_all_categories);
        init();
    }

    private void init() {

        setupRecyclerViewForViewAllCategories(binding.recyclerViewAllCategory);

    }

    private void setupRecyclerViewForViewAllCategories(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ViewAllCategoriesAdapter viewAllCategoriesAdapter = new ViewAllCategoriesAdapter(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAllCategoriesAdapter);
        viewAllCategoriesAdapter.notifyDataSetChanged();
    }
}