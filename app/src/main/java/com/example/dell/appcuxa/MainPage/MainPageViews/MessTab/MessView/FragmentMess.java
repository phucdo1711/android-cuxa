package com.example.dell.appcuxa.MainPage.MainPageViews.MessTab.MessView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dell.appcuxa.Application.ChatApplication;
import com.example.dell.appcuxa.ChatActivity;
import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.CartListAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.RecyclerItemTouchHelper;
import com.example.dell.appcuxa.MainPage.MainPageViews.MainPageActivity;
import com.example.dell.appcuxa.MainPage.MainPageViews.MessTab.MessView.Interface.CallbackChatRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView.FragmentMyRoom;
import com.example.dell.appcuxa.ObjectModels.ChatRoomObj;
import com.example.dell.appcuxa.ObjectModels.Item;
import com.example.dell.appcuxa.ObjectModels.MessageItem;
import com.example.dell.appcuxa.ObjectModels.NotiObject;
import com.example.dell.appcuxa.ObjectModels.ObjectChat;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.internal.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class FragmentMess extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,View.OnClickListener,CallbackChatRoom{
    private View mMainView;
    ImageView imgView;
    List<ObjectChat> cartList;
    RecyclerView recyclerView;
    boolean isSuccess = false;
    View mLayoutHeader;
    CallbackChatRoom callbackChatRoom;
    View mLayoutSearch;
    RobBoldText tvCancel;
    ImageView btnSearch;
    RobEditText mEdtSearch;
    SwipeRefreshLayout swipeRefreshLayout;
    private CartListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    public FragmentMess(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_mess, container, false);
        recyclerView = mMainView.findViewById(R.id.recycler_view);
        callbackChatRoom = this;
        swipeRefreshLayout = mMainView.findViewById(R.id.swipeContainer);
        coordinatorLayout = mMainView.findViewById(R.id.coordinator_layout);
        mLayoutSearch = mMainView.findViewById(R.id.layout_search);
        mLayoutHeader = mMainView.findViewById(R.id.layout_header);
        tvCancel = mMainView.findViewById(R.id.tv_cancel);
        btnSearch = mMainView.findViewById(R.id.btn_search);
        mEdtSearch = mMainView.findViewById(R.id.edt_search);
        cartList = new ArrayList<>();
        mAdapter = new CartListAdapter(getActivity(), cartList,this);
        btnSearch.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLstChatRoom();
            }
        });
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mAdapter = new CartListAdapter(getActivity(), cartList,callbackChatRoom);
                    recyclerView.setAdapter(mAdapter);
                    String newText = s.toString().toLowerCase();
                    List<ObjectChat> items = new ArrayList<>();
                    for (ObjectChat shop : cartList) {
                        String name = shop.getName().toLowerCase();
                        if (name.contains(newText)) {
                            items.add(shop);
                        }
                    }
                    mAdapter.setFilter(items);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(getContext(),0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT, this);
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

    private void getLstChatRoom() {
        cartList.clear();
        CuXaAPI cuXaAPI = NetworkController.upload();
        Call<ChatRoomObj> lstChatRoom = cuXaAPI.getLstChatRoom("Bearer "+AppUtils.getToken(getActivity()));
        lstChatRoom.enqueue(new Callback<ChatRoomObj>() {
            @Override
            public void onResponse(Call<ChatRoomObj> call, retrofit2.Response<ChatRoomObj> response) {
                if(response.isSuccessful()){
                    ChatRoomObj roomObj = response.body();
                    ObjectChat[] objectChat = roomObj.getRows();
                    ArrayList<ObjectChat> lstObject = new ArrayList<>(Arrays.asList(objectChat));
                    cartList.addAll(lstObject);
                    mAdapter.notifyDataSetChanged();

                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ChatRoomObj> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
            if (viewHolder instanceof CartListAdapter.MyViewHolder) {
                // get the removed item name to display it in snack bar
                String name = cartList.get(viewHolder.getAdapterPosition()).getName();

                // backup of removed item for undo purpose
                final ObjectChat deletedItem = cartList.get(viewHolder.getAdapterPosition());
                final int deletedIndex = viewHolder.getAdapterPosition();
                deleteChatRoom(deletedItem.getId(), viewHolder.getAdapterPosition());
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
    private void prepareCart() {
        getLstChatRoom();
    }
    private void gotoSearch() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutHeader.setVisibility(View.GONE);
                mEdtSearch.requestFocus();
                AppUtils.showKeybord(getActivity(), mEdtSearch);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Do nothing
            }
        });
        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    AppUtils.hideSoftKeyboard(getActivity(), mEdtSearch);
                }
                return false;
            }
        });
        mLayoutHeader.startAnimation(animation);
        mLayoutSearch.setVisibility(View.VISIBLE);
        mLayoutSearch.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in));
    }

    private void gotoHeader() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLayoutSearch.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Do nothing
            }
        });
        mEdtSearch.setText("");

        mLayoutSearch.startAnimation(animation);
        mLayoutHeader.setVisibility(View.VISIBLE);
        mLayoutHeader.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_in));
        AppUtils.hideSoftKeyboard(getActivity(), mEdtSearch);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_search:
                gotoSearch();
                break;
            case R.id.tv_cancel:
                gotoHeader();
                break;
            default:
                break;
        }
    }

    @Override
    public void CallBackRoomChat(ObjectChat objectChat) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("object",objectChat);
        startActivity(intent);
    }

    @Override
    public void CallBackNotiScreen(NotiObject notiObject) {
        // do nothing
    }

    public void deleteChatRoom(String id, final int delNum){
        CuXaAPI cuXaAPI = NetworkController.upload();
        Call<ResponseBody> delChatRoom = cuXaAPI.deleteChatRoom("Bearer "+AppUtils.getToken(getActivity()),id);
        delChatRoom.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    isSuccess = true;
                    mAdapter.removeItem(delNum);
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
}

