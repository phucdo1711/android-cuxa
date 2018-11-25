package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.MainPage.MainPageViews.FragmentShowMoreCmt;
import com.example.dell.appcuxa.MainPage.MainPageViews.IBackToDetailRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.IBackToListTopScreen;
import com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView.FragmentPromotion;
import com.example.dell.appcuxa.ObjectModels.CommentContent;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {
    private Context context;
    List<CommentContent> commentContentList;
    IBackToDetailRoom iBackToDetailRoom;
    boolean isShowAllComment = false;
    boolean isViewAll = false;
    public AdapterComment(Context context, List<CommentContent> commentContentList, boolean isShowAllComment) {
        this.context = context;
        this.commentContentList = commentContentList;
        this.isShowAllComment = isShowAllComment;
    }
    public AdapterComment(Context context, List<CommentContent> commentContentList, boolean isShowAllComment,boolean isViewAll) {
        this.context = context;
        this.commentContentList = commentContentList;
        this.isShowAllComment = isShowAllComment;
        this.isViewAll = isViewAll;
    }
    public AdapterComment(Context context, List<CommentContent> commentContentList,IBackToDetailRoom iBackToDetailRoom, boolean isShowAllComment) {
        this.context = context;
        this.iBackToDetailRoom = iBackToDetailRoom;
        this.commentContentList = commentContentList;
        this.isShowAllComment = isShowAllComment;
    }

    @Override
    public AdapterComment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new AdapterComment.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterComment.ViewHolder holder, int position) {
        final CommentContent info = commentContentList.get(position);
        holder.tvName.setText(info.getUserObject().getName());
        Picasso.get().load(info.getUserObject().getPicture()).placeholder(R.drawable.default_image).into(holder.imgAvatar);
        if(info.getCommentObject()!=null){
            if(info.getCommentObject().length >2){
                int preCmtNum = info.getCommentObject().length-1;
                holder.tvPreCmtNum.setVisibility(View.VISIBLE);
                holder.tvPreCmtNum.setText("Xem "+preCmtNum+" phản hồi trước");
            }else{
                holder.tvPreCmtNum.setVisibility(View.GONE);
            }
        }

        holder.tvContentParent.setText(info.getContent()==null?"":info.getContent());
        if(info.getCommentObject()!=null){
            List<CommentContent> lstChildCmt = new ArrayList<>(Arrays.asList(info.getCommentObject()));
            AdapterChildComment adapterComment = new AdapterChildComment(context,lstChildCmt,isShowAllComment);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            holder.lstCmtChild.setLayoutManager(manager);
            holder.lstCmtChild.setAdapter(adapterComment);
            adapterComment.notifyDataSetChanged();
        }
        holder.lstCmtChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iBackToDetailRoom.sendBackObject(info);
            }
        });
        holder.tvPreCmtNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iBackToDetailRoom.sendBackObject(info);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iBackToDetailRoom.sendBackObject(info);
            }
        });
        if(isViewAll){
            holder.tvPreCmtNum.setVisibility(View.GONE);
        }
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

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvUserParent);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            lstCmtChild = itemView.findViewById(R.id.lstCmtChild);
            tvContentParent = itemView.findViewById(R.id.tvContentParent);
            tvPreCmtNum = itemView.findViewById(R.id.tvPreCmtNum);
        }
    }

}

