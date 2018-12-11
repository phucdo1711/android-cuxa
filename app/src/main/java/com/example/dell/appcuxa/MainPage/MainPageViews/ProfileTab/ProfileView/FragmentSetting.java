package com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.dell.appcuxa.R;
import com.kyleduo.switchbutton.SwitchButton;

import io.paperdb.Paper;

import static android.content.Context.MODE_PRIVATE;

public class FragmentSetting extends DialogFragment implements View.OnClickListener {
    public View mMainView;
    private ImageView imgBack;
    public LinearLayout lnNoti;
    public LinearLayout lnDieuKhoan;
    public SwitchButton swipeButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public FragmentSetting(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_setting, container, false);
        init();
        swipeButton.setChecked(true);
        if(Paper.book().read("notification")!=null){
            boolean noti = Paper.book().read("notification");
            if(noti){
                swipeButton.setChecked(true);
            }else{
                swipeButton.setChecked(false);
            }
        }

        return mMainView;
    }

    private void init() {
        swipeButton = mMainView.findViewById(R.id.swipeButton);
        imgBack = mMainView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        lnDieuKhoan = mMainView.findViewById(R.id.layout_dieukhoan);
        lnNoti =mMainView.findViewById(R.id.layout_noti);
        lnDieuKhoan.setOnClickListener(this);
        lnNoti.setOnClickListener(this);
        final Notification notifica = new Notification();
        swipeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    notifica.defaults = 0;
                    Paper.book().delete("notification");
                    Paper.book().write("notification", false);
                }else{
                    Paper.book().delete("notification");
                    Paper.book().write("notification", true);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBack:
                FragmentSetting.this.dismiss();
                break;
            case R.id.layout_dieukhoan:
                FragmentDieuKhoan fragmentDieuKhoan = new FragmentDieuKhoan();
                fragmentDieuKhoan.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);
                fragmentDieuKhoan.show(getFragmentManager(),"fragment_dieukhoan");
                break;
        }
    }
}
