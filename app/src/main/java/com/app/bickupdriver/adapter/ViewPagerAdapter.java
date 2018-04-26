package com.app.bickupdriver.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fluper-pc on 13/11/17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public static int pos = 0;

    private List<Fragment> myFragments;
    private ArrayList<String> categories;
    private Context context;


    public ViewPagerAdapter(Context c, FragmentManager fragmentManager, List<Fragment> myFrags, ArrayList<String> cats) {
        super(fragmentManager);
        myFragments = myFrags;
        this.categories = cats;
        this.context = c;

    }

    @Override
    public Fragment getItem(int position) {

        return myFragments.get(position);

    }

    @Override
    public int getCount() {
        return myFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        setPos(position);
        return categories.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    public static int getPos() {
        return pos;
    }

    public void add(Class<Fragment> c, String title, Bundle b) {
        myFragments.add(Fragment.instantiate(context,c.getName(),b));
        categories.add(title);
    }

    public static void setPos(int pos) {
        ViewPagerAdapter.pos = pos;
    }

}
