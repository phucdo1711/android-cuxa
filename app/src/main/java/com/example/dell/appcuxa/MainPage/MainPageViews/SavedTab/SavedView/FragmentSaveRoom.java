package com.example.dell.appcuxa.MainPage.MainPageViews.SavedTab.SavedView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicSaveRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.IUnsaveRoomLogic;
import com.example.dell.appcuxa.MainPage.MainPageViews.RoomDetailFragment;
import com.example.dell.appcuxa.ObjectModels.LandLord;
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

public class FragmentSaveRoom extends Fragment implements IUnsaveRoomLogic {
    private View mMainView;
    RobBoldText tvNumSaved;
    RecyclerView lstSaved;
    int numSaved = 0;
    private SwipeRefreshLayout swipeContainer;
    CuXaAPI cuXaAPI;
    List<SavedRoom> roomSearchItems;
    IUnsaveRoomLogic iUnsaveRoom;
    AdapterSavedItem savedItem;
    public FragmentSaveRoom(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_save_room, container, false);
        //imgView = mMainView.findViewById(R.id.imgView);
        initView();
        iUnsaveRoom = this;
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
    public void unSaveRoom(final SavedRoom room) {
        if(AppUtils.haveNetworkConnection(getActivity())){
            cuXaAPI = NetworkController.upload();
            Call<ResponseBody> call = cuXaAPI.saveRoom("Bearer "+ AppUtils.getToken(getActivity()),room.getId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        numSaved = numSaved - 1;
                        tvNumSaved.setText(numSaved+" phòng đã lưu");
                        roomSearchItems.remove(room);
                        savedItem.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(), "Có lỗi sảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getActivity(), "Đéo có mạng", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void backToSavedScreen(SavedRoom savedRoom) {
        RoomDetailFragment roomDetailFragment = new RoomDetailFragment();
        RoomSearchItem roomSearchItem = new RoomSearchItem();
        roomSearchItem.setAddress(savedRoom.getAddress());
        roomSearchItem.setAmountOfTenant(savedRoom.getAmountOfTenant());
        roomSearchItem.setDescription(savedRoom.getDescription());
        roomSearchItem.setId(savedRoom.getId());
        roomSearchItem.setImages(savedRoom.getImageObject());
        LandLord landLord = new LandLord();
        landLord.setName(savedRoom.getLandLord());
        roomSearchItem.setLandLord(landLord);
        roomSearchItem.setName(savedRoom.getName());
        roomSearchItem.setGenderAccepted(savedRoom.getGenderAccepted());
        roomSearchItem.setArea(savedRoom.getArea());
        List<String> listIdUtilitySaved = new ArrayList<>();
        for(int i = 0;i<savedRoom.getUtilityObjects().length;i++){
            listIdUtilitySaved.add(savedRoom.getUtilityObjects()[i].getId());
        }
        String[] idUtilities = new String[listIdUtilitySaved.size()];
        idUtilities = listIdUtilitySaved.toArray(idUtilities);
        roomSearchItem.setUtilities(idUtilities);
        roomSearchItem.setPrice(savedRoom.getPrice());
        roomDetailFragment.setObject(roomSearchItem);
        roomDetailFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        roomDetailFragment.show(getFragmentManager(), "fragment_detail");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            loadData();
        }
    }

    private void loadData() {
        Call<SavedRoom[]> call = cuXaAPI.getLstSavedRoom("Bearer "+ AppUtils.getToken(getActivity()));
        call.enqueue(new Callback<SavedRoom[]>() {
            @Override
            public void onResponse(Call<SavedRoom[]> call, Response<SavedRoom[]> response) {
                if(response.isSuccessful()){
                    tvNumSaved.setText(response.body().length+" phòng đã lưu");
                    numSaved = response.body().length;
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    lstSaved.setLayoutManager(manager);
                    roomSearchItems =  new ArrayList<>(Arrays.asList(response.body()));
                    savedItem = new AdapterSavedItem(getContext(),roomSearchItems,iUnsaveRoom);
                    lstSaved.setAdapter(savedItem);
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<SavedRoom[]> call, Throwable t) {
                Log.d("onFailure: ","cannot get saved room");
                swipeContainer.setRefreshing(false);
            }
        });
    }
}

