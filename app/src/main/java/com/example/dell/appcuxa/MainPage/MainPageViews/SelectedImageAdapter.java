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
import com.example.dell.appcuxa.R;

import java.util.List;

public class SelectedImageAdapter extends BaseAdapter {
    List<byte[]> lstByteImage;
    Context context;
    ILogicDeleteImage deleteImage;


    public SelectedImageAdapter(List<byte[]> lstByteImage, Context context, ILogicDeleteImage deleteImage) {
        this.lstByteImage = lstByteImage;
        this.context = context;
        this.deleteImage = deleteImage;
    }

    @Override
    public int getCount() {
        return lstByteImage.size();
    }

    @Override
    public Object getItem(int i) {
        return lstByteImage.get(i);
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

        byte[] imageByte = lstByteImage.get(i);
         Bitmap bmp = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        viewHolder.imgHinh.setImageBitmap(bmp);
       viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               deleteImage.deleteImage(i,"");

           }
       });

        return convertView;
    }

}
