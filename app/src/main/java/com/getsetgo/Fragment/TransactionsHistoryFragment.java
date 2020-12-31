package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;


import com.getsetgo.Adapter.TransactionViewPagerAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;

import com.getsetgo.databinding.FragmentTransactionsBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.P;
import com.google.android.material.tabs.TabLayout;


public class TransactionsHistoryFragment extends Fragment {

    FragmentTransactionsBinding binding;
    TransactionViewPagerAdapter transactionViewPagerAdapter;
    SearchTransactionsFragment searchTransactionsFragment;
    Context context;

    public static int CrashPage = 1;

    public static JsonList crashJsonList = new JsonList();

    public static boolean nextPageForCrash = true;

    public static boolean isCrashProgress = false;

    static String crashstartDate;
    static String crashendDate;

    public static boolean isFromTransHistory = false;
    public static int pos;


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
        context = inflater.getContext();
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
        String tab = this.getArguments().getString("tabTCItem");
        transactionViewPagerAdapter = new TransactionViewPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(transactionViewPagerAdapter);
        /*if (tab.equalsIgnoreCase("All Transations")) {
            binding.viewPager.setCurrentItem(0);
            callTransactionHistoryApi(context);

        } else */

        if (tab.equalsIgnoreCase("Crash Course")) {
            binding.viewPager.setCurrentItem(1);

            if (!isFromTransHistory) {
                initVariable();
                callCrashTransactionApi(context);
            } else {
                CrashCourseFragment.setupRecyclerViewCrash();
            }
        }


        binding.tablayout.setupWithViewPager(binding.viewPager);

        BaseScreenActivity.binding.incFragmenttool.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view);
            }
        });


        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pos = tab.getPosition();
              /*  if (pos == 0) {
                    if (transactionJsonList.size() <= 0) {
                        callTransactionHistoryApi(context);
                    } else {
                        AllTransactionsFragment.setupRecyclerViewTransactions(context, transactionJsonList);
                    }
                } else*/
                if (pos == 1) {
                    if (crashJsonList.size() <= 0) {
                        initVariable();
                        callCrashTransactionApi(context);
                    } else {
                        CrashCourseFragment.setupRecyclerViewCrash();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadFragment(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        searchTransactionsFragment = new SearchTransactionsFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchTransactionsFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    initVariable();
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }
    public static void callCrashTransactionApi(Context context) {

        String sDate = "";
        String eDate = "";
        int Page = 1;
        if (crashstartDate != null) {
            sDate = crashstartDate;
            // coursePage = SearchEarningsFragment.CoursePage;
        } else {
            Page = CrashPage;
        }
        if (crashendDate != null) {
            eDate = crashendDate;
        }

        LoadingDialog loadingDialog = new LoadingDialog(context);
        String apiParam = "?create_date_start=" + sDate + "&create_date_end=" + eDate + "&page=" + Page + "&per_page=10";

        Api.newApi(context, P.baseUrl + "crash_course_transaction" + apiParam)
                .setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (isLoading)
                        if (!isCrashProgress) {
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
                                crashJsonList.addAll(jsonList);
                                //courseJson = Json1;
                                CrashCourseFragment.setupRecyclerViewCrash();
                                if (crashJsonList.size() < numRows) {

                                    CrashPage++;
                                    nextPageForCrash = true;
                                    isCrashProgress = true;
                                } else {
                                    nextPageForCrash = false;
                                    isCrashProgress = false;
                                    CrashPage = 1;
                                }
                            }

                        }
                    }

                }).run("crash_course_transaction");
    }

    private void initVariable() {
        crashstartDate = null;
        crashendDate = null;
        CrashPage = 1;
        nextPageForCrash = true;
        crashJsonList.clear();
    }


}