package com.getsetgo.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.H;
import com.getsetgo.Adapter.NotificationAdapter;
import com.getsetgo.Model.NotificationModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentNotificationsBinding;
import com.getsetgo.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    FragmentNotificationsBinding binding;
    NotificationAdapter notificationAdapter;
    List<NotificationModel> notificationModelList;
    boolean isFromBottom;


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

        isFromBottom = getArguments().getBoolean("isFromBottom");
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Notifications");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

               onBackClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){

        notificationModelList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(),notificationModelList);
        binding.recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewNotifications.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewNotifications.setAdapter(notificationAdapter);

        onClick();
        setData();

    }

    private void onClick(){

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    BaseScreenActivity.callBack();
//                }

                onBackClick();
            }
        });

    }

    private void setData() {

        NotificationModel model = new NotificationModel();
        model.setId("1");
        model.setTitle("Notification Title");
        model.setDescription("Here will be notification description");
        model.setImage("null");
        model.setSlug("super-brain-booster");
        model.setCourseName("Student's Brain Booster");

        notificationModelList.add(model);
        notificationModelList.add(model);
        notificationModelList.add(model);
        notificationModelList.add(model);
        notificationModelList.add(model);

        notificationAdapter.notifyDataSetChanged();
    }


    private void onBackClick(){
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            if (isFromBottom) {
                getFragmentManager().popBackStackImmediate();
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.VISIBLE);
                BaseScreenActivity.binding.bottomNavigation.setSelectedItemId(R.id.menu_Account);
            } else {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                BaseScreenActivity.callBack();
            }
        }
    }

}