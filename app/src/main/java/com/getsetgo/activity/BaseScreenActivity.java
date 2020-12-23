package com.getsetgo.activity;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import com.adoisstudio.helper.H;

import com.getsetgo.Fragment.AccountFragment;
import com.getsetgo.Fragment.CourseDetailFragment;
import com.getsetgo.Fragment.DashBoardFragment;
import com.getsetgo.Fragment.EarningsFragment;
import com.getsetgo.Fragment.FavouritesFragment;
import com.getsetgo.Fragment.HelpAndSupportFragment;
import com.getsetgo.Fragment.HomeFragment;
import com.getsetgo.Fragment.IncentivesFragment;

import com.getsetgo.Fragment.NotificationsFragment;
import com.getsetgo.Fragment.SearchFragment;
import com.getsetgo.Fragment.TermsAndConditionFragment;

import com.getsetgo.Fragment.TransactionsHistoryFragment;
import com.getsetgo.Fragment.YourCourseFragment;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBaseScreenBinding;


import com.getsetgo.util.WindowView;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class BaseScreenActivity extends AppCompatActivity {


    private final BaseScreenActivity activity = this;
    public static ActivityBaseScreenBinding binding;

    private HomeFragment homeFragment;
    DashBoardFragment dashBoardFragment;
    AccountFragment accountFragment;
    NotificationsFragment notificationsFragment;
    EarningsFragment earningsFragment;
    TransactionsHistoryFragment transactionsHistoryFragment;
    IncentivesFragment incentivesFragment;
    HelpAndSupportFragment helpAndSupportFragment;
    TermsAndConditionFragment termsAndConditionFragment;
    CourseDetailFragment courseDetailFragment;
    CheckBox cbMyEarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base_screen);
        init();
    }

    private void init() {
        if (SplashActivity.deviceHeight == 0)
            SplashActivity.deviceHeight = H.getDeviceHeight(this);
        if (SplashActivity.deviceWidth == 0)
            SplashActivity.deviceWidth = H.getDeviceWidth(this);

        loadHomeFragment();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
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
                        fragment = new AccountFragment();
                        isHide = true;
                        break;
                }
                return loadFragment(fragment, isHide);
            }
        });


        checkBoxMyEarning();

    }

    private void checkBoxMyEarning() {
        cbMyEarning = findViewById(R.id.cbMyEarning);
        LinearLayout llCbMyEarningExpand = findViewById(R.id.llCbMyEarningExpand);
        cbMyEarning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    llCbMyEarningExpand.setVisibility(View.VISIBLE);
                } else {
                    llCbMyEarningExpand.setVisibility(View.GONE);
                }
            }
        });
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
                    .commit();
            return true;
        }
        return false;
    }

    public void onDrawerItemClick(View view) {
        int i = view.getId();

        switch (i) {
            case R.id.txtDashboard:
                if (dashBoardFragment == null)
                    dashBoardFragment = DashBoardFragment.newInstance();
                fragmentLoader(dashBoardFragment, true);
                break;
            case R.id.txtAccount:
                if (accountFragment == null)
                    accountFragment = AccountFragment.newInstance();
                fragmentLoader(accountFragment, true);
                break;
            case R.id.txtNotifications:
                if (notificationsFragment == null)
                    notificationsFragment = NotificationsFragment.newInstance();
                fragmentLoader(notificationsFragment, true);
                break;

            case R.id.txtEarning:
                if (earningsFragment == null)
                    earningsFragment = EarningsFragment.newInstance();
                fragmentLoader(earningsFragment, true);
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

                case R.id.txtBusProf:
                if (courseDetailFragment == null)
                    courseDetailFragment = CourseDetailFragment.newInstance();
                fragmentLoader(courseDetailFragment, true);
                break;

        }
        ((DrawerLayout) findViewById(R.id.drawerLayout)).closeDrawer(GravityCompat.START);
    }


    private long l;

    public void fragmentLoader(Fragment fragment, boolean isHideToolbar) {

        if (isHideToolbar) {
            binding.bottomNavigation.setVisibility(View.GONE);
            binding.incBasetool.content.setVisibility(View.GONE);
            binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        }

        if (fragment instanceof HomeFragment) {
            binding.incBasetool.content.setVisibility(View.VISIBLE);
            binding.incBasetool.ivHamMenu.setTag("0");
            binding.bottomNavigation.setVisibility(View.VISIBLE);
            binding.bottomNavigation.setSelectedItemId(R.id.menu_home);
            binding.incFragmenttool.content.setVisibility(View.GONE);
            binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        }

        if (System.currentTimeMillis() - l > 321)
            l = System.currentTimeMillis();
        else
            return;
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit).replace(R.id.fragment_container, fragment).commit();
    }


    public void Cancel(View view) {
        ((DrawerLayout) findViewById(R.id.drawerLayout)).closeDrawer(GravityCompat.START);
    }

    public void onHamClick(View view) {
        ((DrawerLayout) findViewById(R.id.drawerLayout)).openDrawer(GravityCompat.START);
    }

    public void OnNotifications(View view) {
        if (notificationsFragment == null)
            notificationsFragment = NotificationsFragment.newInstance();
        fragmentLoader(notificationsFragment, true);
    }


    @Override
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
    }

    public void onBackClick(View view) {
        loadHomeFragment();
    }


}