package com.example.dell.appcuxa.MainPage.MainPageViews.SavedTab.SavedView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.appcuxa.R;

public class FragmentSaved extends Fragment {
    private View mMainView;
    ImageView imgView;
    public FragmentSaved(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_saved, container, false);
        //imgView = mMainView.findViewById(R.id.imgView);
        //imgView.setImageBitmap(AppUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.fragment_first, 200, 200));
        return mMainView;
    }
}

