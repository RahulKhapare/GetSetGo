package com.getsetgo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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

import com.getsetgo.Adapter.ActiveCourseAdapter;
import com.getsetgo.Adapter.BestSellingCourseAdapter;
import com.getsetgo.Adapter.OtherCategoriesAdapter;
import com.getsetgo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    public ImageView ivNotify, ivMenu, ivCourseImage;
    public TextView txtTech, txtDesciption, txtProgramme, txtStatus, txtViewAll, txtViewAllBestCourse;
    public RecyclerView recyclerViewCources, recyclerViewOtherCategories, recyclerBestSellingCources;
    BottomNavigationView bottomNavigationView;
    CardView cardViewCurrentLearning;
    ActiveCourseAdapter activeCourseAdapter;
    OtherCategoriesAdapter otherCategoriesAdapter;
    BestSellingCourseAdapter bestSellingCourseAdapter;
    NavDrawerFragment mMenuFragment;
    Toolbar toolbar;
    MenuItem item;
    View itemChooser;
    TextView count;


    private FlowingDrawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        context = HomeScreenActivity.this;
        init();
    }

    private void init() {
        ivNotify = findViewById(R.id.ivNotify);
        ivMenu = findViewById(R.id.ivMenu);
        ivCourseImage = findViewById(R.id.imvCourse);
        txtTech = findViewById(R.id.textTech);
        txtDesciption = findViewById(R.id.txtDes);
        txtProgramme = findViewById(R.id.txtProgramme);
        txtViewAll = findViewById(R.id.txtViewAll);
        txtStatus = findViewById(R.id.txtStatus);

        recyclerViewCources = findViewById(R.id.recyclerViewCources);
        txtViewAllBestCourse = findViewById(R.id.txtViewAllBestCourse);
        cardViewCurrentLearning = findViewById(R.id.cardViewCurrentLearning);
        recyclerBestSellingCources = findViewById(R.id.recyclerBestSellingCources);
        recyclerViewOtherCategories = findViewById(R.id.recyclerViewOtherCategories);
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        mDrawer = (FlowingDrawer) findViewById(R.id.drawerLayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_NONE);
        setupToolbar();
        setupMenu();

        txtViewAllBestCourse.setOnClickListener(this);
        txtViewAll.setOnClickListener(this);
        ivNotify.setOnClickListener(this);
        //ivMenu.setOnClickListener(this);

        setupRecyclerViewForActiveCourse();
        setupRecyclerViewForOthersCategories();
        setupRecyclerViewForBestSellingCourse();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_favourites:
                        startActivity(new Intent(context, WishlistActivity.class));
                        break;
                    case R.id.menu_search:
                        startActivity(new Intent(context, MyCourseActivity.class));
                        break;
                    case R.id.menu_yourCourse:
                        startActivity(new Intent(context, CategoriesActivity.class));
                        break;
                }
                return false;
            }
        });

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
        ivLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_splash_logo));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_ham_menud);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.toggleMenu();
            }
        });
    }

    public void Cancel(View view) {
        mDrawer.closeMenu();
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txtViewAll:
                break;

            case R.id.txtViewAllBestCourse:
                break;

            case R.id.cardViewCurrentLearning:
                break;
        }
    }


    private void setupRecyclerViewForActiveCourse() {
        recyclerViewCources.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activeCourseAdapter = new ActiveCourseAdapter(this);
        recyclerViewCources.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCources.setAdapter(activeCourseAdapter);
        activeCourseAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewForOthersCategories() {
        recyclerViewOtherCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        otherCategoriesAdapter = new OtherCategoriesAdapter(this);
        recyclerViewOtherCategories.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOtherCategories.setAdapter(otherCategoriesAdapter);
        otherCategoriesAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewForBestSellingCourse() {
        recyclerBestSellingCources.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bestSellingCourseAdapter = new BestSellingCourseAdapter(this);
        recyclerBestSellingCources.setItemAnimator(new DefaultItemAnimator());
        recyclerBestSellingCources.setAdapter(bestSellingCourseAdapter);
        bestSellingCourseAdapter.notifyDataSetChanged();
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
                    Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return true;
    }


}