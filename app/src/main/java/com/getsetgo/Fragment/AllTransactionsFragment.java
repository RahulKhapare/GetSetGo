package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Adapter.AllTransactionsAdapter;
import com.getsetgo.Adapter.CurriculumLectureAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentAllTransactionsBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.P;

public class AllTransactionsFragment extends Fragment {

    private FragmentAllTransactionsBinding binding;
    private LinearLayoutManager mLayoutManager;
    static AllTransactionsAdapter transactionsAdapter;
    boolean isScrolling = false;
    int currentItem, totalItems, scrollOutItems;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_transactions, container, false);
        View rootView = binding.getRoot();
        init(rootView);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view) {
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);


        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewAllTransactions.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewAllTransactions.setLayoutManager(mLayoutManager);
        transactionsAdapter = new AllTransactionsAdapter(getActivity(), TransactionsHistoryFragment.transactionJsonList);
        binding.recyclerViewAllTransactions.setAdapter(transactionsAdapter);


        binding.recyclerViewAllTransactions.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                currentItem = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItem + scrollOutItems) >= totalItems) {
                    if (TransactionsHistoryFragment.nextPageForTransaction) {
                        isScrolling = false;
                        TransactionsHistoryFragment.callTransactionHistoryApi(getContext());
                    }
                }
            }
        });
    }

    public static void setUpRecuclerviewAllTransactions(){
        transactionsAdapter.notifyDataSetChanged();
    }

}