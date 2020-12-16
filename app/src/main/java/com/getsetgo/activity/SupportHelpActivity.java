package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.getsetgo.Adapter.SupportHelpViewPagerAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityAddNewUserBinding;
import com.getsetgo.databinding.ActivitySupportHelpBinding;
import com.getsetgo.util.WindowView;
import com.google.android.material.tabs.TabLayout;

public class SupportHelpActivity extends AppCompatActivity {

    private SupportHelpActivity activity = this;
    private ActivitySupportHelpBinding binding;
    SupportHelpViewPagerAdapter supportHelpViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_support_help);
        initView();
    }

    private void initView() {
        supportHelpViewPagerAdapter = new SupportHelpViewPagerAdapter(getSupportFragmentManager());
        binding.viewPagerSupportHelp.setAdapter(supportHelpViewPagerAdapter);
        binding.tablayoutSupport.setupWithViewPager(binding.viewPagerSupportHelp);
    }
}