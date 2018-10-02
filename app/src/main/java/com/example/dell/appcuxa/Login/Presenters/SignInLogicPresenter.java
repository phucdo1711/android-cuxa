package com.example.dell.appcuxa.Login.Presenters;


import android.app.Activity;

import com.example.dell.appcuxa.Login.Models.ISignIn;
import com.example.dell.appcuxa.Login.Models.SignInModel;
import com.example.dell.appcuxa.Login.LoginView.LoginView;

public class SignInLogicPresenter implements ISignInPresenter {
    LoginView signInView;
    SignInModel signInModel;

    public SignInLogicPresenter(LoginView signUpView, ISignIn iSignIn){
        this.signInModel = new SignInModel(iSignIn);
        this.signInView = signUpView;
    }

    @Override
    public void ThucHienDangNhapFB(Activity activity, String token) {
        signInModel.DangNhapFacebook(activity,token);
    }

    @Override
    public void ThucHienDangNhapGG(Activity activity,String token) {
        signInModel.DangNhapGoogle(activity,token);
    }
}
