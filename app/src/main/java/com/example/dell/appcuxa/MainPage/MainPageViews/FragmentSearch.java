package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.ListRoomAdapter;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.IBackToListTopScreen;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicSaveRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView.FragmentEditProfile;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomInfo;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.base.FragmentCommon;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class FragmentSearch extends FragmentCommon implements ILogicSaveRoom, IBackToListTopScreen {
    List<RoomInfo> roomInfoList = new ArrayList<>();
    @BindView(R.id.recRoomList)
    public RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    @BindView(R.id.lnQuickSearch)
    RobButton btnUpRoom;
    RobButton btnLiveTogether;
    String token = "";
    SharedPreferences sharedPreferences;
    ILogicSaveRoom iLogicSaveRoom;
    RobButton btnFindRoom;
    CuXaAPI fileService;
    LinearLayout lnProgressbar;
    public LinearLayout edtQuickSearch;
    IBackToListTopScreen iBackToListTopScreen;
    public ListRoomAdapter listRoomAdapter;
    List<RoomSearchItem> roomSearchItemList;
    int size = 0;

    public FragmentSearch() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(mView);
        iLogicSaveRoom = this;
        fileService = NetworkController.upload();
        btnLiveTogether = mView.findViewById(R.id.btnLiveTogether);
        recyclerView = mView.findViewById(R.id.recRoomList);
        edtQuickSearch = mView.findViewById(R.id.lnQuickSearch);
        btnUpRoom = mView.findViewById(R.id.btnUpRoom);
        btnFindRoom = mView.findViewById(R.id.btnFindRoom);
        swipeContainer = (SwipeRefreshLayout) mView.findViewById(R.id.swipeContainer);
        lnProgressbar = mView.findViewById(R.id.lnProgressbar);
        iBackToListTopScreen = this;
        btnUpRoom.setOnClickListener(this);
        btnLiveTogether.setOnClickListener(this);
        sharedPreferences = getActivity().getSharedPreferences("login_data", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        if (getUserVisibleHint()) {
            if (size > 0) {
                //do nothing
            } else {
                loadData();
            }
        }
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListTop();
            }
        });

        if (AppUtils.isServiceOk(getActivity())) {
            btnFindRoom.setOnClickListener(this);
        }
        edtQuickSearch.setOnClickListener(this);

        return mView;
    }

    private void getListTop() {
        Call<ObjectListByOption> getListTop = fileService.getListTop("Bearer " + token, "application/json");
        getListTop.enqueue(new Callback<ObjectListByOption>() {
            @Override
            public void onResponse(Call<ObjectListByOption> call, Response<ObjectListByOption> response) {
                if (response.isSuccessful()) {
                    lnProgressbar.setVisibility(View.GONE);
                    roomInfoList.clear();
                    swipeContainer.setRefreshing(false);
                    roomSearchItemList = new ArrayList<>(Arrays.asList(response.body().getLstRoom()));
                    size = roomSearchItemList.size();
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(manager);
                    listRoomAdapter = new ListRoomAdapter(getContext(), roomSearchItemList, iLogicSaveRoom, iBackToListTopScreen);
                    recyclerView.setAdapter(listRoomAdapter);
                    listRoomAdapter.notifyDataSetChanged();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ObjectListByOption> call, Throwable t) {
                lnProgressbar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    protected void unit(View v) {

    }


    @Override
    protected void setPermission() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        switch (id) {
            case R.id.lnQuickSearch:
                FragmentSearQuickAdva fragmentSearQuickAdva = new FragmentSearQuickAdva();
                //fragmentSearQuickAdva.setCancelable(false);
                fragmentSearQuickAdva.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                fragmentSearQuickAdva.show(getFragmentManager(), "fragment_search");
                break;
            case R.id.btnUpRoom:
                FragmentUpRoom fragmentUpRoom = new FragmentUpRoom();
                //fragmentSearQuickAdva.setCancelable(false);
                fragmentUpRoom.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                fragmentUpRoom.show(getFragmentManager(), "fragment_uproom");
                break;
            case R.id.btnFindRoom:

                FragmentSearchAdvance fragmentAd = new FragmentSearchAdvance();
                //fragmentSearQuickAdva.setCancelable(false);
                fragmentAd.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                fragmentAd.show(getFragmentManager(), "fragment_search_advance");
                break;
            case R.id.btnLiveTogether:
                FragmentFindPeople fragment = new FragmentFindPeople();
                //fragmentSearQuickAdva.setCancelable(false);
                fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                fragment.show(getFragmentManager(), "fragment_live_together");
        }
    }

    @Override
    public void saveRoom(RoomSearchItem id) {
        if (AppUtils.haveNetworkConnection(getActivity())) {
            fileService = NetworkController.upload();
            Call<ResponseBody> call = fileService.saveRoom("Bearer " + AppUtils.getToken(getActivity()), id.getId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        listRoomAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(), "Có lỗi sảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Đéo có mạng", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void unSaveRoom(RoomSearchItem id) {
        saveRoom(id);
    }

    @Override
    public void backToScreen(RoomSearchItem room) {
        //do nothing
    }

    @Override
    public void backToListTopScreen(RoomSearchItem roomInfo) {
        RoomDetailFragment roomDetailFragment = new RoomDetailFragment();
        roomDetailFragment.setObject(roomInfo);
        roomDetailFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        roomDetailFragment.show(getFragmentManager(), "fragment_detail");
    }

    private void loadData() {
        getListTop();
    }

}
