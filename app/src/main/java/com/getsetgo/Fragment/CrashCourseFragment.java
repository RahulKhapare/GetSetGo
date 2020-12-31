package com.getsetgo.Fragment;

import android.content.Context;
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


import com.adoisstudio.helper.JsonList;



import com.getsetgo.Adapter.CrashTransactionsAdapter;
import com.getsetgo.R;

import com.getsetgo.databinding.FragmentCrashCourseTransBinding;



public class CrashCourseFragment extends Fragment {

    LinearLayoutManager layoutManager;
    public static FragmentCrashCourseTransBinding binding;
    public static CrashTransactionsAdapter transactionsAdapter;

    boolean isScrolling = false;
    int currentItem, totalItems, scrollOutItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_crash_course_trans, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public void init() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewCrashTans.setLayoutManager(layoutManager);
        binding.recyclerViewCrashTans.setItemAnimator(new DefaultItemAnimator());
        transactionsAdapter = new CrashTransactionsAdapter(getActivity(), TransactionsHistoryFragment.crashJsonList);
        binding.recyclerViewCrashTans.setAdapter(transactionsAdapter);

        binding.recyclerViewCrashTans.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                currentItem = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItem + scrollOutItems) >= totalItems) {
                    if (TransactionsHistoryFragment.nextPageForCrash) {
                        isScrolling = false;
                        TransactionsHistoryFragment.callCrashTransactionApi(getContext());
                    }
                }
            }
        });
    }

    public static void setupRecyclerViewCrash() {
        transactionsAdapter.notifyDataSetChanged();
    }

}