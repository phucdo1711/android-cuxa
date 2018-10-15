package com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.dell.appcuxa.R;

public class FragmentSetting extends DialogFragment implements View.OnClickListener {
    public View mMainView;
    private ImageView imgBack;
    public LinearLayout lnNoti;
    public LinearLayout lnDieuKhoan;
    public FragmentSetting(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_setting, container, false);
        init();

        return mMainView;
    }

    private void init() {
        imgBack = mMainView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        lnDieuKhoan = mMainView.findViewById(R.id.layout_dieukhoan);
        lnNoti =mMainView.findViewById(R.id.layout_noti);
        lnDieuKhoan.setOnClickListener(this);
        lnNoti.setOnClickListener(this);
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
