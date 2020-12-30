package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgo.Adapter.AllTransactionsAdapter;
import com.getsetgo.Adapter.MyEarningsCommonAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentMyearningBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.P;

import org.json.JSONArray;

public class MyEarningFragment extends Fragment {

    public static FragmentMyearningBinding binding;
    public static LinearLayoutManager mLayoutManager;
    boolean isScrolling = false;
    int currentItem, totalItems, scrollOutItems;


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
        setupRecyclerViewMyEarnings(getActivity(), EarningsFragment.courseJsonList);
        setUpRefIncome(EarningsFragment.courseJson);
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


    }

    public static void setUpRefIncome(Json json) {
        int income = json.getInt("income");
        binding.txtIncome.setText(String.valueOf(income));
    }

    public static void setupRecyclerViewMyEarnings(Context context, JsonList jsonList) {
        if (jsonList != null) {
            MyEarningsCommonAdapter myEarningsCommonAdapter = new MyEarningsCommonAdapter(context, jsonList);
            binding.recyclerViewMyEarning.setAdapter(myEarningsCommonAdapter);
            myEarningsCommonAdapter.notifyDataSetChanged();
        }
    }


}