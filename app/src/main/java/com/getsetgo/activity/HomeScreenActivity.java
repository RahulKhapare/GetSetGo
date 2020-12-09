package com.getsetgo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.getsetgo.Adapter.ActiveCourseAdapter;
import com.getsetgo.Adapter.BestSellingCourseAdapter;
import com.getsetgo.Adapter.OtherCategoriesAdapter;
import com.getsetgo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    public ImageView ivNotify, ivMenu, ivCourseImage;
    public TextView txtTech, txtDesciption, txtProgramme, txtStatus, txtViewAll, txtViewAllBestCourse;
    public RecyclerView recyclerViewCources, recyclerViewOtherCategories, recyclerBestSellingCources;
    BottomNavigationView bottomNavigationView;
    CardView cardViewCurrentLearning;
    ActiveCourseAdapter activeCourseAdapter;
    OtherCategoriesAdapter otherCategoriesAdapter;
    BestSellingCourseAdapter bestSellingCourseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        context = HomeScreenActivity.this;
        init();
    }

    private void init() {
        ivNotify = findViewById(R.id.ivNotify);
        ivMenu = findViewById(R.id.ivMenu);
        ivCourseImage = findViewById(R.id.imvCourse);
        txtTech = findViewById(R.id.textTech);
        txtDesciption = findViewById(R.id.txtDes);
        txtProgramme = findViewById(R.id.txtProgramme);
        txtViewAll = findViewById(R.id.txtViewAll);
        txtStatus = findViewById(R.id.txtStatus);
        recyclerViewCources = findViewById(R.id.recyclerViewCources);
        txtViewAllBestCourse = findViewById(R.id.txtViewAllBestCourse);
        cardViewCurrentLearning = findViewById(R.id.cardViewCurrentLearning);
        recyclerBestSellingCources = findViewById(R.id.recyclerBestSellingCources);
        recyclerViewOtherCategories = findViewById(R.id.recyclerViewOtherCategories);

        txtViewAllBestCourse.setOnClickListener(this);
        txtViewAll.setOnClickListener(this);
        ivNotify.setOnClickListener(this);

        setupRecyclerViewForActiveCourse();
        setupRecyclerViewForOthersCategories();
        setupRecyclerViewForBestSellingCourse();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ivNotify:
                Intent intent = new Intent(context,MyCourseActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;

                case R.id.txtViewAll:
                break;

            case R.id.txtViewAllBestCourse:
                break;

            case R.id.cardViewCurrentLearning:
                break;
        }
    }

    private void setupRecyclerViewForActiveCourse() {
        recyclerViewCources.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activeCourseAdapter = new ActiveCourseAdapter(this);
        recyclerViewCources.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCources.setAdapter(activeCourseAdapter);
        activeCourseAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewForOthersCategories() {
        recyclerViewOtherCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        otherCategoriesAdapter = new OtherCategoriesAdapter(this);
        recyclerViewOtherCategories.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOtherCategories.setAdapter(otherCategoriesAdapter);
        otherCategoriesAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewForBestSellingCourse() {
        recyclerBestSellingCources.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bestSellingCourseAdapter = new BestSellingCourseAdapter(this);
        recyclerBestSellingCources.setItemAnimator(new DefaultItemAnimator());
        recyclerBestSellingCources.setAdapter(bestSellingCourseAdapter);
        bestSellingCourseAdapter.notifyDataSetChanged();
    }
}