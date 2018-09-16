package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Waydrow on 2016/10/26.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {


    public EarthquakeAdapter(Context context, List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        /*填充震级, 保留一位小数*/
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        String formatMagnitude = formatMagnitude(currentEarthquake.getmMagnitude());
        magnitudeView.setText(formatMagnitude);
        /*震级圆圈背景颜色*/
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        /*填充地点*/
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offset);
        String location = currentEarthquake.getmLocation();
        String primaryLocation;
        String locationOffset;
        if(location.contains("of")) {
            String[] parts = location.split("of");
            primaryLocation = parts[1];
            locationOffset = parts[0] + "of";
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = location;
        }
        primaryLocationView.setText(primaryLocation);
        locationOffsetView.setText(locationOffset);

        /*转换时间格式*/
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        Date date = new Date(currentEarthquake.getmDate());
        dateView.setText(formatDate(date));
        timeView.setText(formatTime(date));

        return listItemView;
    }

    /*得到与震级匹配的颜色值*/
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int currentMagnitudeFloor = (int) Math.floor(magnitude);
        switch (currentMagnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }


        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    private String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return simpleDateFormat.format(date);
    }

    private String formatTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        return simpleDateFormat.format(date);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(magnitude);
    }
}
