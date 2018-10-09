package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.MainPage.Adapter.AdapterSearchResultRoom;
import com.example.dell.appcuxa.MainPage.Adapter.PlaceAutoCompleteAdapter;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomSearchResult;
import com.example.dell.appcuxa.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentSearQuickAdva extends DialogFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private View mMainView;
    ImageView imgBack;
    AutoCompleteTextView edtSearchContent;
    Button btnAdvance;
    RadioButton rdLocation;
    LinearLayout lnResult;
    RobBoldText tvNumResult;
    RobBoldText tvTypeResult;
    RadioButton rdPrice;
    RadioGroup rgLocation;
    ObjectListByOption priceOption = new ObjectListByOption();
    ObjectListByOption locationOption = new ObjectListByOption();
    boolean a = false;
    RadioGroup rgOption;
    RecyclerView listSearchResult;
    NestedScrollView nstViewInfo;
    RecyclerView rcHint;
    RecyclerView rcHistory;
    GoogleApiClient mGoogleApiClient;
    PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    protected GeoDataClient mGeoDataClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    public FragmentSearQuickAdva() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.search_layout, container, false);
        init();
      /*  mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();*/
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        listSearchResult.setLayoutManager(manager);
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getContext(), mGeoDataClient, LAT_LNG_BOUNDS, null);
        edtSearchContent.setAdapter(placeAutoCompleteAdapter);
        edtSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    // execute method for searching
                    geoLocate();
                }
                return false;
            }
        });
        imgBack.setOnClickListener(this);

        if (a) {
            lnResult.setVisibility(View.VISIBLE);
            rgOption.setVisibility(View.VISIBLE);
            rgLocation.setVisibility(View.GONE);
            nstViewInfo.setVisibility(View.GONE);
            listSearchResult.setVisibility(View.VISIBLE);
        } else {
            lnResult.setVisibility(View.GONE);
            rgOption.setVisibility(View.GONE);
            rgLocation.setVisibility(View.VISIBLE);
            nstViewInfo.setVisibility(View.VISIBLE);
            listSearchResult.setVisibility(View.GONE);
        }
        if (locationOption.count != null && Integer.valueOf(locationOption.count) > 0) {
            tvNumResult.setText(locationOption.count);
            tvTypeResult.setText("về phạm vi");
            AdapterSearchResultRoom adapterSearchResultRoom = new AdapterSearchResultRoom(getContext(),locationOption);
            listSearchResult.setAdapter(adapterSearchResultRoom);
            adapterSearchResultRoom.notifyDataSetChanged();
        }
        rdPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if (locationOption.count != null && Integer.valueOf(priceOption.count) > 0) {
                        tvNumResult.setText(priceOption.count);
                        tvTypeResult.setText("về giá");
                        AdapterSearchResultRoom adapterSearchResultRoom = new AdapterSearchResultRoom(getContext(),priceOption);
                        listSearchResult.setAdapter(adapterSearchResultRoom);
                        adapterSearchResultRoom.notifyDataSetChanged();
                    }
                }
            }
        });
        rdLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if (locationOption.count != null && Integer.valueOf(locationOption.count) > 0) {
                        tvNumResult.setText(locationOption.count);
                        tvTypeResult.setText("về phạm vi");
                        AdapterSearchResultRoom adapterSearchResultRoom = new AdapterSearchResultRoom(getContext(),locationOption);
                        listSearchResult.setAdapter(adapterSearchResultRoom);
                        adapterSearchResultRoom.notifyDataSetChanged();
                    }
                }
            }
        });

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imgBack:
                FragmentSearQuickAdva.this.dismiss();
                break;
            case R.id.btnSearchAdvance:
                FragmentSearchAdvance fragment = new FragmentSearchAdvance();
                //fragmentSearQuickAdva.setCancelable(false);
                fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                fragment.show(getFragmentManager(), "fragment_search_advance");
                break;
        }
    }

    private void geoLocate() {
        String searchContent = edtSearchContent.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchContent, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("geoLocate", e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d("geoLocate, a location", address.toString());
        }
    }

    public void init() {
        lnResult = mMainView.findViewById(R.id.tvResult);
        tvNumResult = mMainView.findViewById(R.id.tvNumResult);
        tvTypeResult = mMainView.findViewById(R.id.tvTypeResult);
        rdLocation = mMainView.findViewById(R.id.rdPhamVi);
        rdPrice = mMainView.findViewById(R.id.rdGiaTien);
        nstViewInfo = mMainView.findViewById(R.id.viewInfo);
        listSearchResult = mMainView.findViewById(R.id.listSearchResult);
        rgOption = mMainView.findViewById(R.id.rgOption);
        imgBack = mMainView.findViewById(R.id.imgBack);
        edtSearchContent = mMainView.findViewById(R.id.edtSearch);
        btnAdvance = mMainView.findViewById(R.id.btnSearchAdvance);
        btnAdvance.setOnClickListener(this);
        rgLocation = mMainView.findViewById(R.id.rgLocation);
        rcHint = mMainView.findViewById(R.id.recHint);
        rcHistory = mMainView.findViewById(R.id.recHistory);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Check giá trị để biết là vừa tìm kiếm từ màn hình tìm kiếm nâng cao chuyển qua
     *
     * @param isFromSearchResult
     */
    public void isFromSearchResult(boolean isFromSearchResult) {
        a = isFromSearchResult;
    }

    /**
     * Kết quả chuyển từ màn hình tìm kiếm nâng cao.
     *
     * @param result
     */
    public void lstSearchShop(RoomSearchResult result) {
        locationOption = result.objectByLocation;
        priceOption = result.objectByPrice;
    }

}

