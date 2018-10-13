package com.example.dell.appcuxa.MainPage.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.MainPage.MainPageViews.FragmentUpRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ISendBackToEdit;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;

import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;

import com.example.dell.appcuxa.R;
import com.squareup.picasso.Picasso;


public class AdapterMyRoom extends RecyclerView.Adapter<AdapterMyRoom.ViewHolder> {
    private Context context;
    private ObjectListByOption roomInfos;
    private ISendBackToEdit iSendBackToEdit;
    public AdapterMyRoom(Context context, ObjectListByOption roomInfos, ISendBackToEdit iSendBackToEdit) {
        this.context = context;
        this.roomInfos = roomInfos;
        this.iSendBackToEdit = iSendBackToEdit;
    }

    @Override
    public AdapterMyRoom.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterMyRoom.ViewHolder holder, int position) {
        final RoomSearchItem info = roomInfos.getLstRoom()[position];
        holder.tvName.setText(info.getName());
        String image = info.getImages()[0].getSrc();
        Picasso.get().load(image).into(holder.imgHinh);
        holder.tvAddress.setText(info.getAddress()==null?"":info.getAddress());
        holder.tvPrice.setText(info.getPrice()==null?"":info.getPrice()+" đ");
        holder.tvPurpose.setText("NHÀ CHUNG");
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSendBackToEdit.sendBackToEdit(info);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO DELETE EVENT HERE
                Toast.makeText(context, "Chưa delete đc", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomInfos.getLstRoom().length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgHinh;
        public RobLightText tvPrice;
        public RobLightText tvAddress;
        public RobBoldText tvName;
        public RobLightText tvPurpose;
        public RobButton btnEdit;
        public RobButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imageRoom);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvName = itemView.findViewById(R.id.tvName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            tvPurpose = itemView.findViewById(R.id.tvPurpose);

        }
    }
}

