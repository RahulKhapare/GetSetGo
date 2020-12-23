package com.getsetgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.getsetgo.Fragment.AccountFragment;
import com.getsetgo.Fragment.DashBoardFragment;
import com.getsetgo.Fragment.FavouritesFragment;
import com.getsetgo.Fragment.HomeFragment;
import com.getsetgo.Fragment.NavDrawerFragment;
import com.getsetgo.Fragment.SearchFragment;
import com.getsetgo.Fragment.YourCourseFragment;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBaseScreenBinding;
import com.getsetgo.databinding.ActivityHomeScreenBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.P;
import com.getsetgo.util.WindowView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;

public class BaseScreenActivity extends AppCompatActivity {


    private BaseScreenActivity activity = this;
    public static ActivityBaseScreenBinding binding;

    private HomeFragment homeFragment;
    DashBoardFragment dashBoardFragment;
    AccountFragment accountFragment;

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
                        Json json = new Json();
                        String string = P.baseUrl + "series_check/" + json.getString(P.series_slug) + "/" + json.getString(P.video_slug);
                        int i = json.getInt(P.time);
                        i *= 1000;

                        App.app.startMyCourseActivity(activity, string, i);
                        break;
                }
                return loadFragment(fragment,isHide);
            }
        });

    }


    private void loadHomeFragment() {
        if (homeFragment == null)
            homeFragment = HomeFragment.newInstance();
        fragmentLoader(homeFragment, false);
    }

    private boolean loadFragment(Fragment fragment ,boolean isHideToolbar) {
        if (isHideToolbar) {
            binding.incBasetool.content.setVisibility(View.GONE);
            binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        }else{
            binding.incBasetool.content.setVisibility(View.VISIBLE);
            binding.incFragmenttool.content.setVisibility(View.GONE);
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
        Intent intent;

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

        }
        ((DrawerLayout) findViewById(R.id.drawerLayout)).closeDrawer(GravityCompat.START);
    }


    private long l;

    private void fragmentLoader(Fragment fragment, boolean isHideToolbar) {

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


    @Override
    public void onBackPressed() {
        ((DrawerLayout)findViewById(R.id.drawerLayout)).closeDrawer(GravityCompat.START);
        if(!homeFragment.isVisible()){
            onBackClick(findViewById(R.id.ivBack));
        }else if (binding.bottomNavigation.getSelectedItemId() != R.id.menu_home) {
            loadHomeFragment();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    public void onBackClick(View view) {
        loadHomeFragment();
    }


}