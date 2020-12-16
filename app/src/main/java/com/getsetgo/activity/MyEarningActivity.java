package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.getsetgo.Adapter.MyEarningsViewPagerAdapter;
import com.getsetgo.R;
import com.google.android.material.tabs.TabLayout;

public class MyEarningActivity extends AppCompatActivity {

    Context context;
    TabLayout tabLayout;
    ViewPager viewPager;
    MyEarningsViewPagerAdapter myEarningsViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_earning);
        context = MyEarningActivity.this;
        init();
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.viewPagerEarning);
        myEarningsViewPagerAdapter = new MyEarningsViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myEarningsViewPagerAdapter);
        tabLayout = (TabLayout)findViewById(R.id.tablayoutEarnings);
        tabLayout.setupWithViewPager(viewPager);
    }
}