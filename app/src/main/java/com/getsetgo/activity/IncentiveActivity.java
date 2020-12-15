package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.getsetgo.Adapter.IncentivesAdapter;
import com.getsetgo.Adapter.MyCourseAdapter;
import com.getsetgo.R;

public class IncentiveActivity extends AppCompatActivity {

    Context context;
    public RecyclerView recyclerView;
    IncentivesAdapter incentivesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incentive);
        context = IncentiveActivity.this;
        init();
    }
    private void init(){
        recyclerView = findViewById(R.id.recyclerViewIncentive);
        setupRecyclerViewForIncentives();
    }
    private void setupRecyclerViewForIncentives() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        incentivesAdapter = new IncentivesAdapter(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(incentivesAdapter);
        incentivesAdapter.notifyDataSetChanged();
    }
}