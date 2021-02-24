package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Adapter.IncentivesAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentUserincentiveBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.JumpToLogin;
import com.getsetgo.util.P;

public class IncentivesFragment extends Fragment {

    static IncentivesAdapter incentivesAdapter;
    static FragmentUserincentiveBinding binding;
    SearchIncentivesFragment searchIncentivesFragment;
    Context context;

    static JsonList incentiveList = new JsonList();
    static boolean nextPage = false;
    static int page = 1;
    private LinearLayoutManager mLayoutManager;
    boolean isScrolling = false;
    int currentItem, totalItems, scrollOutItems;

    public static boolean isSearch = false;

    static String startDate;
    static String actionType;


    public IncentivesFragment() {
    }

    public static IncentivesFragment newInstance() {
        IncentivesFragment fragment = new IncentivesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_userincentive, container, false);
        View rootView = binding.getRoot();
        context = inflater.getContext();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Incentives");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    public static void initVariable() {
        startDate = null;
        actionType = "";
        incentiveList.clear();
    }


    private void init(View view) {
        if (!isSearch) {
            initVariable();
            callUserIncentiveApi(context);
        }
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewIncentive.setLayoutManager(mLayoutManager);
        binding.recyclerViewIncentive.setItemAnimator(new DefaultItemAnimator());
        incentivesAdapter = new IncentivesAdapter(getActivity(), incentiveList);
        binding.recyclerViewIncentive.setAdapter(incentivesAdapter);
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                loadFragment(view);
            }
        });

        binding.recyclerViewIncentive.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                if (isScrolling && (currentItem + scrollOutItems == totalItems)) {
                    if (nextPage) {
                        isScrolling = false;
                        callUserIncentiveApi(context);
                    }
                }
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

    public static void setupRecyclerViewForIncentives() {
        incentivesAdapter.notifyDataSetChanged();
    }

    private void loadFragment(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        searchIncentivesFragment = new SearchIncentivesFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchIncentivesFragment)
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

    public static void callUserIncentiveApi(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        String sDate = "";
        String action = "";
        int Page = 1;
        Page = page;
        if (isSearch) {
            if (startDate != null) {
                sDate = startDate;
            }
            if (actionType != null && !actionType.isEmpty()) {
                action = actionType;
            }

            Page = SearchIncentivesFragment.page;
        }

        String apiParam = "?add_date=" + sDate + "&approve_status=" + action + "&page=" + Page + "&per_page=10";

        Api.newApi(context, P.baseUrl + "user_incentive" + apiParam).setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (isLoading)
                        loadingDialog.show("loading...");
                    else
                        loadingDialog.hide();
                    loadingDialog.dismiss();
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
                                incentiveList.addAll(jsonList);
                                setupRecyclerViewForIncentives();
                                if (incentiveList.size() < numRows) {
                                    if (isSearch) {
                                        SearchIncentivesFragment.page++;
                                    } else {
                                        page++;
                                    }
                                    nextPage = true;
                                } else {
                                    nextPage = false;
                                    page = 1;
                                }
                            }
                        }
                    }

                }).run("user_incentive");
    }


}