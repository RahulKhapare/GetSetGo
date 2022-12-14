package com.getsetgoapp.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.getsetgoapp.Fragment.AllTransactionsFragment;
import com.getsetgoapp.Fragment.CrashCourseFragment;

public class TransactionViewPagerAdapter extends FragmentPagerAdapter {

    public TransactionViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new AllTransactionsFragment();
        }
        else if (position == 1)
        {
            fragment = new CrashCourseFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Course";
        }
        else if (position == 1)
        {
            title = "Crash Course";
        }
        return title;
    }
}
