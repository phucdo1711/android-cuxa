package com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.AdapterMyRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.FragmentSearQuickAdva;
import com.example.dell.appcuxa.MainPage.MainPageViews.FragmentSearchAdvance;
import com.example.dell.appcuxa.MainPage.MainPageViews.FragmentUpRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.MainPageActivity;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentMyRoom extends DialogFragment implements AdapterMyRoom.ILogicDeleteRoom,
        View.OnClickListener{
    CuXaAPI fileService;
    private View mMainView;
    private SwipeRefreshLayout swipeContainer;
    AdapterMyRoom adapterMyRoom;
    private RobButton btnCreateNew;
    List<RoomSearchItem> roomSearchResults;
    private ImageView imgBack;
    private RecyclerView rcMyRoom;
    AdapterMyRoom.ILogicDeleteRoom logicDeleteRoom = this;
    private SpinKitView progressBar;

    public FragmentMyRoom() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_my_room, container, false);
        init();
        getListMyRoom("Bearer "+ AppUtils.getToken(getActivity()),"application/json",AppUtils.getIdUser(getActivity()));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListMyRoom("Bearer "+ AppUtils.getToken(getActivity()),"application/json",AppUtils.getIdUser(getActivity()));
            }
        });
        return mMainView;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imgBack:
                FragmentMyRoom.this.dismiss();
                break;
             case R.id.btnCreateNew:
                 FragmentUpRoom fragment = new FragmentUpRoom();
                 //fragmentSearQuickAdva.setCancelable(false);
                 fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                 fragment.show(getFragmentManager(), "fragment_uproom");
            break;
        }
    }


    /**
     * Ánh xạ view
     */
    public void init() {
        swipeContainer = (SwipeRefreshLayout) mMainView.findViewById(R.id.swipeContainer);
        progressBar =mMainView.findViewById(R.id.spin_kit);
        btnCreateNew = mMainView.findViewById(R.id.btnCreateNew);
        imgBack = mMainView.findViewById(R.id.imgBack);
        rcMyRoom = mMainView.findViewById(R.id.rvMyRoom);
        imgBack.setOnClickListener(this);
        btnCreateNew.setOnClickListener(this);

    }
    public void getListMyRoom(String token, String header, String landLord){
        if(AppUtils.haveNetworkConnection(getContext())){
            fileService = NetworkController.upload();
            Call<ObjectListByOption> getMyRooms = fileService.getMyRooms(token,header,landLord);
            getMyRooms.enqueue(new Callback<ObjectListByOption>() {
                @Override
                public void onResponse(Call<ObjectListByOption> call, Response<ObjectListByOption> response) {
                    if(response.isSuccessful()){
                        RoomSearchItem[] objectListByOptions = response.body().getLstRoom();

                        roomSearchResults =new ArrayList<>(Arrays.asList(objectListByOptions));
                        adapterMyRoom = new AdapterMyRoom(getContext(),logicDeleteRoom,roomSearchResults);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        rcMyRoom.setLayoutManager(manager);
                        rcMyRoom.setAdapter(adapterMyRoom);
                        adapterMyRoom.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                    swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<ObjectListByOption> call, Throwable t) {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(getActivity(), "Có lỗi sảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            swipeContainer.setRefreshing(false);
            Toast.makeText(getContext(), "Đéo có mạng", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void deleteRoom(final RoomSearchItem info) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Xóa phòng");
        builder.setMessage("Bạn có chắc chắn muốn xóa phòng này không?.");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.setVisibility(View.VISIBLE);
                deleteRoomFunc(info);
            }
        });
        builder.show();

    }

    @Override
    public void backToEdit(RoomSearchItem info) {
        FragmentUpRoom fragment = new FragmentUpRoom();
        //fragmentSearQuickAdva.setCancelable(false);
        //FragmentUpRoom.roomSearchItem = info;
        fragment.setDataEdit(info);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        fragment.show(getFragmentManager(), "fragment_search_advance");

    }

    public void deleteRoomFunc(final RoomSearchItem item){
        fileService = NetworkController.upload();
        Call<ResponseBody> deleteRoom = fileService.deleteRoom("Bearer "+ AppUtils.getToken(getActivity()),item.getId());
        deleteRoom.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "Xóa phòng thành công", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    roomSearchResults.remove(item);
                    adapterMyRoom.notifyDataSetChanged();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Có lỗi sảy ra: "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Có lỗi sảy ra", Toast.LENGTH_SHORT).show();

            }
        });
    }

}

