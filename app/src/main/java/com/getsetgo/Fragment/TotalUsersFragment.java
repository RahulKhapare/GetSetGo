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

import com.getsetgo.Adapter.TotalUserAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentSearchUseridBinding;
import com.getsetgo.databinding.FragmentTotalUsersBinding;

public class TotalUsersFragment extends Fragment {

    FragmentTotalUsersBinding binding;
    TotalUserAdapter totalUserAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_total_users, container, false);
        View rootView = binding.getRoot();

        String myValue = this.getArguments().getString("titleText");
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText(myValue);
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);

        init();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {
        setupRecyclerViewForTotalUser();
    }

    private void onClick() {

    }

    private void setupRecyclerViewForTotalUser() {
        binding.recyclerViewTotalUser.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        totalUserAdapter = new TotalUserAdapter(getActivity());
        binding.recyclerViewTotalUser.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewTotalUser.setAdapter(totalUserAdapter);
        totalUserAdapter.notifyDataSetChanged();
    }


}