package com.waydrow.habittracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.waydrow.habittracker.R;
import com.waydrow.habittracker.data.Habit;

import java.util.List;

import static android.R.attr.resource;

/**
 * Created by Waydrow on 2016/11/18.
 */

public class HabitsAdapter extends ArrayAdapter<Habit> {

    public HabitsAdapter(Context context, List<Habit> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.habit_item, parent, false);
        }

        Habit currentHabit = getItem(position);

        // get textview
        TextView title = (TextView) listItemView.findViewById(R.id.title_textview);
        TextView location = (TextView) listItemView.findViewById(R.id.location_textview);
        TextView times = (TextView) listItemView.findViewById(R.id.times_textview);
        String timesString = Integer.toString(currentHabit.getTimes());

        // set text for these view
        title.setText(currentHabit.getTitle());
        location.setText(currentHabit.getLocation());
        times.setText(timesString);

        return listItemView;

    }
}
