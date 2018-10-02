package com.example.dell.appcuxa.MainPage.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.appcuxa.MainPage.MainPageViews.FragmentUpRoom;
import com.example.dell.appcuxa.ObjectModels.ImageItem;
import com.example.dell.appcuxa.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    List<ImageItem> uriList;
    Context context;
    public boolean isAdded = false;
    private OnRecyclerViewItemClickListener listener;

    public ImageAdapter(List<ImageItem> uriList,Context context) {
        this.uriList = uriList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        private int clickPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_img);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        public void bind(int position) {
            itemView.setOnClickListener(this);
            ImageItem item = uriList.get(position);
            if (isAdded && position == getItemCount() - 1) {
                imageView.setImageResource(R.drawable.selector_image_add);
                clickPosition = FragmentUpRoom.IMAGE_ITEM_ADD;
            } else {
                Bitmap bmp = BitmapFactory.decodeByteArray(item.getUri(), 0, item.getUri().length);
                imageView.setImageBitmap(bmp);
                clickPosition = position;
            }
        }

        @Override
        public void onClick(View view) {
            if (listener != null) listener.onItemClick(view, clickPosition);
        }
    }
    public void setImages(List<ImageItem> data) {
        uriList = new ArrayList<>(data);
        if (getItemCount() < 10) {
            uriList.add(new ImageItem());
            isAdded = true;
        } else {
            isAdded = false;
        }
        notifyDataSetChanged();
    }
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }
}
