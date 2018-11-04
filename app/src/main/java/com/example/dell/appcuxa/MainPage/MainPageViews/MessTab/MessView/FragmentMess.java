package com.example.dell.appcuxa.MainPage.MainPageViews.MessTab.MessView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.MainPage.Adapter.CartListAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.RecyclerItemTouchHelper;
import com.example.dell.appcuxa.ObjectModels.Item;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.internal.Utils;

public class FragmentMess extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,View.OnClickListener{
    private View mMainView;
    ImageView imgView;
    List<Item> cartList;
    RecyclerView recyclerView;

    View mLayoutHeader;

    View mLayoutSearch;
    RobBoldText tvCancel;
    ImageView btnSearch;
    RobEditText mEdtSearch;
    private CartListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    public FragmentMess(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_mess, container, false);
        recyclerView = mMainView.findViewById(R.id.recycler_view);
        coordinatorLayout = mMainView.findViewById(R.id.coordinator_layout);
        mLayoutSearch = mMainView.findViewById(R.id.layout_search);
        mLayoutHeader = mMainView.findViewById(R.id.layout_header);
        tvCancel = mMainView.findViewById(R.id.tv_cancel);
        btnSearch = mMainView.findViewById(R.id.btn_search);
        mEdtSearch = mMainView.findViewById(R.id.edt_search);
        cartList = new ArrayList<>();
        mAdapter = new CartListAdapter(getActivity(), cartList);
        btnSearch.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
            if (viewHolder instanceof CartListAdapter.MyViewHolder) {
                // get the removed item name to display it in snack bar
                String name = cartList.get(viewHolder.getAdapterPosition()).getName();

                // backup of removed item for undo purpose
                final Item deletedItem = cartList.get(viewHolder.getAdapterPosition());
                final int deletedIndex = viewHolder.getAdapterPosition();

                // remove the item from recycler view
                mAdapter.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // undo is selected, restore the deleted item
                        mAdapter.restoreItem(deletedItem, deletedIndex);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
        }

    }
    private void prepareCart() {

        String json = "[{\n" +
                "\t\t\"id\": 1,\n" +
                "\t\t\"name\": \"Salmon Teriyaki\",\n" +
                "\t\t\"description\": \"Roasted salon dumped in soa sauce and mint\",\n" +
                "\t\t\"price\": 140,\n" +
                "\t\t\"thumbnail\": \"https://api.androidhive.info/images/food/1.jpg\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 2,\n" +
                "\t\t\"name\": \"Grilled Mushroom and Vegetables\",\n" +
                "\t\t\"description\": \"Spcie grills mushrooms, cucumber, apples and lot more\",\n" +
                "\t\t\"price\": 150,\n" +
                "\t\t\"thumbnail\": \"https://api.androidhive.info/images/food/2.jpg\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 3,\n" +
                "\t\t\"name\": \"Chicken Overload Meal\",\n" +
                "\t\t\"description\": \"Grilled chicken & tandoori chicken in masala curry\",\n" +
                "\t\t\"price\": 185,\n" +
                "\t\t\"thumbnail\": \"https://api.androidhive.info/images/food/3.jpg\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 4,\n" +
                "\t\t\"name\": \"Chinese Egg Fry\",\n" +
                "\t\t\"description\": \"Exotic eggs Fried served steaming hot\",\n" +
                "\t\t\"price\": 250,\n" +
                "\t\t\"thumbnail\": \"https://api.androidhive.info/images/food/4.jpg\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 5,\n" +
                "\t\t\"name\": \"Chicken Wraps\",\n" +
                "\t\t\"description\": \"Grilled chicken tikka rool wrapped\",\n" +
                "\t\t\"price\": 140,\n" +
                "\t\t\"thumbnail\": \"https://api.androidhive.info/images/food/5.jpg\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 6,\n" +
                "\t\t\"name\": \"Veggie Delight\",\n" +
                "\t\t\"description\": \"Loads of veggies with olives\",\n" +
                "\t\t\"price\": 230,\n" +
                "\t\t\"thumbnail\": \"https://api.androidhive.info/images/food/6.jpg\"\n" +
                "\t},\n" +
                " \t{\n" +
                "\t\t\"id\": 7,\n" +
                "\t\t\"name\": \"Seafood Combo\",\n" +
                "\t\t\"description\": \"combo of prawns, scallop, sliced fish, calanmari, potato fries\",\n" +
                "\t\t\"price\": 330,\n" +
                "\t\t\"thumbnail\": \"https://api.androidhive.info/images/food/7.jpg\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"id\": 8,\n" +
                "\t\t\"name\": \"Full Tandoori\",\n" +
                "\t\t\"description\": \"Chicken roated with lip smacking mayo dressing\",\n" +
                "\t\t\"price\": 430,\n" +
                "\t\t\"thumbnail\": \"https://api.androidhive.info/images/food/8.jpg\"\n" +
                "\t}\n" +
                "]";
        List<Item> items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                int id = object.getInt("id");
                String name = object.getString("name");
                String descri = object.getString("description");
                double price = object.getDouble("price");
                String thumbnail = object.getString("thumbnail");
                Item item = new Item(id,name,descri,price,thumbnail);
                items.add(item);
            }
            cartList.addAll(items);

            // refreshing recycler view
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
}

