package com.example.dell.appcuxa.MainPage.MainPageViews.SavedTab.SavedView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class FragmentSaved extends Fragment implements ILogicSaveRoom {
    private View mMainView;
    RobBoldText tvNumSaved;
    RecyclerView lstSaved;
    CuXaAPI cuXaAPI;
    ILogicSaveRoom iLogicSaveRoom;
    AdapterSavedItem savedItem;
    public FragmentSaved(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_saved, container, false);
        //imgView = mMainView.findViewById(R.id.imgView);
        initView();
        iLogicSaveRoom = this;
        cuXaAPI = NetworkController.upload();
        Call<SavedRoom[]> call = cuXaAPI.getLstSavedRoom("Bearer "+ AppUtils.getToken(getActivity()));
        call.enqueue(new Callback<SavedRoom[]>() {
            @Override
            public void onResponse(Call<SavedRoom[]> call, Response<SavedRoom[]> response) {
                if(response.isSuccessful()){
                    tvNumSaved.setText(response.body().length+" phòng đã lưu");
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    lstSaved.setLayoutManager(manager);
                   List<SavedRoom> roomSearchItems =  new ArrayList<>(Arrays.asList(response.body()));
                   savedItem = new AdapterSavedItem(getContext(),roomSearchItems,iLogicSaveRoom);
                   lstSaved.setAdapter(savedItem);
                }
            }

            @Override
            public void onFailure(Call<SavedRoom[]> call, Throwable t) {
                Log.d("onFailure: ","cannot get saved room");
            }
        });
        //imgView.setImageBitmap(AppUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.fragment_first, 200, 200));
        return mMainView;
    }

    private void initView() {
        tvNumSaved = mMainView.findViewById(R.id.tvNumSaved);
        lstSaved = mMainView.findViewById(R.id.lstSaved);
    }

    @Override
    public void saveRoom(String id) {
        if(AppUtils.haveNetworkConnection(getActivity())){
            cuXaAPI = NetworkController.upload();
            Call<ResponseBody> call = cuXaAPI.saveRoom("Bearer "+ AppUtils.getToken(getActivity()),id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
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
    public void unSaveRoom(String id) {
       saveRoom(id);
    }
}

