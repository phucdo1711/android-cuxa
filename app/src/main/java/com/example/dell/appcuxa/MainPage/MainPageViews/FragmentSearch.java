package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.example.dell.appcuxa.ObjectModels.RoomInfo;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class FragmentSearch extends FragmentCommon{
    List<RoomInfo> roomInfoList = new ArrayList<>();
    @BindView(R.id.recRoomList)
    public RecyclerView recyclerView;
    @BindView(R.id.lnQuickSearch)
    RobButton btnUpRoom;
    String token = "";
    SharedPreferences sharedPreferences;
    RobButton btnFindRoom;
    CuXaAPI fileService;
    public LinearLayout edtQuickSearch;
    public ListRoomAdapter listRoomAdapter;
    public FragmentSearch(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(mView);
        recyclerView = mView.findViewById(R.id.recRoomList);
        edtQuickSearch = mView.findViewById(R.id.lnQuickSearch);
        btnUpRoom = mView.findViewById(R.id.btnUpRoom);
        btnFindRoom = mView.findViewById(R.id.btnFindRoom);
        btnUpRoom.setOnClickListener(this);
        sharedPreferences = getActivity().getSharedPreferences("login_data", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        getListTop();
        if(AppUtils.isServiceOk(getActivity())){
            btnFindRoom.setOnClickListener(this);
        }
        edtQuickSearch.setOnClickListener(this);

        return mView;
    }

    private void getListTop() {
        fileService = NetworkController.upload();
        Call<ResponseBody> getListTop = fileService.getListTop("Bearer "+ token,"application/json");
        getListTop.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    roomInfoList.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for(int i = 0;i<jsonArray.length();i++){
                         JSONObject objectRoom = (JSONObject) jsonArray.get(i);
                         String name = objectRoom.getString("name");
                         String type = objectRoom.getString("type");
                         JSONArray arrayImage = objectRoom.getJSONArray("images");
                         List<String> stringImage = new ArrayList<>();
                         for(int j = 0;j<arrayImage.length();j++){
                             JSONObject objectImage = (JSONObject) arrayImage.get(j);
                             String path = objectImage.getString("src");
                             stringImage.add(path);
                         }
                         String address = objectRoom.getString("address");
                         String price = objectRoom.getString("price");
                         RoomInfo info = new RoomInfo();
                         info.setAddress(address);
                         info.setNameRoom(name);
                         info.setPrice(price);
                         info.setPurpose(type);
                         info.setImage(stringImage);
                         roomInfoList.add(info);
                        }
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(manager);
                        listRoomAdapter = new ListRoomAdapter(getContext(),roomInfoList);
                        recyclerView.setAdapter(listRoomAdapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(act, "Đã có lỗi sảy ra khi lấy danh sách phòng", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(act, "Đã có lỗi sảy ra khi lấy danh sách phòng", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
        switch (id){
            case R.id.lnQuickSearch:
               FragmentSearQuickAdva fragmentSearQuickAdva = new FragmentSearQuickAdva();
               //fragmentSearQuickAdva.setCancelable(false);
                fragmentSearQuickAdva.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
               fragmentSearQuickAdva.show(getFragmentManager(),"fragment_search");
                break;
            case R.id.btnUpRoom:
                FragmentUpRoom fragmentUpRoom= new FragmentUpRoom();
                //fragmentSearQuickAdva.setCancelable(false);
                fragmentUpRoom.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                fragmentUpRoom.show(getFragmentManager(),"fragment_uproom");
                break;
            case R.id.btnFindRoom:
                GoogleMapFragment mapFragment = new GoogleMapFragment();
                mapFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                mapFragment.show(getFragmentManager(),"fragment_map");
                break;
        }
    }
}
