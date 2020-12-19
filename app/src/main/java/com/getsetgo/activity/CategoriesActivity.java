package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.getsetgo.Adapter.CategoriesCommonAdapter;
import com.getsetgo.Adapter.MyCourseAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityCategoriesBinding;
import com.getsetgo.util.WindowView;

public class CategoriesActivity extends AppCompatActivity {

    CategoriesActivity activity = this;
    ActivityCategoriesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_categories);
        init();
    }

    private void init() {





        binding.txtSmartSchoolViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        setupRecyclerViewForCategories(binding.recyclerViewSmartSchool);
        setupRecyclerViewForCategories(binding.recyclerViewProfessional);
        setupRecyclerViewForCategories(binding.recyclerViewArt);
        setupRecyclerViewForCategories(binding.recyclerViewTechnology);
        setupRecyclerViewForCategories(binding.recyclerViewCreInn);
    }

    private void setupRecyclerViewForCategories(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        CategoriesCommonAdapter commonAdapter = new CategoriesCommonAdapter(activity);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(commonAdapter);
        commonAdapter.notifyDataSetChanged();
    }

    public void nextActivity(){
        startActivity(new Intent(activity,ViewAllCategoriesActivity.class));
    }
}