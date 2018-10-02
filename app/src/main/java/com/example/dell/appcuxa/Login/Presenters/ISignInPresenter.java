package com.example.dell.appcuxa.Login.Presenters;

import android.app.Activity;

public interface ISignInPresenter {
    void ThucHienDangNhapFB(Activity activity, String token);
    void ThucHienDangNhapGG(Activity activity,String token);
}
