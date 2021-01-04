package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Adapter.SupportHelpViewPagerAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentHelpandsupportBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.P;
import com.google.android.material.tabs.TabLayout;

public class HelpAndSupportFragment extends Fragment {

    private FragmentHelpandsupportBinding binding;
    SupportHelpViewPagerAdapter supportHelpViewPagerAdapter;
    Context context;
    static int pos;

    static int inboxPage = 1;
    static JsonList inboxJsonList = new JsonList();
    static boolean nextPageForinbox = true;

    static int outboxPage = 1;
    static JsonList outboxJsonList = new JsonList();
    static boolean nextPageForOutbox = true;

    public HelpAndSupportFragment() {
    }

    public static HelpAndSupportFragment newInstance() {
        HelpAndSupportFragment fragment = new HelpAndSupportFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_helpandsupport, container, false);
        View rootView = binding.getRoot();
        context = inflater.getContext();
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Support/Help");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        supportHelpViewPagerAdapter = new SupportHelpViewPagerAdapter(getChildFragmentManager());
        binding.viewPagerSupportHelp.setAdapter(supportHelpViewPagerAdapter);
        binding.tablayoutSupport.setupWithViewPager(binding.viewPagerSupportHelp);

        binding.tablayoutSupport.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pos = tab.getPosition();
               /* if (pos == 0) {
                } else*/
                if (pos == 1) {
                    if (inboxJsonList.size() <= 0) {
                        inboxPage = 1;
                        callSupportInboxApi(context);
                    } else {
                        InboxFragment.setupRecyclerviewForInbox();
                    }

                } else {
                    if (outboxJsonList.size() <= 0) {
                        outboxPage = 1;
                        callSupportOutboxApi(context);
                    } else {
                        OutboxFragment.setupRecyclerviewForOutBox();
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

    public static void callSupportInboxApi(Context context) {

        int Page = 1;
        Page = inboxPage;

        LoadingDialog loadingDialog = new LoadingDialog(context);
        String apiParam = "?page=" + Page + "&per_page=10";

        Api.newApi(context, P.baseUrl + "inbox" + apiParam)
                .setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (isLoading)
                        loadingDialog.show("loading...");
                    else
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
                                inboxJsonList.addAll(jsonList);
                                InboxFragment.setupRecyclerviewForInbox();
                                if (inboxJsonList.size() < numRows) {
                                    inboxPage++;
                                    nextPageForinbox = true;
                                } else {
                                    nextPageForinbox = false;
                                    inboxPage = 1;
                                }
                            }

                        }
                    }

                }).run("inbox");
    }

    public static void callSupportOutboxApi(Context context) {

        int Page = 1;
        Page = outboxPage;

        LoadingDialog loadingDialog = new LoadingDialog(context);
        String apiParam = "?page=" + Page + "&per_page=10";

        Api.newApi(context, P.baseUrl + "support" + apiParam)
                .setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (isLoading)
                        loadingDialog.show("loading...");
                    else
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
                                outboxJsonList.addAll(jsonList);
                                OutboxFragment.setupRecyclerviewForOutBox();
                                if (outboxJsonList.size() < numRows) {
                                    outboxPage++;
                                    nextPageForOutbox = true;
                                } else {
                                    nextPageForOutbox = false;
                                    outboxPage = 1;
                                }
                            }

                        }
                    }

                }).run("support");
    }


}