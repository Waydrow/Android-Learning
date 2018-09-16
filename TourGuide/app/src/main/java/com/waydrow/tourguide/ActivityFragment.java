package com.waydrow.tourguide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Waydrow on 2016/11/19.
 */

public class ActivityFragment extends Fragment {

    public ActivityFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_list, container, false);

        ArrayList<MyView> myViews = new ArrayList<>();

        // add some data
        myViews.add(new MyView("篮球赛", "8:00-16:00", "北京市海淀区新建宫门路19号"));
        myViews.add(new MyView("足球赛", "9:00-16:05", "北京市昌平区居庸关"));
        myViews.add(new MyView("田径越野", "9:00-17:20", "江苏省南京市建邺区水西门大街418号"));
        myViews.add(new MyView("羽毛球比赛", "8:30-18:00", "浙江省杭州市西湖区灵隐路法云弄1号"));
        myViews.add(new MyView("狼人杀", "8:00-16:00", "江苏省南京市玄武区中山陵四方城2号"));
        myViews.add(new MyView("谁是卧底", "8:40-12:30", "浙江省嘉善县西塘镇"));
        myViews.add(new MyView("斗地主", "10:10-16:50", " 江苏省苏州市平江区东北街178号"));
        myViews.add(new MyView("英雄联盟", "9:20-15:20", "浙江省杭州市西湖区灵溪南路"));

        MyViewAdapter myViewAdapter = new MyViewAdapter(getActivity(), myViews);
        ListView listView = (ListView) rootView.findViewById(R.id.view_list);
        listView.setAdapter(myViewAdapter);

        return rootView;
    }
}
