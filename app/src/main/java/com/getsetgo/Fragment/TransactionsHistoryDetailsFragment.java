package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.getsetgo.Adapter.TransactionViewPagerAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentTransactionsBinding;
import com.getsetgo.databinding.FragmentTransactionsHistoryDetailsBinding;

public class TransactionsHistoryDetailsFragment extends Fragment {

    FragmentTransactionsHistoryDetailsBinding binding;

    public TransactionsHistoryDetailsFragment() {
    }

    public static TransactionsHistoryDetailsFragment newInstance() {
        TransactionsHistoryDetailsFragment fragment = new TransactionsHistoryDetailsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transactions_history_details, container, false);
        View rootView = binding.getRoot();

        init();
        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Transaction History");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);

        binding.btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }


}