package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.ObjectModels.Message;
import com.example.dell.appcuxa.ObjectModels.MessageItem;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRoomChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public List<MessageItem> lstMess;
    public String avatFriend;
    public View view;

    public MessageRoomChatAdapter(Context context, List<MessageItem> lstMess, String avatFriend) {
        this.context = context;
        this.lstMess = lstMess;
        this.avatFriend = avatFriend;
    }

    class ViewHolderUser extends RecyclerView.ViewHolder {
        public RobLightText tvContentChatUser;
        RobLightText tvTimeUserSent;

        public ViewHolderUser(View itemView) {
            super(itemView);
            tvContentChatUser = itemView.findViewById(R.id.tvUserMessage);
            tvTimeUserSent = itemView.findViewById(R.id.tvUserTimeText);
            tvTimeUserSent.setVisibility(View.GONE);
            tvContentChatUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvTimeUserSent.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    class ViewHolderFriend extends RecyclerView.ViewHolder {
        public RobLightText tvTimeFriendSent;
        public RobLightText tvContentChatFriend;
        public CircleImageView imgAvatar;

        public ViewHolderFriend(View itemView) {
            super(itemView);
            tvTimeFriendSent = itemView.findViewById(R.id.tvTimeFriendSent);
            tvContentChatFriend = itemView.findViewById(R.id.tvFriendMessage);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvTimeFriendSent.setVisibility(View.GONE);
            tvContentChatFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getVisibility() == View.VISIBLE) {
                        tvContentChatFriend.setVisibility(View.GONE);
                    } else {
                        tvContentChatFriend.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == 1) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.item_chat_from_user, viewGroup, false);
            return new ViewHolderUser(view);

        } else if (viewType == 2) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.item_chat_from_other_user, viewGroup, false);
            return new ViewHolderFriend(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final MessageItem message = lstMess.get(i);
        if (viewHolder.getItemViewType() == 1) {
            final ViewHolderUser viewHolderUser = (ViewHolderUser) viewHolder;
            byte[] data = Base64.decode(message.getContent() == null ? "" : message.getContent(), Base64.DEFAULT);
            try {
                String text = new String(data, "UTF-8");
                viewHolderUser.tvContentChatUser.setText(text);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            viewHolderUser.tvContentChatUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!message.isTimeSeen()){
                        message.setTimeSeen(true);
                        viewHolderUser.tvTimeUserSent.setVisibility(View.VISIBLE);
                        viewHolderUser.tvTimeUserSent.setText(AppUtils.parseDateFromWS(message.getCreatedAt()));
                    }else{
                        message.setTimeSeen(false);
                        viewHolderUser.tvTimeUserSent.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            final ViewHolderFriend viewHolderFriend = (ViewHolderFriend) viewHolder;
            byte[] data = Base64.decode(message.getContent() == null ? "" : message.getContent(), Base64.DEFAULT);
            try {
                String text = new String(data, "UTF-8");
                viewHolderFriend.tvContentChatFriend.setText(text);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Picasso.get().load(avatFriend).placeholder(R.drawable.default_image).into(viewHolderFriend.imgAvatar);
            viewHolderFriend.tvContentChatFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!message.isTimeSeen()){
                        message.setTimeSeen(true);
                        viewHolderFriend.tvTimeFriendSent.setVisibility(View.VISIBLE);
                        viewHolderFriend.tvTimeFriendSent.setText(AppUtils.parseDateFromWS(message.getCreatedAt()));
                    }else{
                        message.setTimeSeen(false);
                        viewHolderFriend.tvTimeFriendSent.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lstMess.size();
    }

    @Override
    public int getItemViewType(int position) {
        /**
         * 1: User gửi tin nhắn
         * 2: Bạn gửi tin nhắn
         */
        MessageItem messageItem = lstMess.get(position);
        if (AppUtils.getIdUser(context).equals(messageItem.getUserObject().getId())) {
            return 1;
        }
        return 2;
    }
}
