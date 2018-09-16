package com.waydrow.newloveinn.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waydrow.newloveinn.MyClass.Event;
import com.waydrow.newloveinn.MyClass.Organization;
import com.waydrow.newloveinn.R;
import com.waydrow.newloveinn.adapter.EventAdapter;
import com.waydrow.newloveinn.adapter.OrganizationAdapter;

import java.util.ArrayList;

/**
 * Created by Waydrow on 2016/11/27.
 */

public class OrganizationFragment extends Fragment {

    public OrganizationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.organization_list, container, false);

        ArrayList<Organization> orgs = new ArrayList<>();

        orgs.add(new Organization("测试13213", "这是一条测试信息", "山东省青岛市", R.drawable.hotel1));
        orgs.add(new Organization("测试233", "这是一条测试信息", "山东省青岛市", R.drawable.hotel2));
        orgs.add(new Organization("测试3", "这是一条测试信息", "山东省青岛市", R.drawable.hotel3));

        OrganizationAdapter orgAdapter = new OrganizationAdapter(getActivity(), orgs);
        ListView listView = (ListView) rootView.findViewById(R.id.organization_list);
        listView.setAdapter(orgAdapter);

        return rootView;
    }
}
