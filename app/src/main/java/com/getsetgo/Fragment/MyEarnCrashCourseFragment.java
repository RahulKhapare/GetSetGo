package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Adapter.MyEarningsCommonAdapter;
import com.getsetgo.R;

public class MyEarnCrashCourseFragment extends Fragment {

    RecyclerView recyclerView;
    TextView txtCrashIncome;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myearning, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMyEarning);
        txtCrashIncome = view.findViewById(R.id.txtRefIncome);
        txtCrashIncome.setText("Crash Course Income");
        setupRecyclerViewCrashCourse();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerViewCrashCourse() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        MyEarningsCommonAdapter myEarningsCommonAdapter = new MyEarningsCommonAdapter(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myEarningsCommonAdapter);
    }

}