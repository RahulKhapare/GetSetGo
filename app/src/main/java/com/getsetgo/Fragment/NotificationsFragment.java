package com.getsetgo.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getsetgo.Adapter.NotificationAdapter;
import com.getsetgo.Model.NotificationModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentNotificationsBinding;
import com.getsetgo.databinding.FragmentSearchBinding;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    FragmentNotificationsBinding binding;
    NotificationAdapter notificationAdapter;


    public NotificationsFragment() {
    }

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Notifications");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();
        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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


        binding.recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        notificationAdapter = new NotificationAdapter(getContext(),notificationModelArrayList);
        binding.recyclerViewNotifications.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewNotifications.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
    }


}