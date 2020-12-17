package com.getsetgo.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.getsetgo.Fragment.ComposeFragment;
import com.getsetgo.Fragment.CrashCourseFragment;
import com.getsetgo.Fragment.InboxOutboxFragment;

public class SupportHelpViewPagerAdapter extends FragmentPagerAdapter {

    public SupportHelpViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new ComposeFragment();
        }
        else if (position == 1)
        {
            fragment = new InboxOutboxFragment();
        }
        else if (position == 2)
        {
            fragment = new InboxOutboxFragment();
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
            title = "Compose";
        }
        else if (position == 1)
        {
            title = "Inbox";
        }
        else if (position == 2)
        {
            title = "Outbox";
        }
        return title;
    }
}
