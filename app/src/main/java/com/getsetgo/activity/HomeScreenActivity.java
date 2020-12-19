package com.getsetgo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.getsetgo.Adapter.ActiveCourseAdapter;
import com.getsetgo.Adapter.BestSellingCourseAdapter;
import com.getsetgo.Adapter.OtherCategoriesAdapter;
import com.getsetgo.Fragment.FavouritesFragment;
import com.getsetgo.Fragment.HomeFragment;
import com.getsetgo.Fragment.NavDrawerFragment;
import com.getsetgo.Fragment.SearchFragment;
import com.getsetgo.Fragment.YourCourseFragment;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBankDetailsBinding;
import com.getsetgo.databinding.ActivityHomeScreenBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.P;
import com.getsetgo.util.WindowView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

public class HomeScreenActivity extends AppCompatActivity {


    private HomeScreenActivity activity = this;
    public static ActivityHomeScreenBinding binding;

    NavDrawerFragment mMenuFragment;
    Toolbar toolbar;
    MenuItem item;
    View itemChooser;
    TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        init();
    }

    private void init() {
        if (SplashActivity.deviceHeight == 0)
            SplashActivity.deviceHeight = H.getDeviceHeight(this);
        if (SplashActivity.deviceWidth == 0)
            SplashActivity.deviceWidth = H.getDeviceWidth(this);
        binding.drawerLayout.setTouchMode(ElasticDrawer.TOUCH_MODE_NONE);
        setupToolbar();
        setupMenu();

        loadFragment(new HomeFragment());

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.menu_favourites:
                        fragment = new FavouritesFragment();
                        break;
                    case R.id.menu_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.menu_yourCourse:
                        fragment = new YourCourseFragment();
                        break;

                    case R.id.menu_Account:
                        Json json = new Json();
                        String string = P.baseUrl + "series_check/" + json.getString(P.series_slug) + "/" + json.getString(P.video_slug);
                        int i = json.getInt(P.time);
                        i *= 1000;

                        App.app.startMyCourseActivity(activity, string, i);
                        break;
                }
                return loadFragment(fragment);
            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        mMenuFragment = (NavDrawerFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new NavDrawerFragment();
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }

//        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
//            @Override
//            public void onDrawerStateChange(int oldState, int newState) {
//                if (newState == ElasticDrawer.STATE_CLOSED) {
//                    Log.i("MainActivity", "Drawer STATE_CLOSED");
//                }
//            }
//
//            @Override
//            public void onDrawerSlide(float openRatio, int offsetPixels) {
//                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
//            }
//        });
    }

    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        ImageView ivLogo = toolbar.findViewById(R.id.ivLogo);
        ivLogo.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_splash_logo));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_ham_menud);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.toggleMenu();
            }
        });
    }

    public void Cancel(View view) {
        binding.drawerLayout.closeMenu();
    }


    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isMenuVisible()) {
            binding.drawerLayout.closeMenu();
        } else if (binding.bottomNavigation.getSelectedItemId() != R.id.menu_home) {
            binding.bottomNavigation.setSelectedItemId(R.id.menu_home);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu_main, menu);
        item = menu.findItem(R.id.menu_notify);
        itemChooser = item.getActionView();
        count = itemChooser.findViewById(R.id.count);
        count.setText("5");
        if (itemChooser != null) {
            itemChooser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, NotificationsActivity.class));
                }
            });
        }
        return true;
    }


}