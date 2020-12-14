package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Adapter.AllTransactionsAdapter;
import com.getsetgo.Adapter.CurriculumLectureAdapter;
import com.getsetgo.R;

public class AllTransactionsFragment extends Fragment {

    RecyclerView recyclerView;
    AllTransactionsAdapter transactionsAdapter;
    LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_all_transactions, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewAllTransactions);

        setupRecyclerViewTransactions();

        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerViewTransactions() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        transactionsAdapter = new AllTransactionsAdapter(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(transactionsAdapter);
    }

}