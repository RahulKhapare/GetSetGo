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
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
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
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.P;
import com.getsetgo.util.Utilities;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Map;

public class EarningsFragment extends Fragment {

    FragmentEarningsBinding binding;
    MyEarningsViewPagerAdapter myEarningsViewPagerAdapter;
    SearchEarningsFragment searchEarningsFragment;
    Context context;
    public static int CoursePage = 1;
    public static int CrashCoursePage = 1;
    public static int pos;

    public static JsonList courseJsonList = new JsonList();
    public static Json courseJson = new Json();
    public static JsonList crashcourseJsonList = new JsonList();
    public static Json crashcourseJson = new Json();
    public static Json totalEarnJson = new Json();

    public static boolean nextPageForCourse = true;
    public static boolean nextPageForCrashCourse = true;
    public static boolean nextPageForTotalEarn = true;
    public static boolean isProgress = false;

    static String startDate;
    static String endDate;

    static String crashstartDate;
    static String crashendDate;
    public static boolean isFromBack = false;


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

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        initVariable();
        courseJsonList.clear();
        crashcourseJsonList.clear();
        totalEarnJson.remove(P.referral_income);


        String tab = this.getArguments().getString("tabItem");
        myEarningsViewPagerAdapter = new MyEarningsViewPagerAdapter(getChildFragmentManager(), context);
        binding.viewPagerEarning.setAdapter(myEarningsViewPagerAdapter);
        if (tab.equalsIgnoreCase("Course Earnings")) {
            binding.viewPagerEarning.setCurrentItem(0);
            BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);
            if (!isFromBack) {
                callCourseEarningApi(context);
            }
        }
        if (tab.equalsIgnoreCase("Crash Course Earnings")) {
            binding.viewPagerEarning.setCurrentItem(1);
            BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);

            if (!isFromBack) {
                callCrashCourseEarningApi(context);
            }
        }
        if (tab.equalsIgnoreCase("Total Earnings")) {
            binding.viewPagerEarning.setCurrentItem(2);
            BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
            if (!isFromBack) {
                callTotalEarningApi(context);
            }
        }

        binding.tablayoutEarnings.setupWithViewPager(binding.viewPagerEarning);

        BaseScreenActivity.binding.incFragmenttool.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                loadFragment(view);
            }
        });


        binding.tablayoutEarnings.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pos = tab.getPosition();
                if (pos == 0) {
                    BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);
                    if (courseJsonList.size() <= 0) {
                        if (!isFromBack) {
                            CoursePage = 1;
                            callCourseEarningApi(context);
                        }
                    } else {
                        MyEarningFragment.setUpRefIncome(courseJson);
                        MyEarningFragment.setupRecyclerViewMyEarnings();
                    }
                } else if (pos == 1) {
                    BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);
                    if (crashcourseJsonList.size() <= 0) {
                        if (!isFromBack) {
                            CrashCoursePage = 1;
                            callCrashCourseEarningApi(context);
                        }
                    } else {
                        MyEarnCrashCourseFragment.setUpCrashIncome(crashcourseJson);
                        MyEarnCrashCourseFragment.setupRecyclerViewCrashCourse();
                    }
                } else {
                    BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
                    if (totalEarnJson.getString(P.referral_income).isEmpty()) {
                            callTotalEarningApi(context);
                    } else {
                        TotalEarningFragment.setUpTotalIncome(totalEarnJson);
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
        String apiParam = "?create_date_start=" + "&create_date_end=" + "&page=" + 1 + "&per_page=10";

        Api.newApi(context, P.baseUrl + "total_earning" + apiParam).setMethod(Api.GET)
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
                            totalEarnJson = Json1;
                            TotalEarningFragment.setUpTotalIncome(Json1);
                        }
                    }

                }).run("total_earning");
    }

    public static void callCourseEarningApi(Context context) {
        String sDate = "";
        String eDate = "";
        int coursePage = 1;
        if (startDate != null) {
            sDate = startDate;
            coursePage = SearchEarningsFragment.CoursePage;
        } else {
            coursePage = CoursePage;
        }
        if (endDate != null) {
            eDate = endDate;
        }

        LoadingDialog loadingDialog = new LoadingDialog(context);
        String apiParam = "?create_date_start=" + sDate + "&create_date_end=" + eDate + "&page=" + coursePage + "&per_page=10";

        Api.newApi(context, P.baseUrl + "course_earning" + apiParam)
                .setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (isLoading)
                        if(!isProgress){
                            loadingDialog.show("loading...");
                        }
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
                                courseJsonList.addAll(jsonList);
                                courseJson = Json1;
                                MyEarningFragment.setupRecyclerViewMyEarnings();
                                MyEarningFragment.setUpRefIncome(Json1);
                                if (courseJsonList.size() < numRows) {
                                    if (startDate != null) {
                                        SearchEarningsFragment.CoursePage++;
                                    } else {
                                        CoursePage++;
                                    }
                                    nextPageForCourse = true;
                                    isProgress = true;
                                } else {
                                    nextPageForCourse = false;
                                    isProgress = false;
                                    CoursePage = 1;
                                    isFromBack = false;
                                }
                            }

                        }
                    }

                }).run("course_earning");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initVariable() {

        if (!isFromBack) {
            startDate = null;
            endDate = null;

            crashstartDate = null;
            crashendDate = null;

            isProgress = false;

            CoursePage = 1;
            CrashCoursePage = 1;

            nextPageForCourse = true;
            nextPageForCrashCourse = true;
            nextPageForTotalEarn = true;

            crashcourseJsonList.clear();
            courseJsonList.clear();
            totalEarnJson.remove(P.referral_income);
        } else {
            courseJsonList.clear();
            isProgress = false;
            crashcourseJsonList.clear();
        }

    }

    public static void callCrashCourseEarningApi(Context context) {
        String sDate = "";
        String eDate = "";
        int crashCoursePage = 1;
        if (crashstartDate != null) {
            sDate = crashstartDate;
            crashCoursePage = SearchEarningsFragment.CrashCoursePage;
        } else {
            crashCoursePage = CrashCoursePage;
        }
        if (crashendDate != null) {
            eDate = crashendDate;
        }
        LoadingDialog loadingDialog = new LoadingDialog(context);
        String apiParam = "?create_date_start=" + sDate + "&create_date_end=" + eDate + "&page=" + crashCoursePage + "&per_page=10";

        Api.newApi(context, P.baseUrl + "crash_course_earning" + apiParam).setMethod(Api.GET)
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
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);
                            int numRows = Json1.getInt(P.num_rows);
                            JsonList jsonList = Json1.getJsonList(P.list);
                            if (jsonList != null && !jsonList.isEmpty()) {
                                crashcourseJsonList.addAll(jsonList);
                                crashcourseJson = Json1;
                                MyEarnCrashCourseFragment.setupRecyclerViewCrashCourse();
                                MyEarnCrashCourseFragment.setUpCrashIncome(Json1);
                                if (crashcourseJsonList.size() < numRows) {
                                    if (crashstartDate != null) {
                                        SearchEarningsFragment.CrashCoursePage++;
                                    } else {
                                        CrashCoursePage++;
                                    }
                                    nextPageForCrashCourse = true;
                                } else {
                                    nextPageForCrashCourse = false;
                                    CrashCoursePage = 1;
                                    isFromBack = false;
                                }
                            }
                        }
                    }

                }).run("crash_course_earning");
    }


}