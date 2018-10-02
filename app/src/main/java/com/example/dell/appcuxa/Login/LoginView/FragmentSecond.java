package com.example.dell.appcuxa.Login.LoginView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.appcuxa.Utils.AppUtils;
import com.example.dell.appcuxa.R;


public class FragmentSecond extends Fragment {
    private View mMainView;
    private ImageView imgView;
    public FragmentSecond(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_login_second, container, false);
        imgView = mMainView.findViewById(R.id.imgView);
        imgView.setImageBitmap(
                AppUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.fragment_second, 200, 200));
        return mMainView;
    }

}
