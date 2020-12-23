package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.getsetgo.Adapter.SupportHelpViewPagerAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentHelpandsupportBinding;

public class HelpAndSupportFragment extends Fragment {

    private FragmentHelpandsupportBinding binding;
    SupportHelpViewPagerAdapter supportHelpViewPagerAdapter;


    public HelpAndSupportFragment() {
    }

    public static HelpAndSupportFragment newInstance() {
        HelpAndSupportFragment fragment = new HelpAndSupportFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_helpandsupport, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Support/Help");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        initView();
        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void initView() {
        setupRecyclerview();
    }

    private void setupRecyclerview() {
        supportHelpViewPagerAdapter = new SupportHelpViewPagerAdapter(getChildFragmentManager());
        binding.viewPagerSupportHelp.setAdapter(supportHelpViewPagerAdapter);
        binding.tablayoutSupport.setupWithViewPager(binding.viewPagerSupportHelp);
    }


}