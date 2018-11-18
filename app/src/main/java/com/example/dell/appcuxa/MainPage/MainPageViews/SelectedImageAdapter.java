package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicDeleteImage;
import com.example.dell.appcuxa.ObjectModels.ImageItem;
import com.example.dell.appcuxa.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectedImageAdapter extends BaseAdapter {
    List<byte[]> lstByteImage;
    Context context;
    ILogicDeleteImage deleteImage;
    List<ImageItem> lstLinkImages;


    public SelectedImageAdapter(List<byte[]> lstByteImage, Context context, ILogicDeleteImage deleteImage) {
        this.lstByteImage = lstByteImage;
        this.context = context;
        this.deleteImage = deleteImage;
    }

    public SelectedImageAdapter(Context context, ILogicDeleteImage deleteImage, List<ImageItem> lstLinkImages) {
        this.context = context;
        this.deleteImage = deleteImage;
        this.lstLinkImages = lstLinkImages;
    }

    @Override
    public int getCount() {
        if(lstByteImage!=null){
            return lstByteImage.size();
        }else{
            return lstLinkImages.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if(lstByteImage!=null)
        return lstByteImage.get(i);
        else return lstLinkImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{
        ImageView imgHinh;
        ImageView imgDelete;
    }
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_image_room, null);

            viewHolder = new ViewHolder();

            viewHolder.imgDelete    = (ImageView) convertView.findViewById(R.id.imgDelete);
            viewHolder.imgHinh   = (ImageView) convertView.findViewById(R.id.img1);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(lstByteImage!=null){
            byte[] imageByte = lstByteImage.get(i);
            Bitmap bmp = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            viewHolder.imgHinh.setImageBitmap(bmp);
        }else{
            String linkImage = lstLinkImages.get(i).getSrc();
            Picasso.get().load(linkImage).placeholder(R.drawable.default_image).into(viewHolder.imgHinh);
        }
       viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               deleteImage.deleteImage(i,"");

           }
       });

        return convertView;
    }

}
