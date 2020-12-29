package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    public static MyEarningsCommonAdapter myEarningsCommonAdapter;
    public static LinearLayoutManager mLayoutManager;


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
        binding.recyclerViewMyEarning.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (EarningsFragment.pos == 0) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == EarningsFragment.courseJsonList.size() - 1) {
                        if(EarningsFragment.nextPageForCourse){
                            EarningsFragment.callCourseEarningApi(getContext());
                        }
                    }
                }
            }
        });


    }

    public static void setUpRefIncome(Json json){
        int income = json.getInt(P.income);
        binding.txtIncome.setText(String.valueOf(income));
    }

    public static void setupRecyclerViewMyEarnings(Context context, JsonList jsonList) {
        if (jsonList != null) {
            myEarningsCommonAdapter = new MyEarningsCommonAdapter(context, jsonList);
            binding.recyclerViewMyEarning.setAdapter(myEarningsCommonAdapter);
            myEarningsCommonAdapter.notifyDataSetChanged();
        }
    }


}