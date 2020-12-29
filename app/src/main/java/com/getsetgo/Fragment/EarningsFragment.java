package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgo.Adapter.MyEarningsViewPagerAdapter;
import com.getsetgo.Adapter.NotificationAdapter;
import com.getsetgo.Model.NotificationModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentEarningsBinding;
import com.getsetgo.databinding.FragmentNotificationsBinding;
import com.getsetgo.util.P;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class EarningsFragment extends Fragment {

    FragmentEarningsBinding binding;
    MyEarningsViewPagerAdapter myEarningsViewPagerAdapter;
    SearchEarningsFragment searchEarningsFragment;
    Context context;

    public EarningsFragment() {
    }

    public static EarningsFragment newInstance() {
        EarningsFragment fragment = new EarningsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_earnings, container, false);
        View rootView = binding.getRoot();
        context = inflater.getContext();
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("My Earnings");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);

        init();
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {
        String tab = this.getArguments().getString("tabItem");
        myEarningsViewPagerAdapter = new MyEarningsViewPagerAdapter(getChildFragmentManager(), context);
        binding.viewPagerEarning.setAdapter(myEarningsViewPagerAdapter);
        if (tab.equalsIgnoreCase("Course Earnings")) {
            binding.viewPagerEarning.setCurrentItem(0);
            callCourseEarningApi(context);
        }
        if (tab.equalsIgnoreCase("Crash Course Earnings")) {
            binding.viewPagerEarning.setCurrentItem(1);
            callCrashCourseEarningApi(context);
        }
        if (tab.equalsIgnoreCase("Total Earnings")) {
            binding.viewPagerEarning.setCurrentItem(2);
            BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
            callTotalEarningApi(context);
        }
        binding.tablayoutEarnings.setupWithViewPager(binding.viewPagerEarning);

        BaseScreenActivity.binding.incFragmenttool.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view);
            }
        });

        binding.tablayoutEarnings.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if (pos == 0) {
                    callCourseEarningApi(context);
                } else if (pos == 1) {
                    callCrashCourseEarningApi(context);
                } else {
                    BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
                    callTotalEarningApi(context);
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
        searchEarningsFragment = new SearchEarningsFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchEarningsFragment)
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

    public static void callTotalEarningApi(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        String apiParam = "?create_date_start=" + "&create_date_end=" + "&page=" + "&per_page=";

        Api.newApi(context, P.baseUrl + "total_earning" + apiParam).setMethod(Api.GET)
                .onLoading(isLoading -> {
                    if (isLoading)
                        loadingDialog.show("loading...");
                    else
                        loadingDialog.hide();

                })
                .onError(() ->
                        MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            //Json1 = Json1.getJson(P.data);
                            String msg = Json1.getString(P.msg);
                            Session session = new Session(context);
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        }
                    }

                }).run("total_earning");
    }

    public static void callCourseEarningApi(Context context) {

        String apiParam = "?create_date_start=" + "&create_date_end=" + "&page=" + "&per_page=";

        Api.newApi(context, P.baseUrl + "course_earning" + apiParam).setMethod(Api.GET)
                .onError(() ->
                        MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            //Json1 = Json1.getJson(P.data);
                            String msg = Json1.getString(P.msg);
                            Session session = new Session(context);
                            //setupRecyclerViewMyEarnings();
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                }).run("course_earning");
    }

    public static void callCrashCourseEarningApi(Context context) {

        String apiParam = "?create_date_start=" + "&create_date_end=" + "&page=" + "&per_page=";

        Api.newApi(context, P.baseUrl + "crash_course_earning" + apiParam).setMethod(Api.GET)
                .onError(() ->
                        MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            //Json1 = Json1.getJson(P.data);
                            String msg = Json1.getString(P.msg);
                            Session session = new Session(context);
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                }).run("crash_course_earning");
    }


}