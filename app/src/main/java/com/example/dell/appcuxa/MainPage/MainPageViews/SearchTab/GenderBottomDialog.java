package com.example.dell.appcuxa.MainPage.MainPageViews.SearchTab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.R;

public class GenderBottomDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    RobLightText tvBoy;
    RobLightText tvGirl;
    RobLightText tvBoth;
    ICallBackGender listener;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvBoy:
                listener.getGender("Nam");
                GenderBottomDialog.this.dismiss();
                break;
            case R.id.tvGirl:
                listener.getGender("Nữ");
                GenderBottomDialog.this.dismiss();
                break;
            case R.id.tvBoth:
                listener.getGender("Tất cả");
                GenderBottomDialog.this.dismiss();
                break;
        }

    }
    public static GenderBottomDialog newInstance() {
        return new GenderBottomDialog();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_choose_gender, container,
                false);
        init(view);

        return view;
    }

    private void init(View view) {
        tvBoy = view.findViewById(R.id.tvBoy);
        tvGirl = view.findViewById(R.id.tvGirl);
        tvBoth = view.findViewById(R.id.tvBoth);
        tvBoy.setOnClickListener(this);
        tvGirl.setOnClickListener(this);
        tvBoth.setOnClickListener(this);
    }
    public interface ICallBackGender {
        public String getGender(String gender);
    }
    public void setOnChooseGenderListener(ICallBackGender listener) {
        this.listener = listener;
    }

}
