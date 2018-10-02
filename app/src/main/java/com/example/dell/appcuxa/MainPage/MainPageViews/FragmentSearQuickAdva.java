package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.dell.appcuxa.MainPage.Adapter.PlaceAutoCompleteAdapter;
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
    RadioGroup rgLocation;
    RecyclerView rcHint;
    RecyclerView rcHistory;
    GoogleApiClient mGoogleApiClient;
    PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    protected GeoDataClient mGeoDataClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168),new LatLng(71,136));
    public FragmentSearQuickAdva(){

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
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getContext(),mGeoDataClient,LAT_LNG_BOUNDS,null);
        edtSearchContent.setAdapter(placeAutoCompleteAdapter);
        edtSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    // execute method for searching
                    geoLocate();
                }
                return false;
            }
        });
        imgBack.setOnClickListener(this);
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.imgBack:
                FragmentSearQuickAdva.this.dismiss();
                break;
            case R.id.btnSearchAdvance:
                FragmentSearchAdvance fragment= new FragmentSearchAdvance();
                //fragmentSearQuickAdva.setCancelable(false);
                fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                fragment.show(getFragmentManager(),"fragment_search_advance");
                break;
        }
    }
     private void geoLocate() {
        String searchContent = edtSearchContent.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchContent,1);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("geoLocate",e.getMessage());
        }
        if(list.size()>0){
            Address address = list.get(0);
            Log.d("geoLocate, a location",address.toString());
        }
    }
    public void init(){
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
}

