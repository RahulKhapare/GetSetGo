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

import com.getsetgo.Adapter.NotificationAdapter;
import com.getsetgo.Adapter.TransactionViewPagerAdapter;
import com.getsetgo.Model.NotificationModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentNotificationsBinding;
import com.getsetgo.databinding.FragmentTransactionsBinding;

import java.util.ArrayList;

public class TransactionsHistoryFragment extends Fragment {

    FragmentTransactionsBinding binding;
    TransactionViewPagerAdapter transactionViewPagerAdapter;



    public TransactionsHistoryFragment() {
    }

    public static TransactionsHistoryFragment newInstance() {
        TransactionsHistoryFragment fragment = new TransactionsHistoryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transactions, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Transactions History");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);

        init();
        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {
        transactionViewPagerAdapter = new TransactionViewPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(transactionViewPagerAdapter);
        binding.tablayout.setupWithViewPager(binding.viewPager);
    }


}