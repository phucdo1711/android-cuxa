package com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.MainPage.MainPageViews.SearchTab.GenderBottomDialog;
import com.example.dell.appcuxa.R;
import com.github.ybq.android.spinkit.SpinKitView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentEditProfile extends DialogFragment implements View.OnClickListener,GenderBottomDialog.ICallBackGender {
    public View mMainView;
    CircleImageView imgAvatar;
    RobLightText tvChangeImage, tvGender, tvBirthday;
    SpinKitView progressDialog;
    RobEditText edtName, edtCurAddress, edtCurSchool, edtEmail,edtPhoneNo, edtCmnd;
    private ImageView imgBack;
    RobButton btnSaveChange;
    final Calendar newCalendar = Calendar.getInstance();
    public FragmentEditProfile(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        init();

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
                StartTime .show();
                break;

        }
    }

    private void submitChange() {

    }

    @Override
    public String getGender(String gender) {
        tvGender.setText(gender);
        return gender;
    }


}
