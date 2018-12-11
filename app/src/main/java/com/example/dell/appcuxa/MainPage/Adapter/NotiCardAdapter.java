package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.appcuxa.MainPage.MainPageViews.MessTab.MessView.Interface.CallbackChatRoom;
import com.example.dell.appcuxa.ObjectModels.NotiObject;
import com.example.dell.appcuxa.ObjectModels.ObjectChat;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotiCardAdapter  extends RecyclerView.Adapter<NotiCardAdapter.MyViewHolder> {
    private Context context;
    private List<NotiObject> cartList;
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


    public NotiCardAdapter(Context context, List<NotiObject> cartList,CallbackChatRoom callbackChatRoom) {
        this.context = context;
        this.cartList = cartList;
        this.callbackChatRoom = callbackChatRoom;
    }

    @Override
    public NotiCardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);

        return new NotiCardAdapter.MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Date date = new Date();
        final NotiObject item = cartList.get(position);
        holder.name.setText(item.getTitle());
        holder.description.setText(item.getMessage());
        holder.price.setText(AppUtils.parseDateFromWS(item.getCreatedAt()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.thumbnail.setImageDrawable(context.getDrawable(R.drawable.ic_rent));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackChatRoom.CallBackNotiScreen(item);
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

    public void restoreItem(NotiObject item, int position) {
        cartList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
    public void setFilter(List<NotiObject> items) {
        cartList = new ArrayList<>();
        cartList.addAll(items);
        notifyDataSetChanged();
    }
}
