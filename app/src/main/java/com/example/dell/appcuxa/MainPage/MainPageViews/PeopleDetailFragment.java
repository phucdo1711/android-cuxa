package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.appcuxa.ChatActivity;
import com.example.dell.appcuxa.CustomeView.MyGridView;
import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.CheckBoxAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.SlideImageAdapter;
import com.example.dell.appcuxa.ObjectModels.ObjectChat;
import com.example.dell.appcuxa.ObjectModels.RoomCreatedObj;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.UtilityObject;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleDetailFragment extends DialogFragment implements View.OnClickListener {
    public View mMainView;
    public RobBoldText tvName;
    CuXaAPI fileService;
    RobButton btnMesNow;
    List<UtilityObject> utilityObjectList = new ArrayList<>();
    private ImageView imgBack;
    private MyGridView gvCheckBox;
    ViewPager imgHinh;
    CircleIndicator circleIndicator;
    private RoomSearchItem roomSearchItem;
    public PeopleDetailFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_detail_people, container, false);
        init();
        fileService = NetworkController.upload();
        getAllUtilities();
        String name = roomSearchItem.getLandLord().getName();
        String image = roomSearchItem.getLandLord().getPicture();
        tvName.setText(name==null?"Người dùng cư xá":name);
        List<String> lstString = new ArrayList<>();
        lstString.add(roomSearchItem.getLandLord().getPicture());
        SlideImageAdapter slideImageAdapter = new SlideImageAdapter(getContext(),lstString);
        imgHinh.setAdapter(slideImageAdapter);
        slideImageAdapter.notifyDataSetChanged();
        circleIndicator.setViewPager(imgHinh);
        return mMainView;
    }

    private void init() {
        btnMesNow = mMainView.findViewById(R.id.btnMesNow);
        btnMesNow.setOnClickListener(this);
        imgHinh =mMainView.findViewById(R.id.imgHinh);
        circleIndicator = mMainView.findViewById(R.id.indicator);
        gvCheckBox = mMainView.findViewById(R.id.gridCheckBox);
        tvName = mMainView.findViewById(R.id.tvName);
        imgBack = mMainView.findViewById(R.id.imgBackTb);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBackTb:
                PeopleDetailFragment.this.dismiss();
                break;
            case R.id.btnMesNow:
                String idUser = roomSearchItem.getLandLord().getId();
                String name = roomSearchItem.getName();
                RoomCreatedObj obj = new RoomCreatedObj(idUser,name);
                Call<ObjectChat> createChat = fileService.createRoom("Bearer "+ AppUtils.getToken(getActivity()),obj);
                createChat.enqueue(new Callback<ObjectChat>() {
                    @Override
                    public void onResponse(Call<ObjectChat> call, Response<ObjectChat> response) {
                        if(response.isSuccessful()){
                            Intent intent = new Intent(getActivity(),ChatActivity.class);
                            intent.putExtra("object",response.body());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ObjectChat> call, Throwable t) {

                    }
                });
                break;
        }
    }
    public RoomSearchItem setObject(RoomSearchItem roomSearchItem){
        this.roomSearchItem = roomSearchItem;
        return roomSearchItem;
    }
    public void getAllUtilities(){
        //progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = fileService.getAllUtilities("code");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    //progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        //JSONArray  jsonArray = jsonObject.getJSONArray("rows");
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for (int i = 0; i < 12; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            UtilityObject utilityObject =
                                    new UtilityObject(object.getString("id"),object.getString("name"),object.getString("code"));
                            utilityObjectList.add(utilityObject);
                        }
                        if(roomSearchItem!=null){
                            String[] utilitiesSelected = roomSearchItem.getUtilities();
                            CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(utilityObjectList,getContext(),utilitiesSelected,true);
                            gvCheckBox.setAdapter(checkBoxAdapter);
                            checkBoxAdapter.notifyDataSetChanged();
                        }else{
                            CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(utilityObjectList,getContext());
                            gvCheckBox.setAdapter(checkBoxAdapter);
                            checkBoxAdapter.notifyDataSetChanged();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
            }
        });
    }
}
