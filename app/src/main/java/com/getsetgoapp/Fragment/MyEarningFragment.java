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


import com.getsetgoapp.Adapter.MyEarningsCommonAdapter;
import com.getsetgoapp.R;

import com.getsetgoapp.databinding.FragmentMyearningBinding;


public class MyEarningFragment extends Fragment {

    public static FragmentMyearningBinding binding;
    public static LinearLayoutManager mLayoutManager;
    boolean isScrolling = false;
    int currentItem, totalItems, scrollOutItems;
    static MyEarningsCommonAdapter myEarningsCommonAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myearning, container, false);
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
        binding.recyclerViewMyEarning.setLayoutManager(mLayoutManager);
        binding.recyclerViewMyEarning.setItemAnimator(new DefaultItemAnimator());
        myEarningsCommonAdapter = new MyEarningsCommonAdapter(getActivity(), EarningsFragment.courseJsonList);
        binding.recyclerViewMyEarning.setAdapter(myEarningsCommonAdapter);

        binding.recyclerViewMyEarning.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();
                //if (EarningsFragment.pos == 0) {

                if (isScrolling && (currentItem + scrollOutItems) >= totalItems) {
                    if (EarningsFragment.nextPageForCourse) {
                        isScrolling = false;
                        EarningsFragment.callCourseEarningApi(getContext());
                    }
                }
                //}
            }
        });

        try {
            setUpRefIncome(EarningsFragment.courseJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setUpRefIncome(Json json) {
        String income = json.getString("income");
        binding.txtIncome.setText(income);
    }

    public static void setupRecyclerViewMyEarnings() {
            myEarningsCommonAdapter.notifyDataSetChanged();
    }


}