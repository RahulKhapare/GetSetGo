package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.getsetgo.Adapter.TransactionViewPagerAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityTransactionHistoryBinding;
import com.getsetgo.util.WindowView;
import com.google.android.material.tabs.TabLayout;

public class TransactionHistoryActivity extends AppCompatActivity {

    TransactionHistoryActivity activity = this;
    ActivityTransactionHistoryBinding binding;
    TransactionViewPagerAdapter transactionViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_history);
        init();
    }

    private void init() {
        transactionViewPagerAdapter = new TransactionViewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(transactionViewPagerAdapter);
        binding.tablayout.setupWithViewPager(binding.viewPager);
    }
}