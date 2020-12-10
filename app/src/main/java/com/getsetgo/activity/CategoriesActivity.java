package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
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

public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    public RecyclerView recyclerViewSmartSchool, recyclerViewCreInn,
            recyclerViewTechnology, recyclerViewArt, recyclerViewProfessional;
    public TextView txtSS, txtA, txtProf, txtTech, txtCreInn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        context = CategoriesActivity.this;
        init();
    }

    private void init() {
        recyclerViewSmartSchool = findViewById(R.id.recyclerViewSmartSchool);
        recyclerViewProfessional = findViewById(R.id.recyclerViewProfessional);
        recyclerViewArt = findViewById(R.id.recyclerViewArt);
        recyclerViewTechnology = findViewById(R.id.recyclerViewTechnology);
        recyclerViewCreInn = findViewById(R.id.recyclerViewCreInn);

        txtSS = findViewById(R.id.txtSmartSchoolViewAll);
        txtA = findViewById(R.id.txtArtViewAll);
        txtProf = findViewById(R.id.txtProfessionalViewAll);
        txtTech = findViewById(R.id.txtTechnologyViewAll);
        txtCreInn = findViewById(R.id.txtCreInnViewAll);

        txtSS.setOnClickListener(this);
        txtA.setOnClickListener(this);
        txtTech.setOnClickListener(this);
        txtCreInn.setOnClickListener(this);
        txtProf.setOnClickListener(this);


        setupRecyclerViewForCategories(recyclerViewSmartSchool);
        setupRecyclerViewForCategories(recyclerViewProfessional);
        setupRecyclerViewForCategories(recyclerViewArt);
        setupRecyclerViewForCategories(recyclerViewTechnology);
        setupRecyclerViewForCategories(recyclerViewCreInn);
    }

    private void setupRecyclerViewForCategories(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CategoriesCommonAdapter commonAdapter = new CategoriesCommonAdapter(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(commonAdapter);
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.txtArtViewAll:
                nextActivity();
                break;

            case R.id.txtProfessionalViewAll:
                nextActivity();
                break;

            case R.id.txtSmartSchoolViewAll:
                nextActivity();
                break;

            case R.id.txtCreInnViewAll:
                nextActivity();
                break;

            case R.id.txtTechnologyViewAll:
                nextActivity();
                break;
        }
    }

    public void nextActivity(){
        startActivity(new Intent(context,ViewAllCategoriesActivity.class));
    }
}