package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgo.Adapter.MyCrashCourseEarningsCommonAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentCrashCourseEarningBinding;
import com.getsetgo.util.Config;
import com.getsetgo.util.P;

public class MyEarnCrashCourseFragment extends Fragment {

    public static FragmentCrashCourseEarningBinding binding;
    public static LinearLayoutManager mLayoutManager;
    public static MyCrashCourseEarningsCommonAdapter myCrashCourseEarningsCommonAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_crash_course_earning, container, false);
        View rootView = binding.getRoot();
        init();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);
        binding.recyclerViewCrashCourseEarning.setLayoutManager(mLayoutManager);
        binding.recyclerViewCrashCourseEarning.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewCrashCourseEarning.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (EarningsFragment.pos == 1) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == 15 - 1) {
                         EarningsFragment.callCrashCourseEarningApi(getContext());
                    }
                }
            }
        });
    }
    public static void setUpCrashIncome(Json json){
        int income = json.getInt(P.income);
        binding.txtIncome.setText(String.valueOf(income));
    }

    public static void setupRecyclerViewCrashCourse(Context context,JsonList jsonList) {
        if (jsonList != null) {
            myCrashCourseEarningsCommonAdapter = new MyCrashCourseEarningsCommonAdapter(context,jsonList);
            binding.recyclerViewCrashCourseEarning.setAdapter(myCrashCourseEarningsCommonAdapter);
            myCrashCourseEarningsCommonAdapter.notifyDataSetChanged();
        }
    }


}