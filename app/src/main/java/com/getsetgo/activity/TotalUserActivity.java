package com.getsetgo.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Adapter.IncentivesAdapter;
import com.getsetgo.Adapter.TotalUserAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBankDetailsBinding;
import com.getsetgo.databinding.ActivityTotalUserBinding;
import com.getsetgo.util.WindowView;

public class TotalUserActivity extends AppCompatActivity {

    TotalUserAdapter totalUserAdapter;
    private TotalUserActivity activity = this;
    private ActivityTotalUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_total_user);
        init();
    }
    private void init(){

        String data = getIntent().getExtras().getString("titleText","Total User");
        binding.txtUserTitle.setText(data);
        setupRecyclerViewForTotalUser();
    }
    private void setupRecyclerViewForTotalUser() {
        binding.recyclerViewTotalUser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        totalUserAdapter = new TotalUserAdapter(this);
        binding.recyclerViewTotalUser.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewTotalUser.setAdapter(totalUserAdapter);
        totalUserAdapter.notifyDataSetChanged();
    }
}