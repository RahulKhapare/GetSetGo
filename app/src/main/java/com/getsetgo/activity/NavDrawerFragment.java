
package com.getsetgo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.getsetgo.R;
import com.google.android.material.navigation.NavigationView;


public class NavDrawerFragment extends Fragment {

    public static Context context;
    static AppCompatActivity activity;
    public TextView txtName,txtProfile,txtEmail;
    public static MenuItem menuBusiness,menuEarn,
            menuPoints,menuTrans,menuIncentive,
            nav_courseEarn,nav_totalEarn,nav_crashcourseEarn;
    public static CheckBox chkBusiness,chkEarn,
            chkPoints,chkTrans,chkIncentive;;
    static NavDrawerFragment navDrawerFragment;
    NavigationView vNavigation;
    View view = null;


    public static NavDrawerFragment getInstance() {
        return navDrawerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        context = getActivity();
        activity = (AppCompatActivity) getActivity();
        navDrawerFragment = this;
        txtProfile = view.findViewById(R.id.txtProfile);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtName = view.findViewById(R.id.txtName);
        vNavigation = view.findViewById(R.id.vNavigation);

        Menu navigate = vNavigation.getMenu();
        nav_courseEarn = navigate.findItem(R.id.nav_courseEarn);
        nav_crashcourseEarn = navigate.findItem(R.id.nav_crashcourseEarn);
        nav_totalEarn = navigate.findItem(R.id.nav_totalEarn);
        menuBusiness = navigate.findItem(R.id.nav_business);
        menuEarn = navigate.findItem(R.id.nav_MyEarning);
        menuTrans = navigate.findItem(R.id.nav_Transactions);
        menuIncentive = navigate.findItem(R.id.nav_Userincentive);
        menuPoints = navigate.findItem(R.id.nav_MyPoints);

        View businessActionView = menuBusiness.getActionView();
        chkBusiness = businessActionView.findViewById(R.id.chkUpDown);

        View earnActionView = menuEarn.getActionView();
        chkEarn = earnActionView.findViewById(R.id.chkUpDown);

        View transActionView = menuTrans.getActionView();
        chkTrans = transActionView.findViewById(R.id.chkUpDown);

        View pointsActionView = menuPoints.getActionView();
        chkPoints = pointsActionView.findViewById(R.id.chkUpDown);

        View incentiveActionView = menuIncentive.getActionView();
        chkIncentive = incentiveActionView.findViewById(R.id.chkUpDown);

        chkEarn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    navigate.findItem(R.id.nav_courseEarn).setVisible(true);
                    navigate.findItem(R.id.nav_crashcourseEarn).setVisible(true);
                    navigate.findItem(R.id.nav_totalEarn).setVisible(true);
                }else{
                    navigate.findItem(R.id.nav_courseEarn).setVisible(false);
                    navigate.findItem(R.id.nav_crashcourseEarn).setVisible(false);
                    navigate.findItem(R.id.nav_totalEarn).setVisible(false);
                }
            }
        });


        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (menuItem.getItemId()){
                            case R.id.nav_Dashboard:
                                startActivity(new Intent(context,DashBoardActivity.class));
                                break;
                        }

                    }
                },300);

                return false;
            }
        }) ;
        return  view ;
    }



}
