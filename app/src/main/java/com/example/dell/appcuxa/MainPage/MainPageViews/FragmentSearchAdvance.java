package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.MyGridView;
import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CustomeView.RobRadioButton;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.CheckBoxAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.PlaceAutoCompleteAdapter;
import com.example.dell.appcuxa.ObjectModels.UtilityObject;
import com.example.dell.appcuxa.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSearchAdvance extends DialogFragment implements View.OnClickListener {
    public View mMainView;
    RobButton btnSearch;
    public ImageView imgBack;
    MyGridView gvCheckBox;
    CuXaAPI fileService;
    SpinKitView progressBar;
    RadioButton rbBelowMil, rbBtOneVsTwo, rbTwoVsThree, rbMtThree;
    RadioButton rb500m,rb1km,rb5km,rb8km,rb10km;
    List<Double> doubleList = new ArrayList<>();
    AutoCompleteTextView edtAddress;
    protected GeoDataClient mGeoDataClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168),new LatLng(71,136));
    PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    List<UtilityObject> utilityObjectList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.search_advance_fragment, container, false);
        ButterKnife.bind(getActivity());
        init();
        fileService = NetworkController.upload();
        getAllUtilities();
        resetRadioButton();
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getContext(),mGeoDataClient,LAT_LNG_BOUNDS,null);
        edtAddress.setAdapter(placeAutoCompleteAdapter);
        edtAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    doubleList = geoLocate();
                }
            }
        });
        edtAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        ){
                    // execute method for searching
                    doubleList = geoLocate();
                }
                return false;
            }
        });
        return mMainView;
    }



    private void init() {
        rb1km = mMainView.findViewById(R.id.rb1km);
        rb500m =mMainView.findViewById(R.id.rb500m);
        rb5km = mMainView.findViewById(R.id.rb5km);
        rb8km = mMainView.findViewById(R.id.rb8km);
        rb10km = mMainView.findViewById(R.id.rb10km);
        btnSearch = mMainView.findViewById(R.id.btnUpload);
        rbBelowMil = mMainView.findViewById(R.id.rbBelowOne);
        rbBtOneVsTwo = mMainView.findViewById(R.id.rbBtOneVsTwo);
        rbTwoVsThree = mMainView.findViewById(R.id.rbBtTwoVsThree);
        rbMtThree = mMainView.findViewById(R.id.rbMoreThanThree);
        edtAddress = mMainView.findViewById(R.id.edtSearch);
        imgBack = mMainView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        gvCheckBox = mMainView.findViewById(R.id.gridCheckBox);
        progressBar = mMainView.findViewById(R.id.spin_kit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBack:
                FragmentSearchAdvance.this.dismiss();
            break;
            case R.id.btnUpload:
                searchRoom();
                break;
        }
    }

    private void searchRoom() {
        if(rbBelowMil.isChecked()){

        }else if(rbBtOneVsTwo.isChecked()){

        }else if(rbTwoVsThree.isChecked()){

        }else if(rbMtThree.isChecked()){

        }else{
            Toast.makeText(getActivity(), "Bạn chưa chọn giá tiền mong muốn", Toast.LENGTH_SHORT).show();
            return;
        }

        if(rb500m.isChecked()){

        }else if(rb1km.isChecked()){

        }else if(rb1km.isChecked()){

        }else if(rb1km.isChecked()){

        }else if(rb1km.isChecked()){

        }else{
            Toast.makeText(getActivity(), "Bạn chưa chọn khoảng cách", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public void getAllUtilities(){
        progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = fileService.getAllUtilities("code");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
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
                        CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(utilityObjectList,getContext());
                        gvCheckBox.setAdapter(checkBoxAdapter);
                        checkBoxAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private List<Double> geoLocate() {
        List<Double> listPos = new ArrayList<>();
        String searchContent = edtAddress.getText().toString();
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
            listPos.add(address.getLongitude());
            listPos.add(address.getLatitude());
        }
        return listPos;
    }
    private void resetRadioButton() {
        rbBelowMil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    rbBtOneVsTwo.setChecked(false);
                    rbMtThree.setChecked(false);
                    rbTwoVsThree.setChecked(false);
                }
            }
        });
        rbBtOneVsTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    rbBelowMil.setChecked(false);
                    rbMtThree.setChecked(false);
                    rbTwoVsThree.setChecked(false);
                }
            }
        });
        rbTwoVsThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    rbBtOneVsTwo.setChecked(false);
                    rbMtThree.setChecked(false);
                    rbBelowMil.setChecked(false);
                }
            }
        });
        rbMtThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    rbBtOneVsTwo.setChecked(false);
                    rbBelowMil.setChecked(false);
                    rbTwoVsThree.setChecked(false);
                }
            }
        });
    }
}
