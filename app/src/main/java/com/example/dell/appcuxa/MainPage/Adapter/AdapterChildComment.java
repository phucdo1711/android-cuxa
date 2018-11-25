package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.IBackToListTopScreen;
import com.example.dell.appcuxa.ObjectModels.CommentContent;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChildComment extends RecyclerView.Adapter<AdapterChildComment.ViewHolder> {
    private Context context;
    List<CommentContent> commentContentList;
    boolean isShowAllCmt = false;
    public AdapterChildComment(Context context, List<CommentContent> commentContentList, boolean isShowAllCmt) {
        this.context = context;
        this.commentContentList = commentContentList;
        this.isShowAllCmt = isShowAllCmt;
    }

    @Override
    public AdapterChildComment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new AdapterChildComment.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterChildComment.ViewHolder holder, int position) {
        holder.tvPreCmtNum.setVisibility(View.GONE);
        if(commentContentList.size()>2&&!isShowAllCmt){ //không show hết
            if(position==commentContentList.size()-1){
                final CommentContent info = commentContentList.get(commentContentList.size()-1);
                holder.tvName.setText(info.getUserObject().getName());
                Picasso.get().load(info.getUserObject().getPicture()).placeholder(R.drawable.default_image).into(holder.imgAvatar);
                holder.tvContentParent.setText(info.getContent()==null?"":info.getContent());
            }else{
                holder.item_comment.setVisibility(View.GONE);
            }
        }else{ // show hết
            final CommentContent info = commentContentList.get(position);
            holder.tvName.setText(info.getUserObject().getName());
            Picasso.get().load(info.getUserObject().getPicture()).placeholder(R.drawable.default_image).into(holder.imgAvatar);
            holder.tvContentParent.setText(info.getContent()==null?"":info.getContent());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentContentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imgAvatar;
        public RecyclerView lstCmtChild;
        public RobLightText tvContentParent;
        public RobBoldText tvName;
        public RobBoldText tvPreCmtNum;
        public LinearLayout item_comment;

        public ViewHolder(View itemView) {
            super(itemView);
            item_comment = itemView.findViewById(R.id.item_comment);
            tvName = itemView.findViewById(R.id.tvUserParent);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            lstCmtChild = itemView.findViewById(R.id.lstCmtChild);
            tvContentParent = itemView.findViewById(R.id.tvContentParent);
            tvPreCmtNum = itemView.findViewById(R.id.tvPreCmtNum);
        }
    }
}

