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

import com.getsetgo.Adapter.InboxOutboxAdapter;
import com.getsetgo.Adapter.MyEarningsCommonAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.FragmentComposeBinding;
import com.getsetgo.databinding.FragmentInboxOutboxBinding;

public class InboxOutboxFragment extends Fragment {

    FragmentInboxOutboxBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox_outbox, container, false);
        View rootView = binding.getRoot();

        setupRecyclerviewForInboxOutBox();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerviewForInboxOutBox() {
        binding.recyclerViewInboxOutbox.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        InboxOutboxAdapter inboxAndOutboxAdapter = new InboxOutboxAdapter(getActivity());
        binding.recyclerViewInboxOutbox.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewInboxOutbox.setAdapter(inboxAndOutboxAdapter);
    }


}