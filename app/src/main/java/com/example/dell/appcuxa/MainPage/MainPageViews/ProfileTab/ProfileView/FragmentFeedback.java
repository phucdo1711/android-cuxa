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
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.ObjectModels.ContentObject;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFeedback extends DialogFragment implements View.OnClickListener {
    public View mMainView;
    private RobButton btnSendFeed;
    private RobEditText edtContentFeedback;
    private ImageView imgBack;
    public FragmentFeedback(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_feedback, container, false);
        init();

        return mMainView;
    }

    private void init() {
        btnSendFeed = mMainView.findViewById(R.id.btnSendFeedback);
        imgBack = mMainView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        btnSendFeed.setOnClickListener(this);
        edtContentFeedback = mMainView.findViewById(R.id.edtContentFeedback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBack:
                FragmentFeedback.this.dismiss();
                break;
            case R.id.btnSendFeedback:
                if(edtContentFeedback.getText().toString().trim().length()==0){
                    Toast.makeText(getContext(), "Mời bạn nhập vào nội dung", Toast.LENGTH_SHORT).show();
                }else{
                    if(AppUtils.haveNetworkConnection(getContext())){
                        uploadContent(edtContentFeedback.getText().toString().trim());
                    }else{
                        Toast.makeText(getContext(), "Mạng không khả dụng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    public void uploadContent(String content){
        CuXaAPI cuXaAPI = NetworkController.upload();
        ContentObject contentObject = new ContentObject();
        contentObject.setContent(content);
        Call<ResponseBody> call = cuXaAPI.uploadFeedBack("Bearer "+ AppUtils.getToken(getActivity()),contentObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Cám ơn bạn đã đóng góp ý kiến", Toast.LENGTH_SHORT).show();
                    edtContentFeedback.setText("");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
