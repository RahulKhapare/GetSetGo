package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.getsetgo.Adapter.IncentivesAdapter;
import com.getsetgo.Adapter.MyCourseAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBankDetailsBinding;
import com.getsetgo.databinding.ActivityIncentiveBinding;
import com.getsetgo.util.WindowView;

public class IncentiveActivity extends AppCompatActivity {

    IncentivesAdapter incentivesAdapter;
    private IncentiveActivity activity = this;
    private ActivityIncentiveBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_incentive);
        init();
    }
    private void init(){
        setupRecyclerViewForIncentives();
    }
    private void setupRecyclerViewForIncentives() {
        binding.recyclerViewIncentive.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        incentivesAdapter = new IncentivesAdapter(this);
        binding.recyclerViewIncentive.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewIncentive.setAdapter(incentivesAdapter);
        incentivesAdapter.notifyDataSetChanged();
    }
}