package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.ObjectModels.Message;
import com.example.dell.appcuxa.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRoomChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public List<Message> lstMess;
    public View view;

    public MessageRoomChatAdapter(Context context, List<Message> lstMess) {
        this.context = context;
        this.lstMess = lstMess;
    }

    class ViewHolderUser extends RecyclerView.ViewHolder {
        RobLightText tvContentChatUser;
        RobLightText tvTimeUserSent;
        public ViewHolderUser(View itemView){
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
                    tvContentChatFriend.setVisibility(View.VISIBLE);
                }
            });

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == 10) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.item_chat_from_user, viewGroup, false);
            return new ViewHolderUser(view);

        } else if (viewType == 20) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.item_chat_from_other_user, viewGroup, false);
            return new ViewHolderFriend(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Message message = lstMess.get(i);

    }

    @Override
    public int getItemCount() {
        return lstMess.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = -1;
        /**
         * ĐIều kiện để chia bên.
         */
        return super.getItemViewType(position);

    }
}
