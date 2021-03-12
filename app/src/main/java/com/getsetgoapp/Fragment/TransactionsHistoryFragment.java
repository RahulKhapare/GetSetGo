package com.getsetgoapp.Fragment;

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

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;


import com.getsetgoapp.Adapter.TransactionViewPagerAdapter;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;

import com.getsetgoapp.databinding.FragmentTransactionsBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
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
    static String actionType;
    static String incomeType;


    static int transactionPage = 1;
    static JsonList transactionJsonList = new JsonList();
    static boolean nextPageForTransaction = true;
    static boolean isProgress = false;

    static String startDate;
    static String endDate;

    static String actionTransType;
    static String incomeTransType;

    public static boolean isFromTransHistory = false;
    public static boolean isFromSearch = false;
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
        initVariable();
        String tab = this.getArguments().getString("tabTCItem");
        transactionViewPagerAdapter = new TransactionViewPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(transactionViewPagerAdapter);
        if (tab.equalsIgnoreCase("Course")) {
            binding.viewPager.setCurrentItem(0);

            if (!isFromSearch) {
                if (!isFromTransHistory) {
                    callTransactionHistoryApi(context);
                } else {
                    AllTransactionsFragment.setUpRecuclerviewAllTransactions();
                }
            }

        }

        if (tab.equalsIgnoreCase("Crash Course")) {
            binding.viewPager.setCurrentItem(1);
            if (!isFromSearch) {
                if (!isFromTransHistory) {
                    callCrashTransactionApi(context);
                } else {
                    CrashCourseFragment.setupRecyclerViewCrash();
                }
            }
        }


        binding.tablayout.setupWithViewPager(binding.viewPager);

        BaseScreenActivity.binding.incFragmenttool.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                loadFragment(view);
            }
        });


        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pos = tab.getPosition();
                if (pos == 0) {
                    if (transactionJsonList.size() <= 0) {
                        if(!isFromSearch){
                            callTransactionHistoryApi(context);
                        }
                    } else {
                        AllTransactionsFragment.setUpRecuclerviewAllTransactions();
                    }
                }
                if (pos == 1) {
                    if (crashJsonList.size() <= 0) {
                        if(!isFromSearch){
                            callCrashTransactionApi(context);
                        }
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

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
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
        String action = "";
        String income = "";
        int Page = 1;
        Page = CrashPage;

        if(isFromSearch){
            if (crashstartDate != null) {
                sDate = crashstartDate;
            }
            if (crashendDate != null) {
                eDate = crashendDate;

            }
            if(actionType != null && !actionType.isEmpty()){
                action = actionType;
            }
            if(incomeType != null && !incomeType.isEmpty()){
                income = incomeType;
            }
            Page = SearchTransactionsFragment.CrashPage;
        }

        LoadingDialog loadingDialog = new LoadingDialog(context);
        String apiParam = "?create_date_start=" + sDate + "&create_date_end=" + eDate + "&action_type=" + action + "&income_type=" + income + "&page=" + Page + "&per_page=10";

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
                        JumpToLogin.call(Json1,context);
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

    public static void callTransactionHistoryApi(Context context) {

        String sDate = "";
        String eDate = "";
        String action = "";
        String income = "";
        int Page = 1;
        Page = transactionPage;

        if(isFromSearch){
            if (startDate != null) {
                sDate = startDate;
            }
            if (endDate != null) {
                eDate = endDate;

            }
            if(actionTransType != null && !actionTransType.isEmpty()){
                action = actionTransType;
            }
            if(incomeTransType != null && !incomeTransType.isEmpty()){
                income = incomeTransType;
            }
            Page = SearchTransactionsFragment.transPage;
        }


        LoadingDialog loadingDialog = new LoadingDialog(context);
        String apiParam = "?create_date_start=" + sDate + "&create_date_end=" + eDate + "&action_type=" + action + "&income_type=" + income + "&page=" + Page + "&per_page=10";

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
                        JumpToLogin.call(Json1,context);
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
                                AllTransactionsFragment.setUpRecuclerviewAllTransactions();
                                if (transactionJsonList.size() < numRows) {
                                    if (isFromSearch) {
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

        if (isFromSearch) {
        } else if (isFromTransHistory) {
        } else {
            crashstartDate = null;
            crashendDate = null;
            actionType = "";
            incomeType = "";

            startDate = null;
            endDate = null;
            actionTransType = "";
            incomeTransType = "";

            isCrashProgress = false;
            isProgress = false;
            transactionPage = 1;
            CrashPage = 1;
            nextPageForTransaction = true;
            nextPageForCrash = true;
            crashJsonList.clear();
            transactionJsonList.clear();

        }
    }


}