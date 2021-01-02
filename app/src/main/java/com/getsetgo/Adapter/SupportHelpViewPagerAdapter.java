package com.getsetgo.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.getsetgo.Fragment.ComposeFragment;
import com.getsetgo.Fragment.InboxFragment;
import com.getsetgo.Fragment.OutboxFragment;

public class SupportHelpViewPagerAdapter extends FragmentPagerAdapter {

    public SupportHelpViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new ComposeFragment();
        }
        else if (position == 1) {
            fragment = new InboxFragment();
        }
        else if (position == 2) {
            fragment = new OutboxFragment();
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
        if (position == 0) {
            title = "Compose";
        } else if (position == 1) {
            title = "Inbox";
        } else if (position == 2) {
            title = "Outbox";
        }
        return title;
    }
}
