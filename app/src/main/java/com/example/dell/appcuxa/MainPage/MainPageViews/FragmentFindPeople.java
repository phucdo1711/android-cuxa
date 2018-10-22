package com.example.dell.appcuxa.MainPage.MainPageViews;

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
import com.example.dell.appcuxa.MainPage.Adapter.AdapterLiveTogether;
import com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView.FragmentFeedback;
import com.example.dell.appcuxa.R;

public class FragmentFindPeople extends DialogFragment implements View.OnClickListener {
    public View mMainView;
    private ImageView imgBack;
    public FragmentFindPeople(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_find_people, container, false);
        init();
        getSomeFriend();
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
                FragmentFindPeople.this.dismiss();
                break;
        }
    }

    public void getSomeFriend() {
        //gọi hàm list 1 số bạn ở đây.
        //Sau đó set adapter ở đây.
        //AdapterLiveTogether adapterLiveTogether = new AdapterLiveTogether();
        return ;
    }
}
