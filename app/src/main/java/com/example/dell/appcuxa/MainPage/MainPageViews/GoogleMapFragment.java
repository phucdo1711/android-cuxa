package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.appcuxa.MainPage.Adapter.PlaceAutoCompleteAdapter;
import com.example.dell.appcuxa.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class GoogleMapFragment extends DialogFragment implements OnMapReadyCallback, View.OnClickListener {
    private static final String TAG = "GoogleMapFragment";
    public View mMainView;
    protected GeoDataClient mGeoDataClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168),new LatLng(71,136));
    private static final String FIND_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private GoogleMap map;
    private ImageView imgBack;
    private ImageView imgMyLocation;
    SupportMapFragment supportMapFragment;
    AutoCompleteTextView edtSearchContent;
    PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMainView != null) {
            ViewGroup parent = (ViewGroup) mMainView.getParent();
            if (parent != null)
                parent.removeView(mMainView);
        }

        try {
            mMainView = inflater.inflate(R.layout.map_fragment, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        ButterKnife.bind(getActivity());
        getLocationPermission();
        init(mMainView);
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getContext(),mGeoDataClient,LAT_LNG_BOUNDS,null);
        edtSearchContent.setAdapter(placeAutoCompleteAdapter);
        imgMyLocation.setOnClickListener(this);
        imgBack.setOnClickListener(this);
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
        hideSoftKeybroad();
        return mMainView;
    }

    private void init(View mMainView) {
        edtSearchContent = mMainView.findViewById(R.id.edtSearch);
        imgMyLocation = mMainView.findViewById(R.id.imgMyLocation);
        imgBack = mMainView.findViewById(R.id.imgBack);
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
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }

    private void initMap() {
        supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), FIND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    // initialize our map
                    initMap();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getActivity(), "Map is ready", Toast.LENGTH_SHORT).show();
        map = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
            //ẩn phím vị trí của tôi
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
    private void getDeviceLocation(){
        Log.d(TAG,"getDeviceLocation: Get device current location");
        fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity());
        try{
            final Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();
                        if(currentLocation!=null){
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM,"My location");
                        }else{
                            Toast.makeText(getActivity(), "Chưa bật Vị trí", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getActivity(), "onComplete: unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (SecurityException e){
            Log.e(TAG,"getDeviceLocation: SecurityException: "+e.getMessage());
        }
    }
    private void moveCamera(LatLng latLng, float zoom,String title){
        Log.d(TAG,"moveCamera: moving camera to : lat"+latLng.latitude+", lng: "+latLng.longitude);
           map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        if(!title.equals("My location")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            map.addMarker(options);
        }
        hideSoftKeybroad();
    }
    private void hideSoftKeybroad(){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgMyLocation:
                getDeviceLocation();
                break;
            case R.id.imgBack:
                GoogleMapFragment.this.dismiss();
                break;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (supportMapFragment != null)
            getFragmentManager().beginTransaction().remove(supportMapFragment).commit();
    }
}
