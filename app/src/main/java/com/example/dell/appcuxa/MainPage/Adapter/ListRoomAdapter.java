package com.example.dell.appcuxa.MainPage.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.appcuxa.ObjectModels.RoomInfo;
import com.example.dell.appcuxa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListRoomAdapter extends RecyclerView.Adapter<ListRoomAdapter.ViewHolder> {
    private Context context;
    private List<RoomInfo> roomInfos = new ArrayList<>();

    public ListRoomAdapter(Context context, List<RoomInfo> roomInfos) {
        this.context = context;
        this.roomInfos = roomInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoomInfo info = roomInfos.get(position);
        if(info.getPurpose().equals("empty")){
            holder.tvPurpose.setText("Tìm người thuê trọ");
        }else if(info.getPurpose().equals("graft")){
            holder.tvPurpose.setText("Tìm người ở ghép");
        }
        Picasso.get().load(info.getImage().get(0)).placeholder(R.drawable.default_image).into(holder.imgHinh);
        holder.tvAddress.setText(info.getAddress()==null?"":info.getAddress());
        holder.tvPrice.setText(info.getAddress()==null?"":info.getPrice()+" đ");
        holder.tvName.setText(info.getAddress()==null?"":info.getNameRoom());
    }

    @Override
    public int getItemCount() {
        return roomInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgHinh)
        public ImageView imgHinh;
        @BindView(R.id.tvPrice)
        public TextView tvPrice;
        @BindView(R.id.tvAddress)
        public TextView tvAddress;
        @BindView(R.id.tvName)
        public TextView tvName;
        @BindView(R.id.tvPurpose)
        public TextView tvPurpose;

        public ViewHolder(View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imgHinh);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPurpose = itemView.findViewById(R.id.tvPurpose);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvName = itemView.findViewById(R.id.tvName);
            //ButterKnife.bind(itemView);
        }
    }
}

