package com.projects.diwanshusoni.happyapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.projects.diwanshusoni.happyapp.fragments.Tasktodo_frag;

/**
 * Created by Diwanshu Soni on 01-10-2017.
 */

public class CAdapterViewpagerTasktodo extends FragmentStatePagerAdapter {
    public CAdapterViewpagerTasktodo(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new Tasktodo_frag();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
