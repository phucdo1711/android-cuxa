package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.appcuxa.R;

import butterknife.ButterKnife;

public class FragmentSearchAdvance extends DialogFragment implements View.OnClickListener {
    public View mMainView;
    public ImageView imgBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.search_advance_fragment, container, false);
        ButterKnife.bind(getActivity());
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
                FragmentSearchAdvance.this.dismiss();
            break;
        }
    }
}
