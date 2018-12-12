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

import com.example.dell.appcuxa.CustomeView.RobBoldText;
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
    RobBoldText tvTitle;
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
        if(notiObject!=null){
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
        }else{
            String contentWeb = "<h2>Ch&agrave;o mừng bạn đến với Cư X&aacute;</h2><p>Nếu bạn cần trợ gi&uacute;p vui l&ograve;ng gọi <strong>1900561252</strong> để được trợ gi&uacute;p</p><p><img src=\"http://saostyle.vn/wp-content/uploads/2017/02/%E1%BA%A2nh-1-T%E1%BB%9Bi-C%C6%B0-X%C3%A1-C%C3%A0-Ph%C3%AA-v%C3%A0o-nh%E1%BB%AFng-ng%C3%A0y-tr%E1%BB%9Di-%C4%91%E1%BB%95i-gi%C3%B3-600x411.jpg\" width=\"100%\" /></p><p style=\"text-align: right;\">&nbsp;</p><p style=\"text-align: right;\">Cư X&aacute; Team</p>";
            webview.loadData(contentWeb, "text/html", "UTF-8");
        }

    }

    private void init() {
        tvTitle = mMainView.findViewById(R.id.tvTitle);
        if(notiObject!=null){
            tvTitle.setText("Thông báo");
        }else{
            tvTitle.setText("Trợ giúp");
        }
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