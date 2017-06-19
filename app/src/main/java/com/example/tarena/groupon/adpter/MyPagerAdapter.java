package com.example.tarena.groupon.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tarena.groupon.fragment.AFragment;
import com.example.tarena.groupon.fragment.BFragment;
import com.example.tarena.groupon.fragment.CFragment;
import com.example.tarena.groupon.fragment.DFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarena on 2017/6/15.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
List<Fragment> fragmentList;
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList=new ArrayList<>();
        fragmentList.add(new AFragment());
        fragmentList.add(new BFragment());
        fragmentList.add(new CFragment());
        fragmentList.add(new DFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
