package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicSaveRoom;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomInfo;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.RoomSearchResult;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSearchResultRoom extends RecyclerView.Adapter<AdapterSearchResultRoom.ViewHolder> {
    private Context context;
    private ObjectListByOption roomInfos;
    ILogicSaveRoom iLogicSaveRoom;
    CuXaAPI fileService;

    public AdapterSearchResultRoom(Context context, ObjectListByOption roomInfos,ILogicSaveRoom iLogicSaveRoom,CuXaAPI fileService) {
        this.context = context;
        this.roomInfos = roomInfos;
        this.iLogicSaveRoom = iLogicSaveRoom;
        this.fileService = fileService;
    }

    @Override
    public AdapterSearchResultRoom.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterSearchResultRoom.ViewHolder holder, int position) {
        final List<RoomSearchItem> roomSearchItemList = new ArrayList<>(Arrays.asList(roomInfos.getLstRoom()));
        final RoomSearchItem info = roomSearchItemList.get(position);

        List<String> imageList = new ArrayList<>();
         holder.tvName.setText((info.getName()==null)?"":info.getName());
         if(info.getImages()!=null){
             for(int i = 0;i<info.getImages().length;i++){
                 imageList.add(info.getImages()[i].getSrc());
             }
         }

        SlideImageAdapter slide = new SlideImageAdapter(context,imageList);
        slide.notifyDataSetChanged();
        holder.imgHinh.setAdapter(slide);
        slide.notifyDataSetChanged();
        if(info.getIsSaved()!=null && info.getIsSaved()){
            holder.imgSave.setChecked(true);
        }else {
            holder.imgSave.setChecked(false);
        }
        holder.circleIndicator.setViewPager(holder.imgHinh);
        holder.tvAddress.setText(info.getAddress()==null?"":info.getAddress());
        holder.tvPrice.setText(info.getPrice()==null?"": AppUtils.formatMoney2(info.getPrice())+" đ");
        holder.imgSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
             logicSaveUnsaveRoom(holder.imgSave,b,info);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLogicSaveRoom.backToScreen(info);
            }
        });
    }

    public void logicSaveUnsaveRoom(final CheckBox cb, final boolean b,RoomSearchItem item){
        if(AppUtils.haveNetworkConnection(context)){
            Call<ResponseBody> call = fileService.saveRoom("Bearer "+ AppUtils.getToken(context),item.getId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        cb.setChecked(b);
                    }else{
                        cb.setChecked(!b);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    cb.setChecked(!b);
                    Toast.makeText(context, "Có lỗi sảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            cb.setChecked(!b);
            Toast.makeText(context, "Đéo có mạng", Toast.LENGTH_SHORT).show();
        }
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

