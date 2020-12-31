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


    static int transactionPage = 1;
    static JsonList transactionJsonList = new JsonList();
    static boolean nextPageForTransaction = true;

    static boolean isProgress = false;

    static String startDate;
    static String endDate;

    static String actionType;
    static String incomeType;


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
        initVariable();
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);

        if (!TransactionsHistoryFragment.isFromSearch) {
            if (!TransactionsHistoryFragment.isFromTransHistory) {
                transactionJsonList.clear();
                callTransactionHistoryApi(getActivity());
            } else {
                transactionsAdapter.notifyDataSetChanged();
            }
        }
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewAllTransactions.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewAllTransactions.setLayoutManager(mLayoutManager);
        transactionsAdapter = new AllTransactionsAdapter(getActivity(), transactionJsonList);
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
                    if (nextPageForTransaction) {
                        isScrolling = false;
                        callTransactionHistoryApi(getContext());
                    }
                }
            }
        });
    }

    public static void callTransactionHistoryApi(Context context) {

        String sDate = "";
        String eDate = "";
        String action = "";
        String income = "";
        int Page = 1;
        if (startDate != null) {
            sDate = startDate;
            action = actionType;
            income = incomeType;
            Page = SearchTransactionsFragment.transPage;
        } else {
            Page = transactionPage;
        }
        if (endDate != null) {
            eDate = endDate;
        }

        LoadingDialog loadingDialog = new LoadingDialog(context);
        String apiParam = "?create_date_start=" + sDate + "&create_date_end=" + eDate + "&action_type="+action + "&income_type="+income +"&page=" + Page + "&per_page=10";

        Api.newApi(context, P.baseUrl + "all_transaction" + apiParam)
                .setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (isLoading)
                        if (!isProgress) {
                            loadingDialog.show("loading...");
                        } else
                            loadingDialog.hide();
                })
                .onError(() ->
                        MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
                            loadingDialog.dismiss();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);
                            int numRows = Json1.getInt(P.num_rows);
                            JsonList jsonList = Json1.getJsonList(P.list);
                            if (jsonList != null && !jsonList.isEmpty()) {
                                transactionJsonList.addAll(jsonList);
                                //courseJson = Json1;
                                transactionsAdapter.notifyDataSetChanged();
                                if (transactionJsonList.size() < numRows) {
                                    if (startDate != null) {
                                        SearchTransactionsFragment.transPage++;
                                    } else {
                                        transactionPage++;
                                    }
                                    nextPageForTransaction = true;
                                    isProgress = true;
                                } else {
                                    nextPageForTransaction = false;
                                    isProgress = false;
                                    transactionPage = 1;
                                }
                            }

                        }
                    }

                }).run("all_transaction");
    }

    private void initVariable() {

        if (TransactionsHistoryFragment.isFromSearch) {
            isProgress = false;
            transactionPage = 1;
            nextPageForTransaction = true;
            transactionJsonList.clear();
        } else {
            startDate = null;
            endDate = null;
        }

    }


}