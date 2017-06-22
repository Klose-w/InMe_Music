package com.example.wade.inme.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import java.util.List;

/**
 * Created by wade on 2017/5/28.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> listfragment;
    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        listfragment=fragmentList;
    }



    @Override
    public Fragment getItem(int position) {
        return listfragment.get(position);
    }

    @Override
    public int getCount() {
        return listfragment.size();
    }
}
