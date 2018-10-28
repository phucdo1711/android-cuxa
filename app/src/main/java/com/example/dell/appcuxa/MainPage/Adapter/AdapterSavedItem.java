package com.example.dell.appcuxa.MainPage.Adapter;

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

import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicSaveRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.IUnsaveRoomLogic;
import com.example.dell.appcuxa.MainPage.MainPageViews.RoomDetailFragment;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomInfo;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.RoomSearchResult;
import com.example.dell.appcuxa.ObjectModels.SavedRoom;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class AdapterSavedItem extends RecyclerView.Adapter<AdapterSavedItem.ViewHolder> {
    private Context context;
    private List<SavedRoom> roomInfos;
    List<String> imageList = new ArrayList<>();
    IUnsaveRoomLogic iLoginUnsave;

    public AdapterSavedItem(Context context, List<SavedRoom> roomInfos,IUnsaveRoomLogic iLoginUnsave) {
        this.context = context;
        this.roomInfos = roomInfos;
        this.iLoginUnsave = iLoginUnsave;
    }

    @Override
    public AdapterSavedItem.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterSavedItem.ViewHolder holder, int position) {
        final SavedRoom info = roomInfos.get(position);
        imageList.clear();
        holder.tvName.setText(info.getName());
        for(int i = 0;i<info.getImageObject().length;i++){
            imageList.add(info.getImageObject()[i].getSrc());
        }
        SlideImageAdapter slideImageAdapter = new SlideImageAdapter(context,imageList);
        holder.imgHinh.setAdapter(slideImageAdapter);
        slideImageAdapter.notifyDataSetChanged();
        holder.circleIndicator.setViewPager(holder.imgHinh);
        holder.tvAddress.setText(info.getAddress()==null?"":info.getAddress());
        holder.tvPrice.setText(info.getPrice()==null?"": AppUtils.formatMoney2(info.getPrice())+" đ");
        holder.imgSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){//do nothing
                }else{
                    iLoginUnsave.unSaveRoom(info);
                    AdapterSavedItem.this.notifyDataSetChanged();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLoginUnsave.backToSavedScreen(info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewPager imgHinh;
        public TextView tvPrice;
        public TextView tvAddress;
        public TextView tvName;
        CheckBox imgSave;
        CircleImageView imgAvatar;
        CircleIndicator circleIndicator;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            circleIndicator = itemView.findViewById(R.id.indicator);
            imgHinh = itemView.findViewById(R.id.imgHinh);
            tvPrice = itemView.findViewById(R.id.tvMoney);
            imgSave = itemView.findViewById(R.id.imgSave);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvName = itemView.findViewById(R.id.tvName);

        }
    }
}

