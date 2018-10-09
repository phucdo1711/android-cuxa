package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomInfo;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.RoomSearchResult;
import com.example.dell.appcuxa.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class AdapterSearchResultRoom extends RecyclerView.Adapter<AdapterSearchResultRoom.ViewHolder> {
    private Context context;
    private ObjectListByOption roomInfos;
    List<String> imageList = new ArrayList<>();

    public AdapterSearchResultRoom(Context context, ObjectListByOption roomInfos) {
        this.context = context;
        this.roomInfos = roomInfos;
    }

    @Override
    public AdapterSearchResultRoom.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterSearchResultRoom.ViewHolder holder, int position) {
        RoomSearchItem info = roomInfos.getLstRoom()[position];
            imageList.clear();
            holder.tvName.setText(info.getName());
            for(int i = 0;i<info.getImages().length;i++){
                imageList.add(info.getImages()[i].getSrc());
            }
        SlideImageAdapter slideImageAdapter = new SlideImageAdapter(context,imageList);
        holder.imgHinh.setAdapter(slideImageAdapter);
        slideImageAdapter.notifyDataSetChanged();
        holder.circleIndicator.setViewPager(holder.imgHinh);
        holder.tvAddress.setText(info.getAddress()==null?"":info.getAddress());
        holder.tvPrice.setText(info.getPrice()==null?"":info.getPrice()+" đ");
        holder.imgSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    // do something
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomInfos.getLstRoom().length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewPager imgHinh;
        public TextView tvPrice;
        public TextView tvAddress;
        public TextView tvName;
        CheckBox imgSave;
        CircleIndicator circleIndicator;

        public ViewHolder(View itemView) {
            super(itemView);
            circleIndicator = itemView.findViewById(R.id.indicator);
            imgHinh = itemView.findViewById(R.id.imgHinh);
            tvPrice = itemView.findViewById(R.id.tvMoney);
            imgSave = itemView.findViewById(R.id.imgSave);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvName = itemView.findViewById(R.id.tvName);

        }
    }
}

