package com.example.dell.appcuxa.MainPage.MainPageViews.SavedTab.SavedView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.dell.appcuxa.MainPage.Adapter.AdapterLiveTogether;
import com.example.dell.appcuxa.MainPage.Adapter.AdapterSavedItem;
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

public class FragmentSavePeople extends Fragment {
    private View mMainView;
    RobBoldText tvNumSaved;
    RecyclerView lstSaved;
    CuXaAPI cuXaAPI;
    private SwipeRefreshLayout swipeContainer;
    public FragmentSavePeople(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_saved_people, container, false);
        //imgView = mMainView.findViewById(R.id.imgView);
        initView();
        cuXaAPI = NetworkController.upload();
        if(getUserVisibleHint()){ // fragment is visible
            loadData();
        }
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        return mMainView;
    }

    private void initView() {
        swipeContainer = (SwipeRefreshLayout) mMainView.findViewById(R.id.swipeContainer);
        tvNumSaved = mMainView.findViewById(R.id.tvNumSaved);
        lstSaved = mMainView.findViewById(R.id.lstSaved);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            loadData();
        }
    }

    private void loadData() {
        Call<ObjectListByOption> getListOGhep = cuXaAPI.getPeople("Bearer "+AppUtils.getToken(getActivity()),"graft");
        getListOGhep.enqueue(new Callback<ObjectListByOption>() {
            @Override
            public void onResponse(Call<ObjectListByOption> call, Response<ObjectListByOption> response) {
                if(response.isSuccessful()){
                    ObjectListByOption people = response.body();
                    tvNumSaved.setText(people.getCount()+" bạn đã lưu");
                    RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 2);
                    lstSaved.setLayoutManager(manager);
                    AdapterLiveTogether adapterLiveTogether = new AdapterLiveTogether(getContext(),Arrays.asList(people.getLstRoom()));
                    lstSaved.setAdapter(adapterLiveTogether);
                    adapterLiveTogether.notifyDataSetChanged();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ObjectListByOption> call, Throwable t) {
                swipeContainer.setRefreshing(false);
            }
        });
    }
}

