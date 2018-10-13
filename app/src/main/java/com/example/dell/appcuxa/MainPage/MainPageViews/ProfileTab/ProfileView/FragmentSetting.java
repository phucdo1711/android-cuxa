package com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.appcuxa.R;

public class FragmentSetting extends DialogFragment implements View.OnClickListener {
    public View mMainView;
    private ImageView imgBack;
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBack:
                FragmentSetting.this.dismiss();
                break;

        }
    }
}
