package com.getsetgoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentNotificationDetailsBinding;
import com.getsetgoapp.databinding.FragmentNotificationsBinding;
import com.getsetgoapp.util.Config;
import com.squareup.picasso.Picasso;

public class NotificationsDetailsFragment extends Fragment {

    FragmentNotificationDetailsBinding binding;
    boolean isFromHome;


    public NotificationsDetailsFragment() {
    }

    public static NotificationsDetailsFragment newInstance() {
        NotificationsDetailsFragment fragment = new NotificationsDetailsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_details, container, false);
        View rootView = binding.getRoot();

        isFromHome = getArguments().getBoolean("isFromHome");
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Notifications");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                onBackClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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

    private void init() {

        String image = Config.NOTIFICATION_IMAGE;
        if (!image.equals("") && !image.equals("null")) {
            Picasso.get().load(image).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(binding.imgNotification);
        } else {
            Picasso.get().load(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(binding.imgNotification);
        }

        binding.txtDescription.setText(Config.NOTIFICATION_TEXT);

        onClick();
    }


    private void onClick() {

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });

    }

    private void onBackClick() {
        getFragmentManager().popBackStackImmediate();
    }

}