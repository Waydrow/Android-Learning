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
import com.waydrow.newloveinn.bean.Agency;
import com.waydrow.newloveinn.R;
import com.waydrow.newloveinn.util.API;

import java.util.List;

/**
 * Created by Waydrow on 2016/11/27.
 */

public class AgencyAdapter extends ArrayAdapter<Agency> {

    public AgencyAdapter(Context context, List<Agency> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.agency_item, parent, false);
        }

        Agency currentOrg = getItem(position);

        ImageView orgImageView = (ImageView) listItemView.findViewById(R.id.org_image);
        TextView orgNameTextView = (TextView) listItemView.findViewById(R.id.org_name);
        TextView orgLocationTextView = (TextView) listItemView.findViewById(R.id.org_location);

        orgNameTextView.setText(currentOrg.getName());
        orgLocationTextView.setText(currentOrg.getAddress());
        Glide.with(getContext()).load(API.IMG_HEADER + currentOrg.getPhoto()).crossFade().into(orgImageView);

        return listItemView;

    }
}
