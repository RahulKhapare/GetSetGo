package com.getsetgoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.getsetgoapp.Adapter.MyCrashCourseEarningsCommonAdapter;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentCrashCourseEarningBinding;

public class MyEarnCrashCourseFragment extends Fragment {

    public static FragmentCrashCourseEarningBinding binding;
    public static LinearLayoutManager mLayoutManager;
    boolean isScrolling = false;
    int currentItem,totalItems,scrollOutItems;
    static MyCrashCourseEarningsCommonAdapter myCrashCourseEarningsCommonAdapter;

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
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewCrashCourseEarning.setLayoutManager(mLayoutManager);
        binding.recyclerViewCrashCourseEarning.setItemAnimator(new DefaultItemAnimator());
        myCrashCourseEarningsCommonAdapter = new MyCrashCourseEarningsCommonAdapter(getActivity(),EarningsFragment.crashcourseJsonList);
        binding.recyclerViewCrashCourseEarning.setAdapter(myCrashCourseEarningsCommonAdapter);

        binding.recyclerViewCrashCourseEarning.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();
                //if (EarningsFragment.pos ==1 ) {
                    if (isScrolling && (currentItem + scrollOutItems == totalItems)) {
                        if(EarningsFragment.nextPageForCourse){
                            isScrolling = false;
                            EarningsFragment.callCrashCourseEarningApi(getContext());
                        }
                    }
               // }
            }
        });
    }
    public static void setUpCrashIncome(Json json){
        int income = json.getInt("income");
        binding.txtIncome.setText(String.valueOf(income));
    }

    public static void setupRecyclerViewCrashCourse() {
        myCrashCourseEarningsCommonAdapter.notifyDataSetChanged();
    }


}