package com.example.dell.appcuxa.Login.Models;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.ObjectModels.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInModel {
    ISignIn iSignIn;

    public SignInModel(ISignIn iSignIn) {
        this.iSignIn = iSignIn;
    }

    /**
     * Hàm đăng nhập bằng Facebook
     * @param activity
     * @param accessToken
     */
    public void DangNhapFacebook(final Activity activity, String accessToken ){
        Call<UserModel> loginObjectCall = NetworkController.signInAuth().getInfoUserByFacebook(accessToken);
        loginObjectCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (response.isSuccessful()&& response.body() != null && response.body().getUserObject() != null && response.body().getUserObject().getName() != null) {
                    Log.d("FACETAG", response.body().getUserObject().getName());
                    iSignIn.sendBackUserModel(response.body());
                } else {
                    Toast.makeText(activity, "Có lỗi xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                // do nothing.
            }
        });
    }

    /**
     * Hàm đăng nhập bằng google
     * @param activity
     * @param token
     */
    public void DangNhapGoogle(final Activity activity, String token){
        Call<UserModel> loginObjectCall = NetworkController.signInAuth().getInfoUserByGoogle(token);
        loginObjectCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (response.isSuccessful()&& response.body() != null && response.body().getUserObject() != null && response.body().getUserObject().getName() != null) {
                    Log.d("FACETAG", response.body().getUserObject().getName());
                    iSignIn.sendBackUserModel(response.body());
                } else {
                    Toast.makeText(activity, "Có lỗi xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                //do nothing
            }
        });
    }
}
