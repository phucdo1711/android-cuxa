package com.example.dell.appcuxa.MainPage.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.dell.appcuxa.CustomeView.RobCheckBox;
import com.example.dell.appcuxa.ObjectModels.UtilityObject;
import com.example.dell.appcuxa.R;

import java.util.List;

public class CheckBoxAdapter extends BaseAdapter {
    public         LayoutInflater mInflater;
    List<UtilityObject> utilityObjects;
    Context context;

    public CheckBoxAdapter(List<UtilityObject> utilityObjects, Context context) {
        this.utilityObjects = utilityObjects;
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return utilityObjects.size();
    }

    @Override
    public Object getItem(int i) {
        return utilityObjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder{
        RobCheckBox checkbox;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.checkbox_item_in_grid, null);
            holder.checkbox = (RobCheckBox) convertView.findViewById(R.id.checkbox);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checkbox.setText(utilityObjects.get(i).name);

        if(i ==1){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_wifi ),null);
        }
        if(i ==2){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_garage ),null);
        }
        if(i ==3){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_toilet ),null);
        }
        if(i ==4){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_exit_door ),null);
        }
        if(i ==5){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_time ),null);
        }
        if(i ==6){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_chef ),null);
        }
        if(i ==7){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_fridge ),null);
        }
        if(i ==8){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_washing_machine ),null);
        }
        if(i ==9){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_slumber ),null);
        }
        if(i ==10){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_televisions ),null);
        }
        if(i ==11){
            holder.checkbox.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable( R.drawable.ic_dog ),null);
        }
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    utilityObjects.get(i).setChecked(true);
                }else{
                    utilityObjects.get(i).setChecked(false);
                }
            }
        });

        return convertView;
    }
}
