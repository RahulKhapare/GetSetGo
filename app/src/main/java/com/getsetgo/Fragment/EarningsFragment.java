package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getsetgo.Adapter.MyEarningsViewPagerAdapter;
import com.getsetgo.Adapter.NotificationAdapter;
import com.getsetgo.Model.NotificationModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentEarningsBinding;
import com.getsetgo.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;

public class EarningsFragment extends Fragment {

    FragmentEarningsBinding binding;
    MyEarningsViewPagerAdapter myEarningsViewPagerAdapter;
    SearchEarningsFragment searchEarningsFragment;

    public EarningsFragment() {
    }

    public static EarningsFragment newInstance() {
        EarningsFragment fragment = new EarningsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_earnings, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Course Earnings");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);

        init(rootView);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View view) {
        String tab = this.getArguments().getString("tabItem");
        myEarningsViewPagerAdapter = new MyEarningsViewPagerAdapter(getChildFragmentManager());
        binding.viewPagerEarning.setAdapter(myEarningsViewPagerAdapter);
        if (tab.equalsIgnoreCase("Crash Course Earnings")) {
            binding.viewPagerEarning.setCurrentItem(1);
        } else if (tab.equalsIgnoreCase("Total Earnings")) {
            binding.viewPagerEarning.setCurrentItem(2);
        } else {
            binding.viewPagerEarning.setCurrentItem(0);
        }
        binding.tablayoutEarnings.setupWithViewPager(binding.viewPagerEarning);

        BaseScreenActivity.binding.incFragmenttool.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    private void loadFragment(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        searchEarningsFragment = new SearchEarningsFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchEarningsFragment)
                .addToBackStack(null)
                .commit();

    }


}