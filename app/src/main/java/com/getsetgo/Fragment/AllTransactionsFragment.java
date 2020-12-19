package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Adapter.AllTransactionsAdapter;
import com.getsetgo.Adapter.CurriculumLectureAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.FragmentAllTransactionsBinding;

public class AllTransactionsFragment extends Fragment {

    AllTransactionsAdapter transactionsAdapter;
    LinearLayoutManager layoutManager;
    FragmentAllTransactionsBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_transactions, container, false);
        View rootView = binding.getRoot();

        setupRecyclerViewTransactions();

        return rootView;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerViewTransactions() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewAllTransactions.setLayoutManager(layoutManager);
        transactionsAdapter = new AllTransactionsAdapter(getActivity());
        binding.recyclerViewAllTransactions.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewAllTransactions.setAdapter(transactionsAdapter);
    }

}