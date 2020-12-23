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

import com.getsetgo.Adapter.IncentivesAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentUserincentiveBinding;

public class IncentivesFragment extends Fragment {

    IncentivesAdapter incentivesAdapter;
    private FragmentUserincentiveBinding binding;


    public IncentivesFragment() {
    }

    public static IncentivesFragment newInstance() {
        IncentivesFragment fragment = new IncentivesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_userincentive, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Incentives");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();
        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){
        setupRecyclerViewForIncentives();
    }
    private void setupRecyclerViewForIncentives() {
        binding.recyclerViewIncentive.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        incentivesAdapter = new IncentivesAdapter(getActivity());
        binding.recyclerViewIncentive.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewIncentive.setAdapter(incentivesAdapter);
        incentivesAdapter.notifyDataSetChanged();
    }


}