package com.waydrow.newloveinn.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waydrow.newloveinn.MyClass.Event;
import com.waydrow.newloveinn.R;
import com.waydrow.newloveinn.adapter.EventAdapter;

import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by Waydrow on 2016/11/27.
 */

public class EventFragment extends Fragment {

    public EventFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_list, container, false);

        ArrayList<Event> events = new ArrayList<>();

        events.add(new Event(1, "测试1", "2016年11月27日", "这是一条测试信息", R.drawable.hotel1));
        events.add(new Event(2, "测试2", "2016年11月27日", "这是一条测试信息", R.drawable.hotel2));
        events.add(new Event(3, "测试3", "2016年11月27日", "这是一条测试信息", R.drawable.hotel3));

        EventAdapter eventAdapter = new EventAdapter(getActivity(), events);
        ListView listView = (ListView) rootView.findViewById(R.id.event_list);
        listView.setAdapter(eventAdapter);

        return rootView;
    }
}
