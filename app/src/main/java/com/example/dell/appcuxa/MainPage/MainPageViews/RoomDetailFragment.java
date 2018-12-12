package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.ChatActivity;
import com.example.dell.appcuxa.CustomeView.MyGridView;
import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.AdapterComment;
import com.example.dell.appcuxa.MainPage.Adapter.CheckBoxAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.SlideImageAdapter;
import com.example.dell.appcuxa.MainPage.MainPageViews.SavedTab.SavedView.FragmentSaveRoom;
import com.example.dell.appcuxa.ObjectModels.CommentContent;
import com.example.dell.appcuxa.ObjectModels.ObjectChat;
import com.example.dell.appcuxa.ObjectModels.RoomCreatedObj;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.UtilityObject;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomDetailFragment extends DialogFragment implements View.OnClickListener, IBackToDetailRoom {
    public View mMainView;
    Toolbar toolbar;
    AdapterComment adapterComment;
    String id = "";
    List<CommentContent> commentObjectList;
    boolean isTrue = false;
    RoomSearchItem roomSearchItem = new RoomSearchItem();
    CircleImageView imgAvatar;
    SpinKitView progressDialog;
    RobButton btnMesNow;
    RobBoldText tvNameLandLord;
    List<UtilityObject> utilityObjectList = new ArrayList<>();
    private ImageView imgBack;
    CuXaAPI fileService;
    RobLightText tvType, tvLocation, tvSchedule, tvPrice, tvContentDesc, tvSmallType, tvArea, tvNumOfPpl;
    ImageView imgUpDown1, imgUpDown2, imgUpDown3, imgUpDown4;
    ViewPager imgHinh;
    CircleIndicator circleIndicator;
    RobBoldText tvName;
    MyGridView gvCheckBox;
    RecyclerView lstCmts;
    RobButton btnSaveChange;
    IBackToDetailRoom iBackToDetailRoom;
    CheckBox cbSave;
    boolean isShow1 = false;
    boolean isShow2 = false;
    boolean isShow3 = false;
    boolean isShow4 = false;
    RobEditText edtCmtContent;
    ImageView btnSentCmt;
    final Calendar newCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.layout_detail_room, container, false);
        fileService = NetworkController.upload();
        iBackToDetailRoom = this;
        init();
        if(isTrue){
            btnMesNow.setVisibility(View.GONE);
        }else {
            btnMesNow.setVisibility(View.VISIBLE);
        }

        getAllUtilities();

        return mMainView;
    }

    private void init() {
        edtCmtContent = mMainView.findViewById(R.id.edtCmtContent);
        btnSentCmt = mMainView.findViewById(R.id.btnSendCmt);
        btnSentCmt.setOnClickListener(this);
        lstCmts = mMainView.findViewById(R.id.lstCmts);
        btnMesNow = mMainView.findViewById(R.id.btnMesNow);
        btnMesNow.setOnClickListener(this);
        tvNameLandLord = mMainView.findViewById(R.id.tvNameLandLord);
        toolbar = mMainView.findViewById(R.id.toolbar);
        gvCheckBox = mMainView.findViewById(R.id.gridCheckBox);
        tvContentDesc = mMainView.findViewById(R.id.tvContentDesc);
        tvLocation = mMainView.findViewById(R.id.tvLocation);
        tvSchedule = mMainView.findViewById(R.id.tvSchedule);
        tvPrice = mMainView.findViewById(R.id.tvPrice);
        tvSmallType = mMainView.findViewById(R.id.tvSmallType);
        tvNumOfPpl = mMainView.findViewById(R.id.tvNumOfPpl);
        tvArea = mMainView.findViewById(R.id.tvArea);
        tvName = mMainView.findViewById(R.id.tvName);
        tvType = mMainView.findViewById(R.id.tvType);
        imgAvatar = mMainView.findViewById(R.id.imgAvatar);
        cbSave = mMainView.findViewById(R.id.imgSaveTb);
        imgBack = mMainView.findViewById(R.id.imgBackTb);
        imgBack.setOnClickListener(this);
        imgAvatar = mMainView.findViewById(R.id.imgAvatar);
        imgHinh = mMainView.findViewById(R.id.imgHinh);
        circleIndicator = mMainView.findViewById(R.id.indicator);
        imgUpDown1 = mMainView.findViewById(R.id.imgUpDown1);
        imgUpDown1.setOnClickListener(this);
        imgUpDown3 = mMainView.findViewById(R.id.imgUpDown3);
        imgUpDown3.setOnClickListener(this);
        imgUpDown2 = mMainView.findViewById(R.id.imgUpDown2);
        imgUpDown2.setOnClickListener(this);
        imgUpDown4 = mMainView.findViewById(R.id.imgUpDown4);
        imgUpDown4.setOnClickListener(this);

       /* progressDialog = mMainView.findViewById(R.id.spin_kit);
        progressDialog.setVisibility(View.GONE);*/
        id = roomSearchItem.getId();
        getListComment();
        Call<RoomSearchItem> getRoomById = fileService.getRoomById("Bearer " + AppUtils.getToken(getActivity()), id);
        getRoomById.enqueue(new Callback<RoomSearchItem>() {
            @Override
            public void onResponse(Call<RoomSearchItem> call, Response<RoomSearchItem> response) {
                if (response.isSuccessful()) {
                    if (response.body().getIsSaved() != null)
                        cbSave.setChecked(response.body().getIsSaved());
                }
            }

            @Override
            public void onFailure(Call<RoomSearchItem> call, Throwable t) {

            }
        });
        tvContentDesc.setText(roomSearchItem.getDescription());
        Picasso.get().load(roomSearchItem.getLandLord().getPicture()).placeholder(R.drawable.default_image).into(imgAvatar);
        tvName.setText(roomSearchItem.getName());
        tvArea.setText("Diện tích: " + roomSearchItem.getArea() + " m2");
        toolbar.setTitle(roomSearchItem.getName());
        tvPrice.setText(AppUtils.formatMoney2(roomSearchItem.getPrice()) + " đ");
        tvNumOfPpl.setText("Số người cho thuê: " + roomSearchItem.getAmountOfTenant() + " người");
        tvNameLandLord.setText(roomSearchItem.getLandLord().getName());
        tvLocation.setText(roomSearchItem.getAddress());
        Boolean isSaved = roomSearchItem.getIsSaved();
        if (isSaved != null && isSaved == true) {
            cbSave.setChecked(true);
        } else {
            cbSave.setChecked(false);
        }

        List<String> lstString = new ArrayList<>();
        for (int i = 0; i < roomSearchItem.getImages().length; i++) {
            lstString.add(roomSearchItem.getImages()[i].getSrc());
        }
        SlideImageAdapter slideImageAdapter = new SlideImageAdapter(getContext(), lstString);
        imgHinh.setAdapter(slideImageAdapter);
        slideImageAdapter.notifyDataSetChanged();
        circleIndicator.setViewPager(imgHinh);
        Picasso.get().load(roomSearchItem.getLandLord().getPicture()).into(imgAvatar);
      /*  cbSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    saveOrUnsave(false);
                }
            }
        });*/

        cbSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    saveOrUnsave(false);
                } else {
                    saveOrUnsave(true);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBackTb:
                RoomDetailFragment.this.dismiss();
                break;
            case R.id.imgUpDown1:
                if (!isShow1) {
                    isShow1 = true;
                    imgUpDown1.setImageDrawable(getResources().getDrawable(R.drawable.ic_down_arrow));
                    tvContentDesc.setVisibility(View.GONE);
                } else {
                    isShow1 = false;
                    imgUpDown1.setImageDrawable(getResources().getDrawable(R.drawable.ic_up_arrow));
                    tvContentDesc.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.imgUpDown2:
                if (!isShow2) {
                    isShow2 = true;
                    imgUpDown2.setImageDrawable(getResources().getDrawable(R.drawable.ic_down_arrow));
                    tvSmallType.setVisibility(View.GONE);
                    tvArea.setVisibility(View.GONE);
                    tvNumOfPpl.setVisibility(View.GONE);
                } else {
                    isShow2 = false;
                    imgUpDown2.setImageDrawable(getResources().getDrawable(R.drawable.ic_up_arrow));
                    tvSmallType.setVisibility(View.VISIBLE);
                    tvArea.setVisibility(View.VISIBLE);
                    tvNumOfPpl.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.imgUpDown3:
                if (!isShow3) {
                    isShow3 = true;
                    imgUpDown3.setImageDrawable(getResources().getDrawable(R.drawable.ic_down_arrow));
                    gvCheckBox.setVisibility(View.GONE);
                } else {
                    isShow3 = false;
                    imgUpDown3.setImageDrawable(getResources().getDrawable(R.drawable.ic_up_arrow));
                    gvCheckBox.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.imgUpDown4:
                if (!isShow4) {
                    isShow4 = true;
                    imgUpDown4.setImageDrawable(getResources().getDrawable(R.drawable.ic_down_arrow));
                    lstCmts.setVisibility(View.GONE);
                } else {
                    isShow4 = false;
                    imgUpDown4.setImageDrawable(getResources().getDrawable(R.drawable.ic_up_arrow));
                    lstCmts.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.imgSaveTb:
                saveOrUnsave(cbSave.isChecked());
                break;
            case R.id.btnSendCmt:
                if (!edtCmtContent.getText().toString().trim().equals("")) {
                    String content = edtCmtContent.getText().toString().trim();
                    sendComment(content);
                }
                break;
            case R.id.btnMesNow:
                String idUser = roomSearchItem.getLandLord().getId();
                String name = roomSearchItem.getName();
                RoomCreatedObj obj = new RoomCreatedObj(idUser, name);
                Call<ObjectChat> createRoom = fileService.createRoom("Bearer " + AppUtils.getToken(getActivity()), obj);
                createRoom.enqueue(new Callback<ObjectChat>() {
                    @Override
                    public void onResponse(Call<ObjectChat> call, Response<ObjectChat> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("object", response.body());
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onFailure(Call<ObjectChat> call, Throwable t) {

                    }
                });
                break;
        }
    }

    private void saveOrUnsave(final boolean saveOrUnsave) {
        if (AppUtils.haveNetworkConnection(getActivity())) {
            Call<ResponseBody> call = fileService.saveRoom("Bearer " + AppUtils.getToken(getActivity()), roomSearchItem.getId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if (saveOrUnsave) { // nếu nó đã check thì thành công sẽ là uncheck
                            cbSave.setChecked(true);
                        } else {
                            cbSave.setChecked(false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (saveOrUnsave) {
                        cbSave.setChecked(true);
                    } else {
                        cbSave.setChecked(false);
                    }
                    Toast.makeText(getActivity(), "Có lỗi sảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Đéo có mạng", Toast.LENGTH_SHORT).show();
            cbSave.setChecked(false);
        }
    }

    public RoomSearchItem setObject(RoomSearchItem roomSearchItem, String id) {
        this.roomSearchItem = roomSearchItem;
        if (roomSearchItem.getLandLord().getId().equalsIgnoreCase(id)) {
            isTrue = true;
        } else {
            isTrue = false;
        }
        return roomSearchItem;
    }

    public void getAllUtilities() {
        //progressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = fileService.getAllUtilities("code");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        //JSONArray  jsonArray = jsonObject.getJSONArray("rows");
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for (int i = 0; i < 12; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            UtilityObject utilityObject =
                                    new UtilityObject(object.getString("id"), object.getString("name"), object.getString("code"));
                            utilityObjectList.add(utilityObject);
                        }
                        if (roomSearchItem != null) {
                            String[] utilitiesSelected = roomSearchItem.getUtilities();
                            CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(utilityObjectList, getContext(), utilitiesSelected, true);
                            gvCheckBox.setAdapter(checkBoxAdapter);
                            checkBoxAdapter.notifyDataSetChanged();
                        } else {
                            CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter(utilityObjectList, getContext());
                            gvCheckBox.setAdapter(checkBoxAdapter);
                            checkBoxAdapter.notifyDataSetChanged();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void sendComment(String content) {
        CommentContent commentObject = new CommentContent();
        commentObject.setContent(content);
        commentObject.setRoom(id);
        Call<CommentContent> sendCmt = fileService.uploadCommentNoParent("Bearer " + AppUtils.getToken(getActivity()), commentObject);
        sendCmt.enqueue(new Callback<CommentContent>() {
            @Override
            public void onResponse(Call<CommentContent> call, Response<CommentContent> response) {
                if (response.isSuccessful()) {
                    edtCmtContent.setText("");
                    commentObjectList.add(response.body());
                    adapterComment.notifyDataSetChanged();
                    //logic thêm tin nhắn.
                }
            }

            @Override
            public void onFailure(Call<CommentContent> call, Throwable t) {

            }
        });
    }

    public void getListComment() {
        Call<CommentContent[]> getLstComment = fileService.getListComment("Bearer " + AppUtils.getToken(getActivity()), id, "createdAt");
        getLstComment.enqueue(new Callback<CommentContent[]>() {
            @Override
            public void onResponse(Call<CommentContent[]> call, Response<CommentContent[]> response) {
                if (response.isSuccessful()) {
                    commentObjectList = new ArrayList<>(Arrays.asList(response.body()));
                    adapterComment = new AdapterComment(getContext(), commentObjectList, iBackToDetailRoom, false);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    lstCmts.setLayoutManager(linearLayoutManager);
                    lstCmts.setAdapter(adapterComment);
                    adapterComment.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CommentContent[]> call, Throwable t) {

            }
        });
    }

    @Override
    public void sendBackObject(CommentContent commentContent) {
        FragmentShowMoreCmt fragment = new FragmentShowMoreCmt();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        fragment.setObject(commentContent);
        fragment.show(getFragmentManager(), "fragment_showmore_comment");
    }
}
