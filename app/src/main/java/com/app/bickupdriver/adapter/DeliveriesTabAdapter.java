package com.app.bickupdriver.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.bickupdriver.R;
import com.app.bickupdriver.fragments.CompletedRidesFragment;
import com.app.bickupdriver.fragments.MissedRidesFragment;
import com.app.bickupdriver.utility.Utils;

/**
 * Created by manish on 11/5/18.
 */

    public class DeliveriesTabAdapter extends FragmentPagerAdapter {

    private static final String TAG = "HISTORY";
    private Context context;

    public DeliveriesTabAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Utils.printLogs(TAG, "Adding fragment with position : " + position);
            switch (position) {
                case 0:
                    fragment = new CompletedRidesFragment();
                    break;
                case 1:
                    fragment = new MissedRidesFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return context.getResources().getString(R.string.txt_completed);
                case 1:
                    return context.getResources().getString(R.string.txt_missed);
            }
            return null;
        }
    }
