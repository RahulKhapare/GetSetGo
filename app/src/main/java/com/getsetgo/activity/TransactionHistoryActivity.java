package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.getsetgo.Adapter.TransactionViewPagerAdapter;
import com.getsetgo.R;
import com.google.android.material.tabs.TabLayout;

public class TransactionHistoryActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TransactionViewPagerAdapter transactionViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        transactionViewPagerAdapter = new TransactionViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(transactionViewPagerAdapter);
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}