package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("My Earnings");

        init();
        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void init() {
        myEarningsViewPagerAdapter = new MyEarningsViewPagerAdapter(getChildFragmentManager());
        binding.viewPagerEarning.setAdapter(myEarningsViewPagerAdapter);
        binding.tablayoutEarnings.setupWithViewPager(binding.viewPagerEarning);
    }


}