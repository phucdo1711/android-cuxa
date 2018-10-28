package com.example.dell.appcuxa.MainPage.MainPageViews.SavedTab.SavedView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.AdapterSavedItem;
import com.example.dell.appcuxa.MainPage.Adapter.SectionsPagerAdapter;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicSaveRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.IUnsaveRoomLogic;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.SavedRoom;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSaved extends Fragment{
    private View mMainView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    SectionsPagerAdapter mAdapter;

    public FragmentSaved(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_saved, container, false);
        mViewPager = (ViewPager) mMainView.findViewById(R.id.main_tabPager);

        mAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout = (TabLayout) mMainView.findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        return mMainView;
    }
}

