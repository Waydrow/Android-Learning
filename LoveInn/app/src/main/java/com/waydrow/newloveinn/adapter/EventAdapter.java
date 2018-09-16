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
import com.waydrow.newloveinn.R;

import java.util.List;

import static android.R.attr.resource;

/**
 * Created by Waydrow on 2016/11/27.
 */

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(Context context, List<Event> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.event_item, parent, false);
        }

        Event currentEvent = getItem(position);

        ImageView eventImageView = (ImageView) listItemView.findViewById(R.id.event_image);
        TextView eventTitleTextView = (TextView) listItemView.findViewById(R.id.event_title);
        TextView eventDescTextView = (TextView) listItemView.findViewById(R.id.event_desc);
        TextView eventTimeTextView = (TextView) listItemView.findViewById(R.id.event_time);

        eventTitleTextView.setText(currentEvent.getmTitle());
        eventDescTextView.setText(currentEvent.getmDesc());
        eventTimeTextView.setText(currentEvent.getmTime());

        if(currentEvent.hasImage()) {
            eventImageView.setImageResource(currentEvent.getmImageResourceId());
            eventImageView.setVisibility(View.VISIBLE);
        } else {
            eventImageView.setVisibility(View.GONE);
        }

        return listItemView;

    }
}
