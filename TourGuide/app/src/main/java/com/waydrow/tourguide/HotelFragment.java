package com.waydrow.tourguide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Waydrow on 2016/11/19.
 */

public class HotelFragment extends Fragment {

    public HotelFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_list, container, false);

        ArrayList<MyView> myViews = new ArrayList<>();

        // add some data
        myViews.add(new MyView("青岛香格里拉大酒店", "8:00-16:00", "山东省青岛市城阳区兴阳路306号", R.drawable.hotel1));
        myViews.add(new MyView("青岛银沙滩温德姆至尊酒店", "9:00-16:05", "山东省青岛市崂山区崂山公园", R.drawable.hotel2));
        myViews.add(new MyView("李沧绿城喜来登酒店", "9:00-17:20", "山东省青岛市栈桥", R.drawable.hotel3));
        myViews.add(new MyView("鑫江温德姆酒店", "8:30-18:00", "山东省青岛市市南区", R.drawable.hotel4));
        myViews.add(new MyView("鲁商凯悦酒店", "8:00-16:00", "山东省青岛市松岭路238号", R.drawable.hotel5));
        myViews.add(new MyView("青岛中心假日酒店", "8:40-12:30", "北京市天安门广场", R.drawable.hotel6));
        myViews.add(new MyView("金沙滩希尔顿酒店", "10:10-16:50", "青岛市经济技术开发区嘉陵江东路1号", R.drawable.hotel7));
        myViews.add(new MyView("青岛证大喜玛拉雅酒店", "9:20-15:20", "山东省青岛市崂山区同安路880号", R.drawable.hotel8));

        MyViewAdapter myViewAdapter = new MyViewAdapter(getActivity(), myViews);
        ListView listView = (ListView) rootView.findViewById(R.id.view_list);
        listView.setAdapter(myViewAdapter);

        return rootView;
    }
}
