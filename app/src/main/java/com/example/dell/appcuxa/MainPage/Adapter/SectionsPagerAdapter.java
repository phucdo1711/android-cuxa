package com.example.dell.appcuxa.MainPage.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dell.appcuxa.MainPage.MainPageViews.SavedTab.SavedView.FragmentSavePeople;
import com.example.dell.appcuxa.MainPage.MainPageViews.SavedTab.SavedView.FragmentSaveRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.SavedTab.SavedView.FragmentSaved;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by truognnv  on 10/25/2018.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        fragments.add(new FragmentSaveRoom());
        fragments.add(new FragmentSavePeople());
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentSaveRoom();
            case 1:
                return new FragmentSavePeople();

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Phòng đã lưu";
            case 1:
                return "Bạn đã lưu";
            default:
                return null;
        }
    }
}
