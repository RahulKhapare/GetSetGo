package com.getsetgo.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.getsetgo.Fragment.AllTransactionsFragment;
import com.getsetgo.Fragment.CrashCourseFragment;
import com.getsetgo.Fragment.MyEarningFragment;

public class MyEarningsViewPagerAdapter extends FragmentPagerAdapter {

    public MyEarningsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new MyEarningFragment();
        }
        else if (position == 1)
        {
            fragment = new CrashCourseFragment();
        }
        else if (position == 2)
        {
            fragment = new CrashCourseFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "My Earnings";
        }
        else if (position == 1)
        {
            title = "Crash Course";
        }
        else if (position == 2)
        {
            title = "Total Earnings";
        }
        return title;
    }
}
