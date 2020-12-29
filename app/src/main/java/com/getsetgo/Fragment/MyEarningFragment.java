package com.getsetgo.Fragment;

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

public class MyEarningFragment extends Fragment {

    FragmentMyearningBinding binding;

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

        setupRecyclerViewMyEarnings();
        binding.recyclerViewMyEarning.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                EarningsFragment.callCourseEarningApi(getContext());
            }
        });

    }

    private void setupRecyclerViewMyEarnings() {
        binding.recyclerViewMyEarning.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        MyEarningsCommonAdapter myEarningsCommonAdapter = new MyEarningsCommonAdapter(getActivity());
        binding.recyclerViewMyEarning.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewMyEarning.setAdapter(myEarningsCommonAdapter);
    }


}