package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.MyGridView;
import com.example.dell.appcuxa.CustomeView.RobAutoCompleteTextView;
import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CustomeView.RobCheckBox;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.CheckBoxAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.ImageAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.PlaceAutoCompleteAdapter;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicDeleteImage;
import com.example.dell.appcuxa.MainPage.MainPageViews.SearchTab.GenderBottomDialog;
import com.example.dell.appcuxa.ObjectModels.ImageItem;
import com.example.dell.appcuxa.ObjectModels.LocationRoom;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomObject;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.UtilityObject;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class FragmentUpRoom extends DialogFragment implements ILogicDeleteImage, GenderBottomDialog.ICallBackGender,
        View.OnClickListener, AddPhotoBottomDialogFragment.OnChooseReasonListener, ImageAdapter.OnRecyclerViewItemClickListener {
    public static List<byte[]> imageBytes = new ArrayList<>();
    SharedPreferences sharedPreferences;
    public static final int IMAGE_ITEM_ADD = -1;
    CuXaAPI fileService;
    private View mMainView;
    @BindView(R.id.btnUpRoom)
    public RobButton btnUpload;
    @BindView(R.id.btnSelectImage)
    public RobButton btnSelectImage;
    @BindView(R.id.edtRoomName)
    public RobEditText edtRoomName;
    @BindView(R.id.edtPrice)
    public RobEditText edtPrice;
    @BindView(R.id.edtDienTich)
    public RobEditText edtDienTich;
    @BindView(R.id.edtPhoneNo)
    public RobEditText edtPhoneNo;
    @BindView(R.id.edtNumOfPpl)
    public RobEditText edtNumOfPeople;
    @BindView(R.id.edtGender)
    public RobLightText edtGender;
    @BindView(R.id.edtDesc)
    public RobEditText edtDesc;
    public RobCheckBox cbRent;
    public RobBoldText tvTitle;
    public RobCheckBox cbLiveTogether;
    RobAutoCompleteTextView edtAddress;
    RobEditText edtElecPrice;
    RobEditText edtWaterPrice;
    MyGridView gridView;
    public static SpinKitView progressBar;
    SelectedImageAdapter selectedImageAdapterUpdate;
    Double[] latlon;
    String type = "";
    String genderAccepted = "";
    RecyclerView recyclerView;
    List<Double> doubleList = new ArrayList<>();
    public static ArrayList<String> imageHinhId = new ArrayList<>();
    List<UtilityObject> utilityObjectList = new ArrayList<>();
    String token = "";
    ImageView imgBack;
    protected GeoDataClient mGeoDataClient;
    public RoomSearchItem roomSearchItem;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    SelectedImageAdapter selectedImageAdapter;
    PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    MyGridView gvCheckBox;

    public FragmentUpRoom() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_dang_phong, container, false);
        ButterKnife.bind(getActivity());
        init();

        if (roomSearchItem != null) {
            String purpose = roomSearchItem.getType();
            if (purpose.equals("empty")) {
                cbRent.setChecked(true);
            } else {
                cbLiveTogether.setChecked(true);
            }
            tvTitle.setText("Chỉnh sửa");
            edtRoomName.setText(roomSearchItem.getName());
            edtPrice.setText(roomSearchItem.getPrice());
            edtWaterPrice.setText(roomSearchItem.getWaterPrice() == null ? "" : roomSearchItem.getWaterPrice());
            edtElecPrice.setText(roomSearchItem.getElectricityPrice() == null ? "" : roomSearchItem.getElectricityPrice());
            edtDienTich.setText(roomSearchItem.getArea() == null ? "" : roomSearchItem.getArea());
            edtAddress.setText(roomSearchItem.getAddress() == null ? "" : roomSearchItem.getAddress());
            edtNumOfPeople.setText(roomSearchItem.getAmountOfTenant()==null?"":roomSearchItem.getAmountOfTenant());
            String gender = "";
            if (roomSearchItem.getGenderAccepted().equals("both")) {
                gender = "Tất cả";
            } else if (roomSearchItem.getGenderAccepted().equals("male")) {
                gender = "Nam";
            } else {
                gender = "Nữ";
            }
            edtGender.setText(roomSearchItem.getGenderAccepted().equals("") ? "" : gender);
            edtDesc.setText(roomSearchItem.getDescription() == null ? "" : roomSearchItem.getDescription());
        }
        fileService = NetworkController.upload();
        sharedPreferences = getActivity().getSharedPreferences("login_data", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getContext(), mGeoDataClient, LAT_LNG_BOUNDS, null);
        edtAddress.setAdapter(placeAutoCompleteAdapter);
        edtAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    doubleList = geoLocate();
                }
            }
        });
        edtAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        ) {
                    // execute method for searching
                    doubleList = geoLocate();
                }
                return false;
            }
        });
        edtAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doubleList = geoLocate();
                Log.d("location", doubleList.toString());
            }
        });
        if (roomSearchItem != null) {
            btnUpload.setText("Cập nhật");
            List<ImageItem> lstImages = new ArrayList<>(Arrays.asList(roomSearchItem.getImages()));
            selectedImageAdapterUpdate = new SelectedImageAdapter(getContext(), this, lstImages);
            gridView.setAdapter(selectedImageAdapterUpdate);
            selectedImageAdapterUpdate.notifyDataSetChanged();
        }
        if (Paper.book().read("utilities") != null) {
            if (roomSearchItem != null) {
                utilityObjectList = Paper.book().read("utilities");
                String[] utilitiesSelected = roomSearchItem.getUtilities();
                CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(utilityObjectList, getContext(), utilitiesSelected);
                gvCheckBox.setAdapter(checkBoxAdapter);
                checkBoxAdapter.notifyDataSetChanged();
            } else {
                utilityObjectList = Paper.book().read("utilities");
                CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(utilityObjectList, getContext());
                gvCheckBox.setAdapter(checkBoxAdapter);
                checkBoxAdapter.notifyDataSetChanged();
            }
        } else {
            getAllUtilities();
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            int pos = bundle.getInt("position", -1);
            Log.d("poss", pos + "");
        }

        return mMainView;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.edtGender:
                GenderBottomDialog genderBottomDialog = new GenderBottomDialog();
                genderBottomDialog.setOnChooseGenderListener(this);
                genderBottomDialog.show(getActivity().getSupportFragmentManager(),
                        "get_gender_dialog");
                break;
            case R.id.imgBack:
                FragmentUpRoom.this.dismiss();
                imageBytes.clear();
                break;
            case R.id.btnUpRoom:
                break;
            case R.id.btnSelectImage:
                showBottomDialog(AppUtils.PICK_IMAGE_1);
                break;
            case R.id.btnUpload:
                if (roomSearchItem != null) {
                    if (roomSearchItem.getImages().length < 3) {
                        Toast.makeText(getActivity(), "Bạn cần ít nhất 3 tấm ảnh phòng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (imageHinhId.size() < 3) {
                        Toast.makeText(getActivity(), "Bạn cần ít nhất 3 tấm ảnh phòng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (edtDienTich.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Bạn chưa nhập diện tích phòng", Toast.LENGTH_SHORT).show();
                }
                if (!cbLiveTogether.isChecked() && !cbRent.isChecked()) {
                    Toast.makeText(getActivity(), "Bạn chưa chọn mục đích", Toast.LENGTH_SHORT).show();
                    return;
                } else if (cbRent.isChecked()) {
                    type = "empty";
                } else {
                    type = "graft";
                }
                if (edtGender.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Bạn chưa chọn giới tính", Toast.LENGTH_SHORT).show();
                } else {
                    String gender = edtGender.getText().toString();
                    if (gender.equals("Tất cả")) {
                        genderAccepted = "both";
                    } else if (gender.equals("Nam")) {
                        genderAccepted = "male";
                    } else {
                        genderAccepted = "female";
                    }
                }

                latlon = new Double[doubleList.size()];
                latlon = doubleList.toArray(latlon);
                if (roomSearchItem != null) {
                    // do nothing
                } else {
                    if (latlon.length != 2 || edtAddress.getText().toString().trim().equals("")) {
                        Toast.makeText(getActivity(), "Bạn chưa chọn địa điểm", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(roomSearchItem!=null){
                    updateRoom();
                }else{
                    uploadRoom();
                }

                break;
        }
    }

    private void updateRoom() {
        String desc = "";
        String area = "";
        String amountOfTenant = "";
        List<String> utilis = new ArrayList<>();
        for (int i = 0; i < utilityObjectList.size(); i++) {
            if (utilityObjectList.get(i).isIsChecked()) {
                utilis.add(utilityObjectList.get(i).getId());
            }
        }
        String[] utiArray;
        if(roomSearchItem==null){
            utiArray = new String[utilis.size()];
            utiArray = utilis.toArray(utiArray);
        }else{
            utiArray = roomSearchItem.getUtilities();
        }

        String roomName = edtRoomName.getText().toString();

        String price = edtPrice.getText().toString();
        String electricPrice = edtElecPrice.getText().toString();
        String wterPrice = edtWaterPrice.getText().toString();
        String downPayment = "12343";
        LocationRoom locationRoom;
        if (roomSearchItem != null) {
            locationRoom = roomSearchItem.getLocationRoom();
        } else {
            locationRoom = new LocationRoom("Point", latlon);
        }
        String[] images;
        if(roomSearchItem!=null){
            List<String> idHinh = new ArrayList<>();
            for(int i = 0;i<roomSearchItem.getImages().length;i++){
                idHinh.add(roomSearchItem.getImages()[i].getId());
            }
            images = new String[idHinh.size()];
            images = idHinh.toArray(images);
        }else{
            images = new String[imageHinhId.size()];
            images = imageHinhId.toArray(images);
        }


        String address = edtAddress.getText().toString();

        desc = edtDesc.getText().toString();
        area = edtDienTich.getText().toString();
        amountOfTenant = edtNumOfPeople.getText().toString();
        progressBar.setVisibility(View.GONE);
        RoomObject roomObject = new RoomObject(desc, type, roomName, price, electricPrice, wterPrice, downPayment, locationRoom, address, images, area, amountOfTenant, genderAccepted, utiArray);
        Call<ResponseBody> call = fileService.updateRoom("Bearer " + token,roomSearchItem.getId(), roomObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    imageBytes.clear();
                    imageHinhId.clear();
                    FragmentUpRoom.this.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Upload thất bại", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void uploadRoom() {
        String desc = "";
        String area = "";
        String amountOfTenant = "";
        List<String> utilis = new ArrayList<>();
        for (int i = 0; i < utilityObjectList.size(); i++) {
            if (utilityObjectList.get(i).isIsChecked()) {
                utilis.add(utilityObjectList.get(i).getId());
            }
        }
        String[] utiArray;
        if(roomSearchItem==null){
            utiArray = new String[utilis.size()];
            utiArray = utilis.toArray(utiArray);
        }else{
            utiArray = roomSearchItem.getUtilities();
        }

        String roomName = edtRoomName.getText().toString();

        String price = edtPrice.getText().toString();
        String electricPrice = edtElecPrice.getText().toString();
        String wterPrice = edtWaterPrice.getText().toString();
        String downPayment = "12343";
        LocationRoom locationRoom;
        if (roomSearchItem != null) {
            locationRoom = roomSearchItem.getLocationRoom();
        } else {
            locationRoom = new LocationRoom("Point", latlon);
        }
        String[] images;
        if(roomSearchItem!=null){
            List<String> idHinh = new ArrayList<>();
            for(int i = 0;i<roomSearchItem.getImages().length;i++){
                idHinh.add(roomSearchItem.getImages()[i].getId());
            }
            images = new String[idHinh.size()];
            images = idHinh.toArray(images);
        }else{
            images = new String[imageHinhId.size()];
            images = imageHinhId.toArray(images);
        }


        String address = edtAddress.getText().toString();

        desc = edtDesc.getText().toString();
        area = edtDienTich.getText().toString();
        amountOfTenant = edtNumOfPeople.getText().toString();
        progressBar.setVisibility(View.GONE);
        RoomObject roomObject = new RoomObject(desc, type, roomName, price, electricPrice, wterPrice, downPayment, locationRoom, address, images, area, amountOfTenant, genderAccepted, utiArray);
        Call<ResponseBody> call = fileService.uploadRoom("Bearer " + token, roomObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Upload thành công", Toast.LENGTH_SHORT).show();
                    imageBytes.clear();
                    imageHinhId.clear();
                    FragmentUpRoom.this.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Upload thất bại", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    public void init() {
        tvTitle = mMainView.findViewById(R.id.tvTitle);
        edtWaterPrice = mMainView.findViewById(R.id.edtWaterPrice);
        edtElecPrice = mMainView.findViewById(R.id.edtElecPrice);
        edtDesc = mMainView.findViewById(R.id.edtDesc);
        edtNumOfPeople = mMainView.findViewById(R.id.edtNumOfPpl);
        gvCheckBox = mMainView.findViewById(R.id.gridCheckBox);
        cbRent = mMainView.findViewById(R.id.cbRent);
        cbLiveTogether = mMainView.findViewById(R.id.cbLiveTogether);
        edtAddress = mMainView.findViewById(R.id.edtSearch);
        btnUpload = mMainView.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(this);
        imgBack = mMainView.findViewById(R.id.imgBack);
        progressBar = mMainView.findViewById(R.id.spin_kit);
        imgBack.setOnClickListener(this);
        edtDienTich = mMainView.findViewById(R.id.edtDienTich);
        edtPrice = mMainView.findViewById(R.id.edtPrice);
        edtRoomName = mMainView.findViewById(R.id.edtRoomName);
        btnSelectImage = mMainView.findViewById(R.id.btnSelectImage);
        gridView = mMainView.findViewById(R.id.gridImage);
        btnSelectImage.setOnClickListener(this);
        recyclerView = mMainView.findViewById(R.id.recyclerView);
        edtAddress.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtGender = mMainView.findViewById(R.id.edtGender);
        edtGender.setOnClickListener(this);
        edtGender.setText("Tất cả");
        cbRent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbLiveTogether.setChecked(false);
                }
            }
        });
        cbLiveTogether.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cbRent.setChecked(false);
                }
            }
        });
    }

    public void showBottomDialog(int pos) {
        AddPhotoBottomDialogFragment addPhotoBottomDialogFragment =
                AddPhotoBottomDialogFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", imageBytes.size());
        if (imageBytes.size() > 0) {
            Toast.makeText(getActivity(), imageBytes.size() + "", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), imageBytes.size() + "", Toast.LENGTH_SHORT).show();
        }

        addPhotoBottomDialogFragment.setArguments(bundle);
        addPhotoBottomDialogFragment.setOnChooseReasonListener(this);
        addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                "add_photo_dialog_fragment");
    }

    @Override
    public void onChooseReason(List<byte[]> listByte, int pos) {
        imageBytes.addAll(listByte);
        selectedImageAdapter = new SelectedImageAdapter(imageBytes, getContext(), this);
        gridView.setAdapter(selectedImageAdapter);
        selectedImageAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(View view, int position) {
        showBottomDialog(AppUtils.PICK_IMAGE_1);
    }

    @Override
    public void deleteImage(final int pos, String id) {
        /**
         * Trường hợp chỉnh sửa, khi xóa thì không xóa hẳn mà chỉ bỏ bớt phần tử ra khỏi list
         * Nếu back(không cập nhật nữa) lại thì dữ liệu sẽ không có gì thay đổi.
         */
        if (roomSearchItem == null) {
            Call<ResponseBody> call = fileService.deleteImage("Bearer " + token, imageHinhId.get(pos));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                        imageBytes.remove(imageBytes.get(pos));
                        imageHinhId.remove(imageHinhId.get(pos));
                        selectedImageAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(), "Lỗi server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
           /* List<ImageItem> imageItems = new ArrayList<>(Arrays.asList(roomSearchItem.getImages()));
            imageItems.remove(imageItems.get(pos));
            selectedImageAdapterUpdate.notifyDataSetChanged();*/
        }
    }

    public void getAllUtilities() {
        progressBar.setVisibility(View.GONE);
        Call<ResponseBody> call = fileService.getAllUtilities("code");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        //JSONArray  jsonArray = jsonObject.getJSONArray("rows");
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for (int i = 0; i < 12; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            UtilityObject utilityObject =
                                    new UtilityObject(object.getString("id"), object.getString("name"), object.getString("code"));
                            utilityObjectList.add(utilityObject);
                        }
                        if (roomSearchItem != null) {
                            String[] utilitiesSelected = roomSearchItem.getUtilities();
                            CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(utilityObjectList, getContext(), utilitiesSelected);
                            gvCheckBox.setAdapter(checkBoxAdapter);
                            checkBoxAdapter.notifyDataSetChanged();
                        } else {
                            CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(utilityObjectList, getContext());
                            gvCheckBox.setAdapter(checkBoxAdapter);
                            checkBoxAdapter.notifyDataSetChanged();
                        }


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
            list = geocoder.getFromLocationName(searchContent, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("geoLocate", e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d("geoLocate, a location", address.toString());
            listPos.add(address.getLongitude());
            listPos.add(address.getLatitude());
        }
        return listPos;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.main_menu_animstyle;
    }

    @Override
    public String getGender(String gender) {
        edtGender.setText(gender);
        return gender;
    }

    public void setDataEdit(RoomSearchItem object) {
        if (object == null) {
            Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
        } else {
            roomSearchItem = object;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        imageBytes.clear();
        imageHinhId.clear();
    }
}

