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

public class RestaurantFragment extends Fragment {

    public RestaurantFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_list, container, false);

        ArrayList<MyView> myViews = new ArrayList<>();

        // add some data
        myViews.add(new MyView("Fey Restaurant", "8:00-16:00", "El Camino Real"));
        myViews.add(new MyView("Chef Chu's", "9:00-16:05", "N San Antonio Rd, Los Altos"));
        myViews.add(new MyView("Genghix Asian Fusion", "9:00-17:20", "Redwood Rd, Castro Valley, CA "));
        myViews.add(new MyView("Uncle Yu's at the Vineyard", "8:30-18:00", "South Livermore Avenue, Livermore"));
        myViews.add(new MyView("Cooking Papa", "8:00-16:00", "Edgewater Blvd, Foster City, CA "));
        myViews.add(new MyView("Rickshaw Corner Restaurant", "8:40-12:30", "Edgewater Blvd, Foster City, CA"));
        myViews.add(new MyView("Courtyard San Mateo", "10:10-16:50", "Shell Blvd, Foster City, CA"));
        myViews.add(new MyView("Crowne Plaza Foster City", "9:20-15:20", "Chess Drive, Foster City, CA"));

        MyViewAdapter myViewAdapter = new MyViewAdapter(getActivity(), myViews);
        ListView listView = (ListView) rootView.findViewById(R.id.view_list);
        listView.setAdapter(myViewAdapter);

        return rootView;
    }
}
