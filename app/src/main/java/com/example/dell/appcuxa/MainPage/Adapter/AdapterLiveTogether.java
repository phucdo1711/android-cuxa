package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ISendBackToEdit;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterLiveTogether extends RecyclerView.Adapter<AdapterLiveTogether.ViewHolder> {
    private Context context;
    List<RoomSearchItem> roomSearchResults;
    public AdapterLiveTogether(Context context, List<RoomSearchItem> roomSearchResults) {
        this.context = context;
        this.roomSearchResults = roomSearchResults;

    }

    @Override
    public AdapterLiveTogether.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_people, parent, false);
        return new AdapterLiveTogether.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterLiveTogether.ViewHolder holder, int position) {
        final RoomSearchItem info = roomSearchResults.get(position);
        holder.tvName.setText(info.getName());
        String image = info.getImages()[0].getSrc();
        Picasso.get().load(image).into(holder.imgHinh);
        holder.tvAddress.setText(info.getAddress()==null?"":info.getAddress());
        holder.tvPrice.setText(info.getPrice()==null?"":info.getPrice()+" đ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return roomSearchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imgHinh;
        public RobLightText tvPrice;
        public RobLightText tvAddress;
        public RobBoldText tvName;
        public RobLightText tvGender;

        public ViewHolder(View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imgAvatar);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvGender);


        }
    }
}

