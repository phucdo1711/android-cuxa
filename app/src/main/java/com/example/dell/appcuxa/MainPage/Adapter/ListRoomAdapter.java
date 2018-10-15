package com.example.dell.appcuxa.MainPage.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicSaveRoom;
import com.example.dell.appcuxa.ObjectModels.RoomInfo;
import com.example.dell.appcuxa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.ViewHolder> {
    private Context context;
    private ILogicSaveRoom iLogicSaveRoom;
    private List<RoomInfo> roomInfos = new ArrayList<>();

    public ListRoomAdapter(Context context, List<RoomInfo> roomInfos, ILogicSaveRoom iLogicSaveRoom) {
        this.context = context;
        this.roomInfos = roomInfos;
        this.iLogicSaveRoom = iLogicSaveRoom;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RoomInfo info = roomInfos.get(position);
        if(info.getPurpose().equals("empty")){
            holder.tvPurpose.setText("Tìm người thuê trọ");
        }else if(info.getPurpose().equals("graft")){
            holder.tvPurpose.setText("Tìm người ở ghép");
        }
        //Picasso.get().load(info.getImage().get(0)).placeholder(R.drawable.default_image).into(holder.imgHinh);
        SlideImageAdapter slideImageAdapter = new SlideImageAdapter(context,info.getImage());
        holder.imgHinh.setAdapter(slideImageAdapter);
        slideImageAdapter.notifyDataSetChanged();
        holder.circleIndicator.setViewPager(holder.imgHinh);
        holder.tvAddress.setText(info.getAddress()==null?"":info.getAddress());
        holder.tvPrice.setText(info.getAddress()==null?"":info.getPrice()+" đ");
        holder.tvName.setText(info.getAddress()==null?"":info.getNameRoom());
        holder.cbSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                iLogicSaveRoom.saveRoom(info.getId());
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

        public ViewHolder(View itemView) {
            super(itemView);
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

