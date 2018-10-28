package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.AdapterLiveTogether;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.IBackToListTopScreen;
import com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView.FragmentQrCode;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFindPeople extends DialogFragment implements View.OnClickListener, IBackToListTopScreen {
    public View mMainView;
    private ImageView imgBack;
    RecyclerView lstPeople;
    CuXaAPI cuXaAPI;
    IBackToListTopScreen iBackToListTopScreen;
    public FragmentFindPeople(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_find_people, container, false);
        init();
        iBackToListTopScreen = this;
        getSomeFriend();
        return mMainView;
    }

    private void init() {
        imgBack = mMainView.findViewById(R.id.imgBack);
        lstPeople = mMainView.findViewById(R.id.lstPeople);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBack:
                FragmentFindPeople.this.dismiss();
                break;
        }
    }

    public void getSomeFriend() {
        if(AppUtils.haveNetworkConnection(getContext())){

            cuXaAPI = NetworkController.upload();
            Call<ObjectListByOption> getListOGhep = cuXaAPI.getPeople("Bearer "+ AppUtils.getToken(getActivity()),"graft");
            getListOGhep.enqueue(new Callback<ObjectListByOption>() {
                @Override
                public void onResponse(Call<ObjectListByOption> call, Response<ObjectListByOption> response) {
                    if(response.isSuccessful()){
                        ObjectListByOption people = response.body();
                        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 2);
                        lstPeople.setLayoutManager(manager);
                        AdapterLiveTogether adapterLiveTogether = new AdapterLiveTogether(getContext(), Arrays.asList(people.getLstRoom()),iBackToListTopScreen);
                        lstPeople.setAdapter(adapterLiveTogether);
                        adapterLiveTogether.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ObjectListByOption> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(getActivity(), "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void backToListTopScreen(RoomSearchItem roomInfo) {
        PeopleDetailFragment fragment = new PeopleDetailFragment();
        //fragmentSearQuickAdva.setCancelable(false);
        fragment.setObject(roomInfo);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        fragment.show(getFragmentManager(), "fragment_detail_people");
    }
}
