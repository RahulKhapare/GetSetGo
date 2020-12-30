package com.getsetgo.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgo.Fragment.AccountFragment;
import com.getsetgo.Fragment.AddNewUserFragment;
import com.getsetgo.Fragment.BankDetailsFragment;
import com.getsetgo.Fragment.CategoriesFragment;
import com.getsetgo.Fragment.CourseDetailFragment;
import com.getsetgo.Fragment.DashBoardFragment;
import com.getsetgo.Fragment.EarningsFragment;
import com.getsetgo.Fragment.FavouritesFragment;
import com.getsetgo.Fragment.HelpAndSupportFragment;
import com.getsetgo.Fragment.HomeFragment;
import com.getsetgo.Fragment.IncentivesFragment;

import com.getsetgo.Fragment.NotificationsFragment;
import com.getsetgo.Fragment.SearchFragment;
import com.getsetgo.Fragment.SearchUserIdFragment;
import com.getsetgo.Fragment.TermsAndConditionFragment;

import com.getsetgo.Fragment.TotalUsersFragment;
import com.getsetgo.Fragment.TransactionsHistoryFragment;
import com.getsetgo.Fragment.YourCourseFragment;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBaseScreenBinding;


import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.P;
import com.getsetgo.util.WindowView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;


public class BaseScreenActivity extends AppCompatActivity {


    private final BaseScreenActivity activity = this;
    public static ActivityBaseScreenBinding binding;

    private HomeFragment homeFragment;
    DashBoardFragment dashBoardFragment;
    AccountFragment accountFragment;
    NotificationsFragment notificationsFragment;
    TransactionsHistoryFragment transactionsHistoryFragment;
    IncentivesFragment incentivesFragment;
    HelpAndSupportFragment helpAndSupportFragment;
    TermsAndConditionFragment termsAndConditionFragment;
    CategoriesFragment categoriesFragment;
    AddNewUserFragment addNewUserFragment;
    SearchUserIdFragment searchUserIdFragment;
    CheckBox cbMyEarning, cbUsers;
    OnBackPressedCallback onBackPressedCallback;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base_screen);
        init();
    }

    private void init() {

        cbUsers = findViewById(R.id.cbUsers);
        cbMyEarning = findViewById(R.id.cbMyEarning);

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else/* if (!homeFragment.isVisible()) {
                    onBackClick(findViewById(R.id.ivBack));
                } else*/ if (binding.bottomNavigation.getSelectedItemId() != R.id.menu_home) {
                    loadHomeFragment();
                } else {
                    //super.onBackPressed();
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        if (SplashActivity.deviceHeight == 0)
            SplashActivity.deviceHeight = H.getDeviceHeight(this);
        if (SplashActivity.deviceWidth == 0)
            SplashActivity.deviceWidth = H.getDeviceWidth(this);

        loadHomeFragment();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                Bundle bundle = null;
                boolean isHide = false;
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        fragment = new HomeFragment();
                        isHide = false;
                        break;
                    case R.id.menu_favourites:
                        fragment = new FavouritesFragment();
                        isHide = true;
                        break;
                    case R.id.menu_search:
                        fragment = new SearchFragment();
                        isHide = true;
                        break;
                    case R.id.menu_yourCourse:
                        fragment = new YourCourseFragment();
                        isHide = true;
                        break;

                    case R.id.menu_Account:
                        bundle = new Bundle();
                        bundle.putBoolean("isFromBottom", true);
                        fragment = new AccountFragment();
                        fragment.setArguments(bundle);
                        isHide = true;
                        break;
                }
                return loadFragment(fragment, isHide);
            }
        });


        checkBoxMyEarning();
        checkBoxUsers();
        try {
            setDrawerData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDrawerData() throws Exception {
        Session session = new Session(this);
        String name = session.getString(P.name);
        String lastName = session.getString(P.lastname);

        if (name == null || name.isEmpty())
            return;

        TextView txtName = findViewById(R.id.txtName);
        TextView txtEmail = findViewById(R.id.txtEmail);
        txtName.setText(name + " " + lastName);
        String email = session.getString(P.email);
        txtEmail.setText(email);
    }

    private void checkBoxMyEarning() {
        LinearLayout llCbMyEarningExpand = findViewById(R.id.llCbMyEarningExpand);
        cbMyEarning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    llCbMyEarningExpand.setVisibility(View.VISIBLE);
                    isCollapse(cbUsers);
                } else {
                    llCbMyEarningExpand.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkBoxUsers() {
        LinearLayout llCbUsers = findViewById(R.id.llCbUsersExpand);
        cbUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    llCbUsers.setVisibility(View.VISIBLE);
                    isCollapse(cbMyEarning);
                } else {
                    llCbUsers.setVisibility(View.GONE);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onFavouriteClick(View view) {
        int i = view.getId();
        switch (i) {
            case R.id.llFavourite:
                FavouritesFragment fragment = new FavouritesFragment();
                binding.bottomNavigation.setSelectedItemId(R.id.menu_favourites);
                loadFragment(fragment, true);
                break;
        }

    }


    private void loadHomeFragment() {
        if (homeFragment == null)
            homeFragment = HomeFragment.newInstance();
        fragmentLoader(homeFragment, false);
    }

    private boolean loadFragment(Fragment fragment, boolean isHideToolbar) {
        if (isHideToolbar) {
            binding.incBasetool.content.setVisibility(View.GONE);
            binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        } else {
            binding.incBasetool.content.setVisibility(View.VISIBLE);
            binding.incFragmenttool.content.setVisibility(View.GONE);
            binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    public void onDrawerItemClick(View view) {
        int i = view.getId();
        Click.preventTwoClick(view);
        Bundle bundle = new Bundle();
        switch (i) {
            case R.id.txtDashboard:
                if (dashBoardFragment == null)
                    dashBoardFragment = DashBoardFragment.newInstance();
                fragmentLoader(dashBoardFragment, true);
                break;
            case R.id.txtAccount:
                if (accountFragment == null)
                    bundle.putBoolean("isFromBottom", false);
                accountFragment = AccountFragment.newInstance();
                accountFragment.setArguments(bundle);
                fragmentLoader(accountFragment, true);
                break;
            case R.id.txtNotifications:
                if (notificationsFragment == null)
                    notificationsFragment = NotificationsFragment.newInstance();
                fragmentLoader(notificationsFragment, true);
                break;

            case R.id.llNotification:
                if (notificationsFragment == null)
                    notificationsFragment = NotificationsFragment.newInstance();
                fragmentLoader(notificationsFragment, true);
                break;

            case R.id.txtEarning:
                EarningsFragment earningsFragment;
                bundle.putString("tabItem", "Course Earnings");
                earningsFragment = EarningsFragment.newInstance();
                earningsFragment.setArguments(bundle);
                EarningsFragment.isFromBack = false;
                fragmentLoader(earningsFragment, true);
                break;

            case R.id.txtCourseEarning:
                EarningsFragment coursEarning;
                bundle.putString("tabItem", "Course Earnings");
                coursEarning = EarningsFragment.newInstance();
                EarningsFragment.isFromBack = false;
                coursEarning.setArguments(bundle);
                fragmentLoader(coursEarning, true);
                break;

            case R.id.txtCrashCourseEarning:
                EarningsFragment crashEarning;
                bundle.putString("tabItem", "Crash Course Earnings");
                crashEarning = EarningsFragment.newInstance();
                EarningsFragment.isFromBack = false;
                crashEarning.setArguments(bundle);
                fragmentLoader(crashEarning, true);
                break;

            case R.id.txtTotalEaring:
                EarningsFragment totalEarning;
                bundle.putString("tabItem", "Total Earnings");
                totalEarning = EarningsFragment.newInstance();
                EarningsFragment.isFromBack = false;
                totalEarning.setArguments(bundle);
                fragmentLoader(totalEarning, true);
                break;

            case R.id.txtTransactions:
                if (transactionsHistoryFragment == null)
                    transactionsHistoryFragment = TransactionsHistoryFragment.newInstance();
                fragmentLoader(transactionsHistoryFragment, true);
                break;

            case R.id.txtincentive:
                if (incentivesFragment == null)
                    incentivesFragment = IncentivesFragment.newInstance();
                fragmentLoader(incentivesFragment, true);
                break;

            case R.id.txtHelpSupport:
                if (helpAndSupportFragment == null)
                    helpAndSupportFragment = HelpAndSupportFragment.newInstance();
                fragmentLoader(helpAndSupportFragment, true);
                break;

            case R.id.txttermCondition:
                if (termsAndConditionFragment == null)
                    termsAndConditionFragment = TermsAndConditionFragment.newInstance();
                fragmentLoader(termsAndConditionFragment, true);
                break;

            case R.id.txtViewAllCategories:
                if (categoriesFragment == null)
                    categoriesFragment = CategoriesFragment.newInstance();
                fragmentLoader(categoriesFragment, true);
                break;

            case R.id.txtAddUser:
                if (addNewUserFragment == null)
                    addNewUserFragment = AddNewUserFragment.newInstance();
                fragmentLoader(addNewUserFragment, true);
                break;

            case R.id.txtOrganizationChart:
                if (searchUserIdFragment == null)
                    searchUserIdFragment = SearchUserIdFragment.newInstance();
                fragmentLoader(searchUserIdFragment, true);
                break;

            case R.id.txtLogout:
                onLogout();
                break;

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }


    private long l;

    public void fragmentLoader(Fragment fragment, boolean isHideToolbar) {

        if (isHideToolbar) {
            binding.bottomNavigation.setVisibility(View.GONE);
            binding.incBasetool.content.setVisibility(View.GONE);
            binding.incFragmenttool.content.setVisibility(View.VISIBLE);
            BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
            BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        }

        if (fragment instanceof HomeFragment) {
            callBack();
        }

        if (System.currentTimeMillis() - l > 321)
            l = System.currentTimeMillis();
        else
            return;
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit).addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
    }


    public void Cancel(View view) {
        ((DrawerLayout) findViewById(R.id.drawerLayout)).closeDrawer(GravityCompat.START);
    }

    public void onHamClick(View view) {
        ((DrawerLayout) findViewById(R.id.drawerLayout)).openDrawer(GravityCompat.START);
    }

    public void OnNotifications(View view) {
        BaseScreenActivity.binding.incBasetool.count.setText("99");
        if (notificationsFragment == null)
            notificationsFragment = NotificationsFragment.newInstance();
        fragmentLoader(notificationsFragment, true);
    }


   /* @Override
    public void onBackPressed() {


        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!homeFragment.isVisible()) {
            onBackClick(findViewById(R.id.ivBack));
        } else if (binding.bottomNavigation.getSelectedItemId() != R.id.menu_home) {
            loadHomeFragment();
        } else {
            //super.onBackPressed();
            finish();
        }
    }*/

    public void onBackClick(View view) {

        loadHomeFragment();

    }


    public static void callBack() {
        binding.incBasetool.content.setVisibility(View.VISIBLE);
        binding.incBasetool.ivHamMenu.setTag("0");
        binding.bottomNavigation.setVisibility(View.VISIBLE);
        binding.bottomNavigation.setSelectedItemId(R.id.menu_home);
        binding.incFragmenttool.content.setVisibility(View.GONE);
        binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
    }

    private void isCollapse(CheckBox checkBox) {
        checkBox.setChecked(false);
    }

    private void callLogOutApi() {
        loadingDialog = new LoadingDialog(activity);
        Json json = new Json();

        Api.newApi(activity, P.baseUrl + "logout").setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (!isDestroyed()) {
                        if (isLoading)
                            loadingDialog.show("loading...");
                        else
                            loadingDialog.hide();
                    }
                })
                .onError(() ->
                        MessageBox.showOkMessage(this, "Message", "Failed to login. Please try again", () -> {
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(activity, Json1.getString(P.err));
                        } else {
                            String message = Json1.getString(P.msg);
                            Json1 = Json1.getJson(P.userdata);

                            App.BackToLogin(activity);
                            finish();
                        }
                    }

                }).run("logout");
    }

    public void onLogout() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage("Do you really want to log out?");
        adb.setTitle("Alert");
        adb.setPositiveButton("Yes", (dialog, which) ->
        {
            Session session = new Session(this);
            boolean bool = session.getBool("isViewed");// for intro of video player

            session.clear();

           /* if (bool)
                session.addBool("isViewed",true);*/

            callLogOutApi();
        });
        adb.setNegativeButton("No", null);
        adb.show();
    }


}