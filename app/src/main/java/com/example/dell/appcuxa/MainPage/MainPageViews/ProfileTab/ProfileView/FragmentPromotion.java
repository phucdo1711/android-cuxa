package com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.R;


public class FragmentPromotion extends DialogFragment implements View.OnClickListener {
    public View mMainView;
    private RobButton btnPresent;
    private RobBoldText tvPromotionCode;
    private ImageView imgBack;
    public FragmentPromotion(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_promotion, container, false);
        init();

        return mMainView;
    }

    private void init() {
        btnPresent = mMainView.findViewById(R.id.btnPresent);
        imgBack = mMainView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        btnPresent.setOnClickListener(this);
        tvPromotionCode = mMainView.findViewById(R.id.tvPromotionCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBack:
                FragmentPromotion.this.dismiss();
                break;
            case R.id.btnPresent:
                Toast.makeText(getContext(), "Mời mọc cái gì!!", Toast.LENGTH_SHORT).show();
        }
    }
}
