package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.getsetgo.Adapter.MyEarningsViewPagerAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityMyEarningBinding;
import com.google.android.material.tabs.TabLayout;

public class MyEarningActivity extends AppCompatActivity {

    MyEarningActivity activity = this;
    ActivityMyEarningBinding binding;
    MyEarningsViewPagerAdapter myEarningsViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_earning);
        init();
    }

    private void init() {
        myEarningsViewPagerAdapter = new MyEarningsViewPagerAdapter(getSupportFragmentManager());
        binding.viewPagerEarning.setAdapter(myEarningsViewPagerAdapter);
        binding.tablayoutEarnings.setupWithViewPager(binding.viewPagerEarning);
    }
}