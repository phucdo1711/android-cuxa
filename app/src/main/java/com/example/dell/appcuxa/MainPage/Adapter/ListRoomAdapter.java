package com.example.dell.appcuxa.MainPage.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.IBackToListTopScreen;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicSaveRoom;
import com.example.dell.appcuxa.ObjectModels.RoomInfo;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.ViewHolder> {
    private Context context;
    private ILogicSaveRoom iLogicSaveRoom;
    private IBackToListTopScreen iBackToListTopScreen;
    private List<RoomSearchItem> roomInfos = new ArrayList<>();

    public ListRoomAdapter(Context context, List<RoomSearchItem> roomInfos, ILogicSaveRoom iLogicSaveRoom,IBackToListTopScreen iBackToListTopScreen) {
        this.context = context;
        this.roomInfos = roomInfos;
        this.iBackToListTopScreen = iBackToListTopScreen;
        this.iLogicSaveRoom = iLogicSaveRoom;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RoomSearchItem info = roomInfos.get(position);
        if(info.getType().equals("empty")){
            holder.tvPurpose.setText("Tìm người thuê trọ");
        }else if(info.getType().equals("graft")){
            holder.tvPurpose.setText("Tìm người ở ghép");
        }
        //Picasso.get().load(info.getImage().get(0)).placeholder(R.drawable.default_image).into(holder.imgHinh);
        List<String> images = new ArrayList<>();
        for(int i = 0;i<info.getImages().length;i++){
            images.add(info.getImages()[i].getSrc());
        }
        SlideImageAdapter slideImageAdapter = new SlideImageAdapter(context,images);
        holder.imgHinh.setAdapter(slideImageAdapter);
        slideImageAdapter.notifyDataSetChanged();
        holder.circleIndicator.setViewPager(holder.imgHinh);
        holder.tvAddress.setText(info.getAddress()==null?"":info.getAddress());
        holder.tvPrice.setText(info.getAddress()==null?"": AppUtils.formatMoney2(info.getPrice())+" đ");
        holder.tvName.setText(info.getAddress()==null?"":info.getName());
        holder.cbSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                iLogicSaveRoom.saveRoom(info.getId());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iBackToListTopScreen.backToListTopScreen(info);
            }
        });

        holder.cardViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iBackToListTopScreen.backToListTopScreen(info);
            }
        });

    }

    @Override
    public int getItemCount() {
        return roomInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgHinh)
        public ViewPager imgHinh;
        @BindView(R.id.tvPrice)
        public TextView tvPrice;
        @BindView(R.id.tvAddress)
        public TextView tvAddress;
        @BindView(R.id.tvName)
        public TextView tvName;
        @BindView(R.id.tvPurpose)
        public TextView tvPurpose;
        CircleIndicator circleIndicator;
        CheckBox cbSave;
        CardView cardViewItem;

        public ViewHolder(View itemView) {
            super(itemView);
            cardViewItem = itemView.findViewById(R.id.layout_item);
            cbSave = itemView.findViewById(R.id.imgSave);
            circleIndicator = itemView.findViewById(R.id.indicator);
            imgHinh = itemView.findViewById(R.id.imgHinh);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPurpose = itemView.findViewById(R.id.tvPurpose);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvName = itemView.findViewById(R.id.tvName);
            //ButterKnife.bind(itemView);
        }
    }
}

