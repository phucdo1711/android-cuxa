package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.appcuxa.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SlideImageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> images;

    public SlideImageAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return view ==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.layout_custome_image,null);
        ImageView imageView = view1.findViewById(R.id.imageItem);
        Picasso.get().load(images.get(position)).into(imageView);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view1,0);
        return view1;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
