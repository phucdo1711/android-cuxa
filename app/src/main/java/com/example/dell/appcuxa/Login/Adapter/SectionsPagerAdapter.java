package com.example.dell.appcuxa.Login.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.example.dell.appcuxa.Login.LoginView.FragmentFirst;
import com.example.dell.appcuxa.Login.LoginView.FragmentSecond;
import com.example.dell.appcuxa.Login.LoginView.FragmentThird;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    public static int LOOPS_COUNT = 1000;
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        fragments.add(new FragmentFirst());
        fragments.add(new FragmentSecond());
        fragments.add(new FragmentThird());
    }

    @Override
    public Fragment getItem(int position) {

        /*if (fragments != null && fragments.size() > 0)
        {
            position = position % fragments.size(); // use modulo for infinite cycling
            return fragments.get(position);
        }
        else
        {
            return null;
        }*/
        if(position>=fragments.size()-1){
            return fragments.get(0);
        }else{
            ++position;
            return fragments.get(position);
        }
    }

    @Override
    public int getCount() {
        /*if (fragments != null && fragments.size() > 0)
        {
            return fragments.size()*LOOPS_COUNT; // simulate infinite by big number of products
        }
        else
        {
            return 1;
        }*/
        return fragments.size();
    }
}
