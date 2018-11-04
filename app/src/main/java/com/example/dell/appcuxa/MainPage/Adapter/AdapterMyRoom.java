package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.MainPage.MainPageViews.FragmentSearQuickAdva;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ISendBackToEdit;

import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;

import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterMyRoom extends RecyclerView.Adapter<AdapterMyRoom.ViewHolder> {
    private Context context;
    private ILogicDeleteRoom iLogicDeleteRoom;
    List<RoomSearchItem> roomSearchResults;
    private ISendBackToEdit iSendBackToEdit;
    public AdapterMyRoom(Context context, ILogicDeleteRoom iLogicDeleteRoom, List<RoomSearchItem> roomSearchResults) {
        this.context = context;
        this.roomSearchResults = roomSearchResults;
        this.iLogicDeleteRoom = iLogicDeleteRoom;

    }

    @Override
    public AdapterMyRoom.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterMyRoom.ViewHolder holder, int position) {
        final RoomSearchItem info = roomSearchResults.get(position);
        holder.tvName.setText((info.getName()==null)?"":info.getName());
        Picasso.get().load(info.getImages()[0].getSrc()).placeholder(R.drawable.default_image).into(holder.imgHinh);
        holder.tvAddress.setText(info.getAddress()==null?"":info.getAddress());
        holder.tvPrice.setText(info.getPrice()==null?"": AppUtils.formatMoney2(info.getPrice())+" đ");
        holder.tvPurpose.setText("NHÀ CHUNG");
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               iLogicDeleteRoom.backToEdit(info);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO DELETE EVENT HERE
                iLogicDeleteRoom.deleteRoom(info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomSearchResults.size();
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
    public interface ILogicDeleteRoom {
        public void deleteRoom(RoomSearchItem info);
        public void backToEdit(RoomSearchItem info);
    }

}

