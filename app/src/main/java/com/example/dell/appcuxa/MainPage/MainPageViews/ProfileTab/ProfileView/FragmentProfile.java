package com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.Login.LoginView.MainActivity;
import com.example.dell.appcuxa.MainPage.MainPageViews.FragmentSearchAdvance;
import com.example.dell.appcuxa.MainPage.MainPageViews.FragmentUpRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ISendBackToEdit;
import com.example.dell.appcuxa.MainPage.MainPageViews.MainPageActivity;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.R;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProfile extends Fragment implements View.OnClickListener, ISendBackToEdit{
    private View mMainView;
    ImageView imgEdit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RobBoldText tvUserName;
    String token = "";
    String username = "";
    LinearLayout lnLogout, lnFeedBack, lnHelp, lnSetting, lnQrCode,lnInviteFriend, lnCoupon,lnMyRoom;
    public FragmentProfile(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        sharedPreferences = getActivity().getSharedPreferences("login_data",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("name","");
        if(username.equals("")){
            tvUserName.setText("Người dùng Cư Xá");
        }else{
            tvUserName.setText(username);
        }
        return mMainView;
    }

    private void init() {
        tvUserName = mMainView.findViewById(R.id.tvUserName);
        imgEdit = mMainView.findViewById(R.id.imgEditprofile);
        lnLogout = mMainView.findViewById(R.id.layout_logout);
        lnFeedBack = mMainView.findViewById(R.id.layout_feedback);
        lnHelp = mMainView.findViewById(R.id.layout_help);
        lnSetting = mMainView.findViewById(R.id.layout_setting);
        lnQrCode = mMainView.findViewById(R.id.layout_qrcode);
        lnInviteFriend = mMainView.findViewById(R.id.layout_gift);
        lnCoupon = mMainView.findViewById(R.id.layout_coupon);
        lnMyRoom = mMainView.findViewById(R.id.layout_myroom);
        imgEdit.setOnClickListener(this);
        lnLogout.setOnClickListener(this);
        lnFeedBack.setOnClickListener(this);
        lnHelp.setOnClickListener(this);
        lnSetting.setOnClickListener(this);
        lnQrCode.setOnClickListener(this);
        lnInviteFriend.setOnClickListener(this);
        lnCoupon.setOnClickListener(this);
        lnMyRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_logout:
                logout();
                break;
            case R.id.layout_qrcode:
                FragmentQrCode fragment = new FragmentQrCode();
                //fragmentSearQuickAdva.setCancelable(false);
                fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                fragment.show(getFragmentManager(), "fragment_qrocde");
                break;
            case R.id.layout_myroom:
                FragmentMyRoom myRoom = new FragmentMyRoom();
                myRoom.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);
                myRoom.show(getFragmentManager(),"fragment_my_room");
                break;
            case R.id.layout_gift:
                FragmentPromotion promotion= new FragmentPromotion();
                promotion.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);
                promotion.show(getFragmentManager(),"fragment_promotion");
                break;
            case R.id.layout_feedback:
                FragmentFeedback fragmentFeedback= new FragmentFeedback();
                fragmentFeedback.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);
                fragmentFeedback.show(getFragmentManager(),"fragment_feedback");
                break;
            case R.id.layout_setting:
                FragmentSetting setting= new FragmentSetting();
                setting.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);
                setting.show(getFragmentManager(),"fragment_setting");
                break;
            case R.id.imgEditprofile:
                FragmentEditProfile fragmentEditProfile = new FragmentEditProfile();
                fragmentEditProfile.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);
                fragmentEditProfile.show(getFragmentManager(),"fragment_edit_profile");
                break;
        }
    }

    private void logout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setMessage("Bạn có muốn đăng xuất không?");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = getActivity().getSharedPreferences("login_data", MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

    @Override
    public void sendBackToEdit(RoomSearchItem object) {
        FragmentUpRoom myRoom = new FragmentUpRoom();
        myRoom.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);
        myRoom.setDataEdit(object);
        myRoom.show(getFragmentManager(),"fragment_uproom");
    }
}

