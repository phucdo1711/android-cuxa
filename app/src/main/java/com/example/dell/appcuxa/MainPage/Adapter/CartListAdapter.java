package com.example.dell.appcuxa.MainPage.Adapter;

/**
 * Created by truongnv on 02/11/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.appcuxa.MainPage.MainPageViews.MessTab.MessView.Interface.CallbackChatRoom;
import com.example.dell.appcuxa.ObjectModels.Item;
import com.example.dell.appcuxa.ObjectModels.ObjectChat;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.google.android.gms.vision.text.Line;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
    private Context context;
    private List<ObjectChat> cartList;
    private CallbackChatRoom callbackChatRoom;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price;
        public ImageView thumbnail;
        public LinearLayout viewForeground;
        LinearLayout viewBackground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public CartListAdapter(Context context, List<ObjectChat> cartList,CallbackChatRoom callbackChatRoom) {
        this.context = context;
        this.cartList = cartList;
        this.callbackChatRoom = callbackChatRoom;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Date date = new Date();
        final ObjectChat item = cartList.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getName());
        holder.price.setText(AppUtils.parseDateFromWS(item.getCreatedAt()));

        Glide.with(context)
                .load(item.getUsers()[1].getPicture())
                .into(holder.thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackChatRoom.CallBackRoomChat(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        cartList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ObjectChat item, int position) {
        cartList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
    public void setFilter(List<ObjectChat> items) {
        cartList = new ArrayList<>();
        cartList.addAll(items);
        notifyDataSetChanged();
    }
}
