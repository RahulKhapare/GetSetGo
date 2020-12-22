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
import com.getsetgo.Model.NotificationModel;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBankDetailsBinding;
import com.getsetgo.databinding.ActivityNotificationsBinding;
import com.getsetgo.util.WindowView;

import java.util.ArrayList;

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
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        NotificationModel n = new NotificationModel(1);
        NotificationModel no = new NotificationModel(1);
        NotificationModel not = new NotificationModel(2);
        NotificationModel noti = new NotificationModel(1);
        NotificationModel notif = new NotificationModel(3);
        NotificationModel notifi = new NotificationModel(2);
        NotificationModel notific = new NotificationModel(1);
        NotificationModel notifica = new NotificationModel(1);
        NotificationModel notificat = new NotificationModel(3);
        NotificationModel notificati = new NotificationModel(1);
        NotificationModel notificatio = new NotificationModel(2);
        NotificationModel notification = new NotificationModel(1);
        notificationModelArrayList.add(n);
        notificationModelArrayList.add(no);
        notificationModelArrayList.add(not);
        notificationModelArrayList.add(noti);
        notificationModelArrayList.add(notif);
        notificationModelArrayList.add(notifi);
        notificationModelArrayList.add(notific);
        notificationModelArrayList.add(notifica);
        notificationModelArrayList.add(notificat);
        notificationModelArrayList.add(notificati);
        notificationModelArrayList.add(notificatio);
        notificationModelArrayList.add(notification);


        binding.recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notificationAdapter = new NotificationAdapter(this,notificationModelArrayList);
        binding.recyclerViewNotifications.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewNotifications.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
    }
}