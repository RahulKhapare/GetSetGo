package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.getsetgo.Adapter.CategoriesCommonAdapter;
import com.getsetgo.Adapter.ViewAllCategoriesAdapter;
import com.getsetgo.R;

public class ViewAllCategoriesActivity extends AppCompatActivity {
    Context context;
    public RecyclerView recyclerViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_categories);
        context = ViewAllCategoriesActivity.this;
        init();
    }

    private void init() {

        recyclerViewCategory = findViewById(R.id.recyclerViewAllCategory);

        setupRecyclerViewForViewAllCategories(recyclerViewCategory);

    }

    private void setupRecyclerViewForViewAllCategories(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ViewAllCategoriesAdapter viewAllCategoriesAdapter = new ViewAllCategoriesAdapter(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAllCategoriesAdapter);
        viewAllCategoriesAdapter.notifyDataSetChanged();
    }
}