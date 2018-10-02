package com.example.dell.appcuxa.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import butterknife.ButterKnife;

public abstract class FragmentCommon extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    protected View mView;
    protected Activity act;
    protected String permission = "";
    @Override
    public void onClick(View view) {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    protected abstract void unit(View v);

    protected abstract void setPermission();
}
