package com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.MainPageViews.AddPhotoBottomDialogFragment;
import com.example.dell.appcuxa.MainPage.MainPageViews.SearchTab.GenderBottomDialog;
import com.example.dell.appcuxa.ObjectModels.UpdateUserObj;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEditProfile extends DialogFragment implements View.OnClickListener,GenderBottomDialog.ICallBackGender,DialogChooseImage.OnChooseReasonListener {
    public View mMainView;
    CircleImageView imgAvatar;
    RobLightText tvChangeImage, tvGender, tvBirthday;
    SpinKitView progressDialog;
    RobEditText edtName, edtCurAddress, edtCurSchool, edtEmail,edtPhoneNo, edtCmnd;
    private ImageView imgBack;
    CuXaAPI cuXaAPI;
    public static String urlImage = "";
    RobButton btnSaveChange;
    final Calendar newCalendar = Calendar.getInstance();
    public FragmentEditProfile(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        init();
        if(AppUtils.haveNetworkConnection(getContext())){
            getInfoMe();
        }else{
            Toast.makeText(getActivity(), "Không có mạng", Toast.LENGTH_SHORT).show();
        }

        return mMainView;
    }

    private void init() {
        tvGender = mMainView.findViewById(R.id.edtGender);
        tvGender.setOnClickListener(this);
        tvBirthday = mMainView.findViewById(R.id.edtBirthday);
        tvBirthday.setOnClickListener(this);
        imgBack = mMainView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        imgAvatar = mMainView.findViewById(R.id.imgAvatar);
        tvChangeImage = mMainView.findViewById(R.id.tvChangeImage);
        tvChangeImage.setOnClickListener(this);
        progressDialog = mMainView.findViewById(R.id.spin_kit);
        progressDialog.setVisibility(View.GONE);
        edtName = mMainView.findViewById(R.id.edtName);
        edtCurAddress = mMainView.findViewById(R.id.edtCurAddress);
        edtCurSchool = mMainView.findViewById(R.id.edtCurSchool);
        edtEmail = mMainView.findViewById(R.id.edtEmail);
        edtPhoneNo = mMainView.findViewById(R.id.edtPhoneNo);
        edtCmnd = mMainView.findViewById(R.id.edtCMND);
        btnSaveChange = mMainView.findViewById(R.id.btnSave);
        btnSaveChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBack:
                FragmentEditProfile.this.dismiss();
                break;
            case R.id.btnSave:
                submitChange();
                break;
            case R.id.tvChangeImage:
                showBottomDialog();
                break;
            case R.id.edtGender:
                GenderBottomDialog genderBottomDialog = new GenderBottomDialog();
                genderBottomDialog.setOnChooseGenderListener(this);
                genderBottomDialog.show(getActivity().getSupportFragmentManager(),
                        "get_gender_dialog");
                break;
            case R.id.edtBirthday:
                final SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
                final DatePickerDialog StartTime = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tvBirthday.setText(simpleDate.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                StartTime.show();
                break;

        }
    }

    private void submitChange() {
        cuXaAPI = NetworkController.upload();
        UpdateUserObj updateUserObj = new UpdateUserObj();
        String gender = tvGender.getText().toString();
        String genderUpload = "";
        if(gender.equals("Nam")){
            genderUpload = "male";
        }else if(gender.equals("Nữ")){
            genderUpload = "female";
        }else{
            genderUpload = "both";
        }
        updateUserObj.setGender(genderUpload);
        updateUserObj.setBirth(AppUtils.getCurrentUTC(AppUtils.parseStringToDate(tvBirthday.getText().toString())));
        updateUserObj.setIdCard(edtCmnd.getText().toString());
        updateUserObj.setSchool(edtCurSchool.getText().toString());
        updateUserObj.setCurrentResidence(edtCurAddress.getText().toString());
        updateUserObj.setPicture(urlImage);
        updateUserObj.setPhone(edtPhoneNo.getText().toString());

        Call<ResponseBody> call = cuXaAPI.updateInfoUser("Bearer "+ AppUtils.getToken(getActivity()),updateUserObj);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public String getGender(String gender) {
        tvGender.setText(gender);
        return gender;
    }

    public void showBottomDialog() {
        DialogChooseImage addPhotoBottomDialogFragment =
                DialogChooseImage.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", 9);

        addPhotoBottomDialogFragment.setArguments(bundle);
        addPhotoBottomDialogFragment.setOnChooseReasonListener(this);
        addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                "add_photo_dialog_fragment");
    }

    @Override
    public void onChooseReason(List<byte[]> bytes, int pos) {
        byte[] image = bytes.get(0);
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        imgAvatar.setImageBitmap(bmp);

    }

    private void getInfoMe() {
        CuXaAPI fileService = NetworkController.upload();
        Call<ResponseBody> getMe = fileService.getInfoMe("Bearer " + AppUtils.getToken(getActivity()));
        getMe.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        String phone = object.getString("phone");
                        String gender =object.getString("gender");
                        String email = object.getString("email");
                        String birth = object.getString("birth");
                        String name = object.getString("name");
                        String picture = object.getString("picture");
                        String idCard = object.getString("idCard");
                        edtCmnd.setText(idCard);
                        edtPhoneNo.setText(phone);
                        edtName.setText(name);
                        edtName.setEnabled(false);
                        edtEmail.setText(email);
                        edtEmail.setEnabled(false);
                        String sex = "";
                        if(gender.equals("male")){
                            sex = "Nam";
                        }else if(gender.equals("female")){
                            sex = "Nữ";
                        }else{
                            sex = "Tất cả";
                        }
                        tvGender.setText(sex);
                        Picasso.get().load(picture).placeholder(R.drawable.default_image).into(imgAvatar);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        Date date = dateFormat.parse(birth);
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
                        String dateStr = formatter.format(date);
                        tvBirthday.setText(dateStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
