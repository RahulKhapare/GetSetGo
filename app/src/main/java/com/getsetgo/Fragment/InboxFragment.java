package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Adapter.InboxAdapter;

import com.getsetgo.R;

import com.getsetgo.databinding.FragmentInboxOutboxBinding;

public class InboxFragment extends Fragment {

    FragmentInboxOutboxBinding binding;
    static InboxAdapter inboxAdapter;
    LinearLayoutManager layoutManager;
    boolean isScrolling = false;
    int currentItem, totalItems, scrollOutItems;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox_outbox, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init()
    {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewInboxOutbox.setLayoutManager(layoutManager);
        binding.recyclerViewInboxOutbox.setItemAnimator(new DefaultItemAnimator());
        inboxAdapter = new InboxAdapter(context,HelpAndSupportFragment.inboxJsonList);
        binding.recyclerViewInboxOutbox.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewInboxOutbox.setAdapter(inboxAdapter);

        binding.recyclerViewInboxOutbox.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                currentItem = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItem + scrollOutItems) >= totalItems) {
                    if (HelpAndSupportFragment.nextPageForinbox) {
                        isScrolling = false;
                        HelpAndSupportFragment.callSupportInboxApi(context);
                    }
                }
            }
        });
    }
    public static void setupRecyclerviewForInbox() {
        inboxAdapter.notifyDataSetChanged();
    }


}