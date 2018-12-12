package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.AdapterComment;
import com.example.dell.appcuxa.ObjectModels.CommentContent;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentShowMoreCmt extends DialogFragment implements View.OnClickListener{
    public View mMainView;
    private ImageView imgBack;
    CuXaAPI cuXaAPI;
    ImageView btnSendCmt;
    AdapterComment adapterComment;
    List<CommentContent> commentContents;
    RobEditText edtSendCmt;
    RecyclerView recComment;
    CommentContent commentContent;
    SwipeRefreshLayout swipeRefreshLayout;

    public FragmentShowMoreCmt() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_show_more_cmt, container, false);
        cuXaAPI = NetworkController.upload();
        init();
        commentContents = new ArrayList<>();
        commentContents.add(commentContent);
        adapterComment = new AdapterComment(getContext(),commentContents,true, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recComment.setLayoutManager(linearLayoutManager);
        recComment.setAdapter(adapterComment);
        adapterComment.notifyDataSetChanged();
        return mMainView;
    }

    private void init() {
        btnSendCmt = mMainView.findViewById(R.id.btnSendCmt);
        btnSendCmt.setOnClickListener(this);
        edtSendCmt = mMainView.findViewById(R.id.edtCmtContent);
        recComment =mMainView.findViewById(R.id.recComment);
        imgBack = mMainView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                FragmentShowMoreCmt.this.dismiss();
                break;
            case R.id.btnSendCmt:
                if(!edtSendCmt.getText().toString().trim().equals("")){
                    String content = edtSendCmt.getText().toString().trim();
                    sendComment(content);
                }else{
                    Toast.makeText(getActivity(), "Bạn chưa nhập nội dung", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void setObject(CommentContent object){
        this.commentContent = object;
    }

    public void sendComment(String content) {

        CommentContent commentObject = new CommentContent();
        commentObject.setContent(content);
        commentObject.setRoom(commentContent.getRoom());
        commentObject.setParent(commentContent.getId());
        Call<CommentContent> sendCmt = cuXaAPI.uploadCommentWithParent("Bearer " + AppUtils.getToken(getActivity()), commentObject);
        sendCmt.enqueue(new Callback<CommentContent>() {
            @Override
            public void onResponse(Call<CommentContent> call, Response<CommentContent> response) {
                if (response.isSuccessful()) {
                    edtSendCmt.setText("");
                    commentContents.add(response.body());
                    adapterComment.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CommentContent> call, Throwable t) {

            }
        });
    }
}
