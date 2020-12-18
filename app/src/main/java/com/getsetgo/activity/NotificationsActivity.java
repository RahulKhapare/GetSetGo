package com.getsetgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Adapter.CategoriesCommonAdapter;
import com.getsetgo.Adapter.NotificationAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBankDetailsBinding;
import com.getsetgo.databinding.ActivityNotificationsBinding;
import com.getsetgo.util.WindowView;

public class NotificationsActivity extends AppCompatActivity {

    private NotificationsActivity activity = this;
    private ActivityNotificationsBinding binding;
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notifications);
        init();
    }
    private void init(){
        onClick();
        setupRecyclerViewForNotification();
    }
    private void onClick(){

    }

    private void setupRecyclerViewForNotification() {
        binding.recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notificationAdapter = new NotificationAdapter(this);
        binding.recyclerViewNotifications.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewNotifications.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
    }
}