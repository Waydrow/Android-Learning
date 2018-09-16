package com.waydrow.newloveinn.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waydrow.newloveinn.MyClass.Event;
import com.waydrow.newloveinn.MyClass.Organization;
import com.waydrow.newloveinn.R;

import java.util.List;

/**
 * Created by Waydrow on 2016/11/27.
 */

public class OrganizationAdapter extends ArrayAdapter<Organization> {

    public OrganizationAdapter(Context context, List<Organization> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.organization_item, parent, false);
        }

        Organization currentOrg = getItem(position);

        ImageView orgImageView = (ImageView) listItemView.findViewById(R.id.org_image);
        TextView orgNameTextView = (TextView) listItemView.findViewById(R.id.org_name);
        TextView orgDescTextView = (TextView) listItemView.findViewById(R.id.org_desc);
        TextView orgLocationTextView = (TextView) listItemView.findViewById(R.id.org_location);

        orgNameTextView.setText(currentOrg.getmName());
        orgDescTextView.setText(currentOrg.getmDesc());
        orgLocationTextView.setText(currentOrg.getmLocation());

        if(currentOrg.hasImage()) {
            orgImageView.setImageResource(currentOrg.getmImageResourceId());
            orgImageView.setVisibility(View.VISIBLE);
        } else {
            orgImageView.setVisibility(View.GONE);
        }

        return listItemView;

    }
}
