package com.waydrow.tourguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.resource;

/**
 * Created by Waydrow on 2016/11/19.
 */

public class MyViewAdapter extends ArrayAdapter<MyView> {

    public MyViewAdapter(Context context, List<MyView> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.view_item, parent, false);
        }

        // get current view
        MyView currentMyView = getItem(position);

        // from listItemView get these view
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image_view);
        TextView nameView = (TextView) listItemView.findViewById(R.id.view_name);
        TextView addressView = (TextView) listItemView.findViewById(R.id.view_address);
        TextView timeView = (TextView) listItemView.findViewById(R.id.view_time);

        // set values
        nameView.setText(currentMyView.getmName());
        addressView.setText(currentMyView.getmAddress());
        timeView.setText(currentMyView.getmTime());

        // 判断是否为图片
        if(currentMyView.hasImage()) {
            imageView.setImageResource(currentMyView.getmImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
