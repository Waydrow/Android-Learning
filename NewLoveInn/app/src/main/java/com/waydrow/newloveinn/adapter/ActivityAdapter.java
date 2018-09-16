package com.waydrow.newloveinn.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waydrow.newloveinn.bean.ActivitySummary;
import com.waydrow.newloveinn.R;
import com.waydrow.newloveinn.util.API;

import java.util.List;

/**
 * Created by Waydrow on 2016/11/27.
 */

public class ActivityAdapter extends ArrayAdapter<ActivitySummary> {

    public ActivityAdapter(Context context, List<ActivitySummary> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_item, parent, false);
        }

        ActivitySummary currentActivity = getItem(position);

        ImageView activityImageView = (ImageView) listItemView.findViewById(R.id.activity_image);
        TextView activityNameTextView = (TextView) listItemView.findViewById(R.id.activity_name);
        TextView activitySummaryTextView = (TextView) listItemView.findViewById(R.id.activity_summary);
        TextView activityBeginTimeTextView = (TextView) listItemView.findViewById(R.id.activity_begintime);

        activityNameTextView.setText(currentActivity.getName());
        activitySummaryTextView.setText(currentActivity.getSummary());
        activityBeginTimeTextView.setText(currentActivity.getBegintime());

        Glide.with(getContext()).load(API.IMG_HEADER + currentActivity.getPhoto()).crossFade().into(activityImageView);

        return listItemView;

    }
}
