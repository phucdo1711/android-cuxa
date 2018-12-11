package com.example.dell.appcuxa.MainPage.MainPageViews.NotiTab.NotiView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView.FragmentFeedback;
import com.example.dell.appcuxa.ObjectModels.NotiObject;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDetailNoti extends DialogFragment implements View.OnClickListener {
    public View mMainView;
    ImageView imgBack;
    NotiObject notiObject;
   public WebView webview;

    public FragmentDetailNoti() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_detail_noti, container, false);
        init();
        getDetailNoti();
        return mMainView;
    }

    private void getDetailNoti() {
        CuXaAPI cuXaAPI = NetworkController.upload();
        Call<NotiObject> call  = cuXaAPI.getDetailNoti("Bearer "+ AppUtils.getToken(getActivity()),notiObject.getId());
        call.enqueue(new Callback<NotiObject>() {
            @Override
            public void onResponse(Call<NotiObject> call, Response<NotiObject> response) {

                webview.loadData(response.body().getContent(), "text/html", "UTF-8");
            }

            @Override
            public void onFailure(Call<NotiObject> call, Throwable t) {

            }
        });
    }

    private void init() {
        webview = mMainView.findViewById(R.id.webview);
        imgBack = mMainView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                FragmentDetailNoti.this.dismiss();
                break;

        }
    }
    public void setObject(NotiObject object){
        this.notiObject = object;
    }
}