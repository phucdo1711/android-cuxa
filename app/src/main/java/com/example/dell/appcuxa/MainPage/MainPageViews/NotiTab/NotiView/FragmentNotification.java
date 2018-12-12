package com.example.dell.appcuxa.MainPage.MainPageViews.NotiTab.NotiView;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.CartListAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.NotiCardAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.RecItemNotiTouchAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.RecyclerItemTouchHelper;
import com.example.dell.appcuxa.MainPage.MainPageViews.MessTab.MessView.Interface.CallbackChatRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView.FragmentQrCode;
import com.example.dell.appcuxa.ObjectModels.NotiModel;
import com.example.dell.appcuxa.ObjectModels.NotiObject;
import com.example.dell.appcuxa.ObjectModels.ObjectChat;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNotification extends Fragment implements RecItemNotiTouchAdapter.RecyclerItemTouchHelperListener,View.OnClickListener,CallbackChatRoom {
    private View mMainView;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    NotiCardAdapter mAdapter;
    List<NotiObject> notiObjects;
    List<NotiObject> notiObjectList;
    ImageView imgView;
    public FragmentNotification(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_notification, container, false);
        //imgView = mMainView.findViewById(R.id.imgView);
        //imgView.setImageBitmap(AppUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.fragment_first, 200, 200));
        recyclerView = mMainView.findViewById(R.id.recNoti);
        refreshLayout = mMainView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLstNoti();
            }
        });
        notiObjectList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAdapter = new NotiCardAdapter(getContext(),notiObjectList,this);
        recyclerView.setAdapter(mAdapter);
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecItemNotiTouchAdapter(getContext(),0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        // making http call and fetching menu json
        prepareCart();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
        return mMainView;
    }

    private void prepareCart() {
        getLstNoti();
    }

    public void getLstNoti(){
        notiObjectList.clear();
        CuXaAPI cuXaAPI = NetworkController.upload();
        Call<NotiModel> call = cuXaAPI.getLstNoti("Bearer "+ AppUtils.getToken(getActivity()),AppUtils.getIdUser(getActivity()));
        call.enqueue(new Callback<NotiModel>() {
            @Override
            public void onResponse(Call<NotiModel> call, Response<NotiModel> response) {
                if(response.isSuccessful()){
                    notiObjects = new ArrayList<>(Arrays.asList(response.body().notiObjects));
                    notiObjectList.addAll(notiObjects);
                    mAdapter.notifyDataSetChanged();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<NotiModel> call, Throwable t) {
                refreshLayout.setRefreshing(false);

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotiCardAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = notiObjectList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final NotiObject deletedItem = notiObjectList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            deleteNoti(deletedItem.getId(), viewHolder.getAdapterPosition());
            // remove the item from recycler view
            /*  mAdapter.removeItem(viewHolder.getAdapterPosition());*/

            // showing snack bar with Undo option
              /*  Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // undo is selected, restore the deleted item
                        mAdapter.restoreItem(deletedItem, deletedIndex);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();*/
        }
    }

    private void deleteNoti(String id, final int adapterPosition) {
        CuXaAPI cuXaAPI = NetworkController.upload();
        Call<ResponseBody> delChatRoom = cuXaAPI.deleteNoti("Bearer "+AppUtils.getToken(getActivity()),id);
        delChatRoom.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    mAdapter.removeItem(adapterPosition);
                    Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Có lỗi sảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void CallBackRoomChat(ObjectChat objectChat) {

    }

    @Override
    public void CallBackNotiScreen(NotiObject notiObject) {
        FragmentDetailNoti fragment = new FragmentDetailNoti();
        //fragmentSearQuickAdva.setCancelable(false);
        fragment.setObject(notiObject);
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        fragment.show(getFragmentManager(), "fragment_qrocde");
    }
}

