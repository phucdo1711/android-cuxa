package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.AdapterSearchResultRoom;
import com.example.dell.appcuxa.MainPage.Adapter.PlaceAutoCompleteAdapter;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicSaveRoom;
import com.example.dell.appcuxa.ObjectModels.LocationRoom;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.Price;
import com.example.dell.appcuxa.ObjectModels.RoomObject;
import com.example.dell.appcuxa.ObjectModels.RoomSearch;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.RoomSearchResult;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSearQuickAdva extends DialogFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, ILogicSaveRoom {
    private View mMainView;
    ImageView imgBack;
    AutoCompleteTextView edtSearchContent;
    ImageView imgMap;
    RadioButton rdLocation;
    LinearLayout lnResult;
    RobButton btnHN, btnSG, btnCurrLocation;
    RobBoldText tvNumResult;
    RobBoldText tvTypeResult;
    RadioButton rdPrice;
    RadioButton rdTienNghi;
    RadioGroup rgLocation;
    CuXaAPI fileService;
    ObjectListByOption priceOption = new ObjectListByOption();
    ObjectListByOption locationOption = new ObjectListByOption();
    ObjectListByOption tienNghiOption = new ObjectListByOption();
    boolean a = false;
    boolean isFromGoogleMap = false;
    RadioGroup rgOption;
    RecyclerView listSearchResultPV, recPrice,recTienNghi;
    NestedScrollView nstViewInfo;
    RecyclerView rcHint;
    RecyclerView rcHistory;
    ILogicSaveRoom iLogicSaveRoom;
    GoogleApiClient mGoogleApiClient;
    PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    protected GeoDataClient mGeoDataClient;
    private LocationManager locationManager;
    private LocationListener locationListener;
    double latHN =  21.0277644;
    double lonHN =  105.8341598;
    double latHCM = 10.8230989;
    double lonHCN = 106.62966379999999;
    AdapterSearchResultRoom adapterSearchResultRoom;
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
        iLogicSaveRoom = this;
        fileService = NetworkController.upload();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        listSearchResultPV.setLayoutManager(manager);
        recTienNghi.setLayoutManager(manager1);
        recPrice.setLayoutManager(manager2);
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
            listSearchResultPV.setVisibility(View.VISIBLE);
        } else {
            if(isFromGoogleMap){
                rgOption.setVisibility(View.GONE);
                rgLocation.setVisibility(View.GONE);
                nstViewInfo.setVisibility(View.GONE);
                listSearchResultPV.setVisibility(View.VISIBLE);
                recPrice.setVisibility(View.GONE);
                recTienNghi.setVisibility(View.GONE);
            }else{
                lnResult.setVisibility(View.GONE);
                rgOption.setVisibility(View.GONE);
                rgLocation.setVisibility(View.VISIBLE);
                nstViewInfo.setVisibility(View.VISIBLE);
                listSearchResultPV.setVisibility(View.GONE);
            }
        }
        if (locationOption!=null && null!=locationOption.getCount()&& Integer.valueOf(locationOption.getCount()) > 0) {
            tvNumResult.setText(locationOption.getCount());
            tvTypeResult.setText("về phạm vi");
            adapterSearchResultRoom = new AdapterSearchResultRoom(getContext(), locationOption, iLogicSaveRoom,fileService);
            listSearchResultPV.setAdapter(adapterSearchResultRoom);
            adapterSearchResultRoom.notifyDataSetChanged();
        }
        rdPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    recPrice.setVisibility(View.VISIBLE);
                    listSearchResultPV.setVisibility(View.GONE);
                    recTienNghi.setVisibility(View.GONE);
                    if (priceOption.count != null && Integer.valueOf(priceOption.count) > 0) {
                        tvNumResult.setText(priceOption.count);
                        tvTypeResult.setText("về giá");
                        AdapterSearchResultRoom adapterSearchResultRoom = new AdapterSearchResultRoom(getContext(), priceOption, iLogicSaveRoom,fileService);
                        recPrice.setAdapter(adapterSearchResultRoom);
                        adapterSearchResultRoom.notifyDataSetChanged();
                    }
                }
            }
        });
        rdLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    recPrice.setVisibility(View.GONE);
                    listSearchResultPV.setVisibility(View.VISIBLE);
                    recTienNghi.setVisibility(View.GONE);
                    if (locationOption.count != null && Integer.valueOf(locationOption.count) > 0) {
                        tvNumResult.setText(locationOption.count);
                        tvTypeResult.setText("về phạm vi");
                        adapterSearchResultRoom = new AdapterSearchResultRoom(getContext(), locationOption, iLogicSaveRoom,fileService);
                        listSearchResultPV.setAdapter(adapterSearchResultRoom);
                        adapterSearchResultRoom.notifyDataSetChanged();
                    }
                }
            }
        });
        rdTienNghi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    recPrice.setVisibility(View.GONE);
                    listSearchResultPV.setVisibility(View.GONE);
                    recTienNghi.setVisibility(View.VISIBLE);
                    if (tienNghiOption.count != null && Integer.valueOf(tienNghiOption.count) > 0) {
                        tvNumResult.setText(tienNghiOption.count);
                        tvTypeResult.setText("về tiện nghi");
                        AdapterSearchResultRoom adapterSearchResultRoom = new AdapterSearchResultRoom(getContext(), tienNghiOption, iLogicSaveRoom,fileService);
                        recTienNghi.setAdapter(adapterSearchResultRoom);
                        adapterSearchResultRoom.notifyDataSetChanged();
                    }else{
                        tvNumResult.setText("0");
                        tvTypeResult.setText("về tiện nghi");
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
            case R.id.imgMap:
                GoogleMapFragment mapFragment = new GoogleMapFragment();
                mapFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                mapFragment.show(getFragmentManager(), "fragment_map");
                break;
            case R.id.btnHN:
                searchByLatLon(latHN,lonHN);
                break;
            case R.id.btnSG:
                searchByLatLon(latHCM,lonHCN);
                break;
            case R.id.btnCurrentLocation:
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }else{
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location!=null){
                        searchByLatLon(location.getLatitude(),location.getLongitude());
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location!=null){
                    searchByLatLon(location.getLatitude(),location.getLongitude());
                }

            }
        }
    }

    private void searchByLatLon(double lat, double lon) {
        RoomSearch roomObject = new RoomSearch();
        List<Double> doubles = new ArrayList<>();
        doubles.add(lon);
        doubles.add(lat);
        Double[] latlon = new Double[doubles.size()];
        latlon = doubles.toArray(latlon);
        LocationRoom locationRoom = new LocationRoom("Point",latlon);
        roomObject.setLocation(locationRoom);

        Price price = new Price("0","1000000000");
        roomObject.setLocation(locationRoom);
        roomObject.setDistance("10000");
        roomObject.setPrice(price);
        Call<RoomSearchResult> searchRoom = fileService.searchRoom("", roomObject);

        searchRoom.enqueue(new Callback<RoomSearchResult>() {
            @Override
            public void onResponse(Call<RoomSearchResult> call, Response<RoomSearchResult> response) {
                if(response.isSuccessful()){
                    lnResult.setVisibility(View.VISIBLE);
                    rgLocation.setVisibility(View.GONE);
                    nstViewInfo.setVisibility(View.GONE);
                    listSearchResultPV.setVisibility(View.VISIBLE);
                    rgOption.setVisibility(View.GONE);
                    tvNumResult.setText(response.body().getObjectByLocation().getCount());
                    tvTypeResult.setText("về phạm vi");
                    AdapterSearchResultRoom adapterSearchResultRoom = new AdapterSearchResultRoom(getContext(), response.body().getObjectByLocation(), iLogicSaveRoom,fileService);
                    listSearchResultPV.setAdapter(adapterSearchResultRoom);
                    adapterSearchResultRoom.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RoomSearchResult> call, Throwable t) {
                Toast.makeText(getContext(), "Có lỗi trong quá trình tìm kiếm, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
            }
        });
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
        btnHN = mMainView.findViewById(R.id.btnHN);
        btnSG = mMainView.findViewById(R.id.btnSG);
        btnCurrLocation = mMainView.findViewById(R.id.btnCurrentLocation);
        btnHN.setOnClickListener(this);
        btnSG.setOnClickListener(this);
        btnCurrLocation.setOnClickListener(this);
        lnResult = mMainView.findViewById(R.id.tvResult);
        tvNumResult = mMainView.findViewById(R.id.tvNumResult);
        tvTypeResult = mMainView.findViewById(R.id.tvTypeResult);
        rdLocation = mMainView.findViewById(R.id.rdPhamVi);
        rdTienNghi = mMainView.findViewById(R.id.rdTienNghi);
        rdPrice = mMainView.findViewById(R.id.rdGiaTien);
        nstViewInfo = mMainView.findViewById(R.id.viewInfo);
        listSearchResultPV = mMainView.findViewById(R.id.listSearchResultPV);
        recPrice = mMainView.findViewById(R.id.listSearchResultPrice);
        recTienNghi = mMainView.findViewById(R.id.listSearchResultTN);
        rgOption = mMainView.findViewById(R.id.rgOption);
        imgBack = mMainView.findViewById(R.id.imgBack);
        edtSearchContent = mMainView.findViewById(R.id.edtSearch);
        imgMap = mMainView.findViewById(R.id.imgMap);
        imgMap.setOnClickListener(this);
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
    public void isFromGoogleMap(boolean isFromGoogleMap){
        this.isFromGoogleMap = isFromGoogleMap;
    }

    /**
     * Kết quả chuyển từ màn hình tìm kiếm nâng cao.
     *
     * @param result
     */
    public void lstSearchShop(RoomSearchResult result) {
        locationOption = result.objectByLocation;
        priceOption = result.objectByPrice;
        tienNghiOption = new ObjectListByOption();
    }

    @Override
    public void saveRoom(RoomSearchItem item) {
        if(AppUtils.haveNetworkConnection(getActivity())){
            Call<ResponseBody> call = fileService.saveRoom("Bearer "+ AppUtils.getToken(getActivity()),item.getId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        //do nothing
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

    /**
     * Gọi lại api để chuyển trạng thái
     */
    @Override
    public void unSaveRoom(RoomSearchItem roomSearchItem) {
        if(AppUtils.haveNetworkConnection(getActivity())){
            Call<ResponseBody> call = fileService.saveRoom("Bearer "+ AppUtils.getToken(getActivity()),roomSearchItem.getId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        //do nothing
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
    public void backToScreen(RoomSearchItem room) {
        RoomDetailFragment roomDetailFragment = new RoomDetailFragment();
        roomDetailFragment.setObject(room, AppUtils.getIdUser(getActivity()));
        roomDetailFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        roomDetailFragment.show(getFragmentManager(), "fragment_detail");
    }
}

